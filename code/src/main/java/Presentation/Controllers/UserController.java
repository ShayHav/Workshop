package Presentation.Controllers;

import Presentation.Model.*;
import Service.Services;
import domain.Response;
import domain.ResponseT;
import domain.shop.Shop;
import domain.user.Cart;
import domain.user.User;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;
import io.javalin.websocket.WsMessageContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private final Map<String, PresentationUser> requestedUsers;
    private final Services services;

    public UserController() {
        requestedUsers = new HashMap<>();
        services = Services.getInstance();
    }


    public void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        ResponseT<User> response = services.Login(username, password,null);
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
            ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
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
            PresentationUser requestedUser = ctx.messageAsClass(PresentationUser.class);
            Response response = services.Register(requestedUser.getUsername(), requestedUser.getPassword());
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
            ctx.status(400).render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
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
        Response response = services.EditShoppingCart(username,shopID,serialNumber,quantity);
        if(response.isErrorOccurred())
            ctx.status(400).render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));

        ctx.redirect(String.format("/users/%s/cart",username));
    }

    public void removeFromCart(Context ctx) {
        String username = ctx.pathParam("id");
        int shopID = ctx.formParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        Response response = services.RemoveFromShoppingCart(username,shopID,serialNumber);
        if(response.isErrorOccurred())
            ctx.status(400).render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));

        ctx.redirect(String.format("/users/%s/cart",username));
    }

    public void validateUser(Context ctx) {
        if (ctx.cookieStore("uid") == null) {
            ResponseT<User> response = services.EnterMarket();
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            } else {
                PresentationUser user = new PresentationUser(response.getValue());
                ctx.cookieStore("uid", user.getUsername());
            }
        }
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

}
