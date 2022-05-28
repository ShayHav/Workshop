package Presentation.Controllers;

import Presentation.Model.*;
import Presentation.Model.Messages.AddToCartMessage;
import Presentation.Model.Messages.CheckoutFormMessage;
import Presentation.Model.Messages.NotificationMessage;
import Presentation.Model.Messages.RegisterMessage;
import Service.Services;
import domain.Response;
import domain.ResponseList;
import domain.ResponseMap;
import domain.ResponseT;
import domain.notifications.Message;
import domain.notifications.SystemInfoMessage;
import domain.shop.Order;
import domain.shop.Shop;
import domain.user.Cart;
import domain.user.User;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;
import org.eclipse.jetty.util.ajax.JSON;

import javax.naming.AuthenticationException;
import java.util.*;
import java.util.stream.Collectors;

public class UserController {

    private final Map<String, PresentationUser> requestedUsers;
    private final Services services;

    public UserController() {
        requestedUsers = new HashMap<>();
        services = Services.getInstance();
    }

    public PresentationUser getUser(String username) {
        return requestedUsers.get(username);
    }

    public void login(Context ctx) {
        String guest = ctx.cookieStore("uid");
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        ResponseT<User> response = services.Login(guest, username, password, null);
        if (response.isErrorOccurred()) {
            int errorCode = 401;
            ctx.status(errorCode);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", errorCode));
        } else {
            PresentationUser user = new PresentationUser(response.getValue());
            user.setLoggedIn(true);
            synchronized (requestedUsers) {
                requestedUsers.put(user.getUsername(), user);
            }

            ctx.cookieStore("uid", user.getUsername());
            ctx.redirect("/");
        }
    }

    public void renderLogin(Context ctx) {
        PresentationUser user = getUser(ctx);
        ctx.render("login.jte", Map.of("user", user));
    }

