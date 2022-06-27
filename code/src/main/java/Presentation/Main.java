package Presentation;

import Presentation.Controllers.ShopController;
import Presentation.Controllers.UserController;
import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Responses.ResponseMap;
import domain.Responses.ResponseT;
import domain.shop.Product;
import domain.shop.Shop;
import domain.user.User;
import domain.user.filter.SearchProductFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import javax.naming.AuthenticationException;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static final int port = 80;
    public static InetAddress ip;

    public static void main(String[] args) {
        //Services.getInstance().StartMarket(new PaymentServiceImp(), new SupplyServiceImp());
        UserController userController = new UserController();
        ShopController shopController = new ShopController(userController);
        Javalin app;

        // FOR MY SANITY ADD SOME OBJECTS TO THE SYSTEM
        fillData();

        try {
            ip = Inet4Address.getLocalHost();
            System.out.println(ip.getHostAddress());
        } catch (UnknownHostException e) {
            return;
        }
        app = Javalin.create(JavalinConfig::enableWebjars).start(port);


        Thread activeUsers = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 60);
                    userController.manageActiveUsers();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        activeUsers.start();

        app.routes(() -> {
            before(userController::validateUser);
            get(shopController::renderHomepage);
            //admin interface
            path("admin", () -> {
                path("{id}", () -> {
                    get("systemMonitor", userController::renderAdminPage);
                    ws("systemMonitor", userController::getSystemInfo);
                    get("systemMonitor/pastEntrances", userController::renderAdminPageFilteredEntrances);
                    post("removeUser", userController::deleteUserPermanently);

                    get("sales", userController::renderAllHistorySales);
                });

            });

            // all the users interfaces
            path("users", () -> {
                path("login", () -> {
                    get(userController::renderLogin);
                    post(userController::login);
                });
                path("new", () -> {
                    get(userController::renderRegister);
                    ws(userController::register);
                });

                path("{id}", () -> {
                    before(userController::checkUser);
                    post("/logout", userController::logout);
                    ws("/addToCart", userController::addToCart);
                    get("/orders", userController::renderUserOrderHistory);
                    get("/shops", userController::renderUserShops);
                    ws("/active", userController::activeUser);

                    path("messages", () -> {
                        get(userController::renderInbox);
                        ws(userController::messagesHandler);
                        ws("/getCount", userController::getMessagesCount);
                    });


                    path("cart", () -> {
                        get("checkoutForm", userController::renderCheckoutForm);
                        ws("checkout", userController::checkout);
                        get(userController::renderCart);
                        post("{serialNumber}/update", userController::updateAmountInCart);
                        post("{serialNumber}/remove", userController::removeFromCart);
                    });
                });
            });

            //all the shop interfaces
            path("shops", () -> {
                post(shopController::createShop);

                path("{shopID}", () -> {
                    get(shopController::renderShop);
                    get("/addProduct", shopController::renderAddProductPage);
                    get("/edit", shopController::renderEditShop);
                    post("/addProduct", shopController::addProduct);
                    post("/closeShop", shopController::closeShop);
                    post("/reopenShop", shopController::reopenShop);
                    get("/orders", shopController::renderOrderHistory);
                    post("/addRule", shopController::addPurchaseRule);
                    post("/combineRules", shopController::combineRules);
                    post("/deleteRule", shopController::deleteRule);
                    post("/addDiscount", shopController::addDiscount);
                    post("/deleteDiscount", shopController::deleteDiscount);
                    post("/composeDiscounts", shopController::composeDiscounts);

                    get("/bids", shopController::renderBidsPage);
                    post("/approveBid", shopController::approveBid);
                    post("/declineBid", shopController::declineBid);

                    post("/appointOwner", shopController::appointOwner);
                    post("/approveAppointment", shopController::approveAppointment);
                    post("/declineAppointment", shopController::declineAppointment);

                    path("{serialNumber}", () -> {
                        get(shopController::renderProductPage);
                        get("/edit", shopController::renderEditProduct);
                        post("/edit", shopController::editProduct);
                        post("/remove", shopController::removeProduct);
                    });

                    ws(shopController::editShop);
                });
            });
        });

        app.get("/search", ctx -> {
            String query = ctx.queryParam("query");
            String searchBy = ctx.queryParam("searchBy");
            PresentationUser user = userController.getUser(ctx);
            SearchProductFilter filter = shopController.getProductFilter(ctx);

            ResponseMap<Integer, List<Product>> response = searchBy != null ? switch (searchBy) {
                case "products" -> Services.getInstance().SearchProductByName(user.getUsername(), query, filter);
                case "category" -> Services.getInstance().SearchProductByCategory(user.getUsername(), query, filter);
                case "keyword" -> Services.getInstance().SearchProductByKeyword(user.getUsername(), query, filter);
                default -> new ResponseMap<>("please choose a suitable query method");
            } : new ResponseMap<>("not suppose to happen");
            if (response.isErrorOccurred()) {
                ctx.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
                return;
            }
            List<PresentationProduct> searchResult = new ArrayList<>();
            response.getValue().forEach((shopId, productList) -> searchResult.addAll(PresentationProduct.convertProduct(productList, shopId)));

            Double minPrice = filter.getMinPrice();
            Double maxPrice = filter.getMaxPrice();
            String category = filter.getCategory() == null ? "" : filter.getCategory();

            ctx.render("searchProducts.jte", Map.of("user", user, "products", searchResult, "minPrice", minPrice, "maxPrice", maxPrice, "category", category, "searchBy", searchBy, "query", query));
        });

        app.exception(AuthenticationException.class, ((e, ctx) ->
                ctx.status(400).render("errorPage.jte", Map.of("errorMessage", e.getExplanation(), "status", 400))));

    }

    public static void fillData() {
        Services services = Services.getInstance();
        ResponseT<User> r = services.EnterMarket();
        String shay_guest = r.getValue().getUserName();
        services.Register(shay_guest, "shay", "123");

        r = services.EnterMarket();
        String shahar_guest = r.getValue().getUserName();
        services.Register(shahar_guest, "shahar", "123");

        services.Login(shay_guest,"shay","123");
        Shop shop = services.CreateShop("testing shop","shay","shop").getValue();
        services.AddProductToShopInventory(1, "Product1", "testing product", "test",1.90,15, "shay", shop.getShopID());
        services.AddProductToShopInventory(2, "Product2", "testing product", "test",20,5, "shay", shop.getShopID());
        services.AddProductToShopInventory(3, "Product3", "testing product", "test",20,0, "shay", shop.getShopID());
        services.AddToShoppingCart("shay",shop.getShopID(),1,5);
        services.Checkout("shay","shay havivyan","patish","patish", "israel", "1" ,"0506874838", "12345","121", "02/24");
        r = services.Logout("shay");
        shay_guest = r.getValue().getUserName();
        services.LeaveMarket(shay_guest);
        services.LeaveMarket(shahar_guest);
        //services.AppointNewShopManager(1, "shahar","shay");
    }
}