    public PresentationUser getUser(Context ctx) {
        String username = ctx.cookieStore("uid");
        synchronized (requestedUsers) {
            if (requestedUsers.containsKey(username))
                return requestedUsers.get(username);
        }
        ResponseT<User> response = services.GetUser(username);
        if (response.isErrorOccurred()) {
            ctx.status(503);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 503));
            return null;
        }
        PresentationUser user = new PresentationUser(response.getValue());
        synchronized (requestedUsers) {
            requestedUsers.put(user.getUsername(), user);
        }
        return user;
    }

    public void register(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            System.out.println("register page connected via websocket");
        });

        wsConfig.onMessage(ctx -> {
            RegisterMessage message = ctx.messageAsClass(RegisterMessage.class);
            Response response = services.Register(message.getGuestUsername(), message.getUsername(), message.getPassword());
            ctx.send(response);
        });
    }

    public void renderRegister(Context ctx) {
        PresentationUser user = getUser(ctx);
        ctx.render("register.jte", Map.of("r_user", user));
    }

    public void logout(Context ctx) {
        String username = ctx.pathParam("id");
        ResponseT<User> guest = services.Logout(username);
        if (guest.isErrorOccurred()) {
            ctx.status(418);
            ctx.render("errorPage.jte", Map.of("errorMessage", guest.errorMessage, "status", 418));
        }
        ctx.cookieStore("uid", guest.getValue().getUserName());
        ctx.redirect("/");
    }

    public void addToCart(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            System.out.println("product page connected via websocket");
        });
        wsConfig.onMessage(ctx -> {
            AddToCartMessage message = ctx.messageAsClass(AddToCartMessage.class);
            Response response = services.AddToShoppingCart(message.getUsername(), message.getShopID(), message.getSerialNumber(), message.getQuantity());
            ctx.send(response);
        });
    }

    public void renderCart(Context ctx) {
        PresentationUser user = getUser(ctx);
        ResponseT<Cart.ServiceCart> response = services.ShowCart(user.getUsername());
        if (response.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }
        PresentationCart cart = new PresentationCart(response.getValue());
        //update final price after discounts for each product in cart
        updateFinalPriceOfProducts(cart);
        ctx.render("cart.jte", Map.of("user", user, "cart", cart));
    }

    public void updateAmountInCart(Context ctx) {
        String username = ctx.pathParam("id");
        int shopID = ctx.formParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        int quantity = ctx.formParamAsClass("quantity", Integer.class).get();
        Response response = services.EditShoppingCart(username, shopID, serialNumber, quantity);
        if (response.isErrorOccurred())
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));

        ctx.redirect(String.format("/users/%s/cart", username));
    }

    public void removeFromCart(Context ctx) {
        String username = ctx.pathParam("id");
        int shopID = ctx.formParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        Response response = services.RemoveFromShoppingCart(username, shopID, serialNumber);
        if (response.isErrorOccurred())
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));

        ctx.redirect(String.format("/users/%s/cart", username));
    }

    public void validateUser(Context ctx) {
        if (ctx.cookieStore("uid") == null) {
            ResponseT<User> response = services.EnterMarket();
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 503));
            } else {
                PresentationUser user = new PresentationUser(response.getValue());
                ctx.cookieStore("uid", user.getUsername());
            }
        }
    }

    public void renderCheckoutForm(Context ctx) {
        PresentationUser user = getUser(ctx);
        ctx.render("checkoutForm.jte", Map.of("user", user));
    }

    public void checkout(WsConfig wsConfig) {
        wsConfig.onMessage(ctx -> {
            String username = ctx.pathParam("id");
            CheckoutFormMessage checkout = ctx.messageAsClass(CheckoutFormMessage.class);
            String expirationDate = checkout.getMonth() + "/" + checkout.getYear();
            ResponseList<String> response = services.Checkout(username, checkout.getFullName(), checkout.getAddress(), checkout.getPhoneNumber(), checkout.getCardNumber(), expirationDate);
            StringBuilder error = new StringBuilder(response.isErrorOccurred() ? response.errorMessage : "");
            if (response.getValue().size() > 0) {
                for (String s : response.getValue()) {
                    error.append("/n").append(s);
                }

            }
            ctx.send(error);
        });

    }

    private void updateFinalPriceOfProducts(PresentationCart cart) {
        for (Integer shopID : cart.baskets.keySet()) {
            ResponseT<Shop> r = services.GetShop(shopID);
            Shop s = r.getValue();
            Map<PresentationProduct, Integer> productAmount = cart.baskets.get(shopID).productWithAmount;
            for (PresentationProduct p : productAmount.keySet()) {
                p.setFinalPrice(s.productPriceAfterDiscounts(p.serialNumber, productAmount.get(p)));
            }
        }
    }

    public void renderUserOrderHistory(Context ctx) {
        PresentationUser user = getUser(ctx);
        ResponseList<Order> response = services.getOrderHistoryOfUser(user.getUsername());

        if (response.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }

        List<PresentationOrder> orders = response.getValue().stream().map(PresentationOrder::new).collect(Collectors.toList());
        ctx.render("UserOrderHistory.jte", Map.of("user", user, "orders", orders));
    }

    public void renderUserShops(Context ctx) {
        PresentationUser user = getUser(ctx);
        ResponseList<Shop> response = services.GetAllUserShops(user.getUsername());
        if (response.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }

        List<PresentationShop> shops = response.getValue().stream().map(PresentationShop::new).collect(Collectors.toList());
        ctx.render("userShops.jte", Map.of("user", user, "shops", shops));
    }

    public void checkUser(Context context) throws AuthenticationException {
        PresentationUser currentUser = getUser(context);
        String requestedUsername = context.pathParam("id");
        if (!currentUser.getUsername().equals(requestedUsername)) {
            throw new AuthenticationException("you don't have privilege to view this page");
        }
    }


    public void renderAdminPage(Context ctx) {
        PresentationUser user = getUser(ctx);
        String username = user.getUsername();

        ResponseMap<Integer, User> users = services.getAllUsers(username);
        ResponseT<Integer> activeUsers = Services.getInstance().getCurrentActiveUsers(username);
        ResponseT<Integer> activeMembers = Services.getInstance().getCurrentActiveMembers(username);
        ResponseT<Integer> activeGuests = Services.getInstance().getCurrentActiveGuests(username);
        ResponseT<Integer> totalRegistered = Services.getInstance().getTotalMembers(username);

        if (users.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", users.errorMessage, "status", 400));
        }
        else if (activeUsers.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", activeUsers.errorMessage, "status", 400));
        }
        else if (activeMembers.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", activeMembers.errorMessage, "status", 400));
        }
        else if (activeGuests.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", activeGuests.errorMessage, "status", 400));
        }
        else if (totalRegistered.isErrorOccurred()) {
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", totalRegistered.errorMessage, "status", 400));
        }
        else if (!user.isAdmin()) {
            String errorMessage = "you don't have privilege to view this page";
            ctx.status(403).render("errorPage.jte", Map.of("errorMessage", errorMessage, "status", 403));
        }
        else {
            Map<Integer, PresentationUser> presentationUsers = new HashMap<>();
            for (Integer i : users.getValue().keySet()
            ) {
                presentationUsers.put(i, new PresentationUser(users.getValue().get(i)));
            }
            ctx.render("adminManagingPage.jte", Map.of("admin", user, "users", presentationUsers, "activeUsers", activeUsers.getValue(), "activeMembers", activeMembers.getValue(), "activeGuests", activeGuests.getValue(), "totalRegistered", totalRegistered.getValue()));
        }
    }

    public void messagesHandler(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            PresentationUser currentUser = getUser(ctx.pathParam("id"));
            Response response = services.registerForMessages(currentUser.getUsername(), (message) -> {
                if (message.getAddressee().getUserName().equals(currentUser.getUsername())) {
                    NotificationMessage notification = new NotificationMessage(message);
                    ctx.send(notification);
                }
            });
            if (response.isErrorOccurred()) {
                ctx.send(response);
            }
        });

        wsConfig.onMessage(ctx -> {
            NotificationMessage message = ctx.messageAsClass(NotificationMessage.class);

        });

        wsConfig.onClose(ctx -> {
            PresentationUser currentUser = getUser(ctx.cookie("uid"));
            services.removeFromNotificationCenter(currentUser.getUsername());
        });


    }

    public void renderInbox(Context context) {
        PresentationUser user = getUser(context);
        context.render("inbox.jte", Map.of("user", user));
    }

    public void getMessagesCount(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            PresentationUser currentUser = getUser(ctx.pathParam("id"));
            Response response = services.registerForMessages(currentUser.getUsername(), (message) -> {
                ResponseT<Long> numberOfUnreadMessages = services.getNumberOfUnreadMessages(currentUser.getUsername());
                if(!numberOfUnreadMessages.isErrorOccurred()){
                    ctx.send(numberOfUnreadMessages.getValue());
                }
            });
        });
    }

    public void getSystemInfo(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {
            PresentationUser currentUser = getUser(ctx.pathParam("id"));
            String username = currentUser.getUsername();

            Response response = services.registerForAdminMessages(username, () -> {
                ResponseT<Integer> currentActiveUsers = Services.getInstance().getCurrentActiveUsers(username);
                ResponseT<Integer> currentActiveMembers = Services.getInstance().getCurrentActiveMembers(username);
                ResponseT<Integer> currentActiveGuests = Services.getInstance().getCurrentActiveGuests(username);
                ResponseT<Integer> totalRegistered = Services.getInstance().getTotalMembers(username);
                ResponseMap<Integer, User> users = Services.getInstance().getAllUsers(username);

                Map<Integer, PresentationUser> presentationUsers = new HashMap<>();
                for (Integer i : users.getValue().keySet()
                ) {
                    presentationUsers.put(i, new PresentationUser(users.getValue().get(i)));

                }
                SystemInfoMessage message = new SystemInfoMessage(currentActiveUsers.getValue(), currentActiveMembers.getValue(), currentActiveGuests.getValue(), totalRegistered.getValue(), presentationUsers);
                ctx.send(message);
            });

        });
    }

    public void deleteUserPermanently(Context ctx) {
        String admin = ctx.pathParam("id");
        String username = ctx.formParam("username");
        Response response = services.DeleteUser(admin,username);
        if(response.isErrorOccurred()){
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }
        else{
            String path = "/admin/" + admin + "/systemMonitor";
            ctx.redirect(path);
        }
    }
}
