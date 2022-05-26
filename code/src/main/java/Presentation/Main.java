package Presentation;

import Presentation.Controllers.ShopController;
import Presentation.Controllers.UserController;
import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.ResponseList;
import domain.ResponseMap;
import domain.ResponseT;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.Product;
import domain.shop.Shop;
import domain.user.User;
import domain.user.filter.SearchProductFilter;
import domain.user.filter.SearchShopFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import javax.naming.AuthenticationException;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static final int port = 80;
    public static InetAddress ip;

    public static void main(String[] args) {
        Services.getInstance().StartMarket(new PaymentServiceImp(), new SupplyServiceImp(), "Admin", "Admin");
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

        app.before(userController::validateUser);

        app.get("/", ctx -> {
            String username = ctx.cookieStore("uid");
            ResponseList<Shop> response = Services.getInstance().GetShopsInfo(username, new SearchShopFilter());
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 503));
            }
            List<PresentationShop> shops = response.getValue().stream().map(PresentationShop::new).collect(Collectors.toList());
            PresentationUser user = userController.getUser(ctx);
            ctx.render("index.jte", Map.of("shops", shops, "user", user));
        });

        app.routes(() -> {

            //admin interface
            path("admin/systemMonitor",() -> {
                get(userController::renderAdminPage);
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
            ResponseMap<Integer, List<Product>> response = searchBy != null ? switch (searchBy) {
                case "products" -> Services.getInstance().SearchProductByName(user.getUsername(), query, new SearchProductFilter());
                case "category" -> Services.getInstance().SearchProductByCategory(user.getUsername(), query, new SearchProductFilter());
                case "keyword" -> Services.getInstance().SearchProductByKeyword(user.getUsername(), query, new SearchProductFilter());
                default -> new ResponseMap<Integer, List<Product>>("please choose a suitable query method");
            } : new ResponseMap<Integer, List<Product>>("not suppose to happen");
            if (response.isErrorOccurred()) {
                ctx.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
                return;
            }
            List<PresentationProduct> searchResult = new ArrayList<>();
            response.getValue().forEach((shopId, productList) -> searchResult.addAll(PresentationProduct.convertProduct(productList, shopId)));
            ctx.render("searchProducts.jte", Map.of("user", user, "products", searchResult));
        });

        app.exception(AuthenticationException.class, ((e, ctx) ->
                ctx.status(400).render("errorPage.jte", Map.of("errorMessage", e.getExplanation(), "status", 400))));
    }

    public static void fillData(){
        Services services = Services.getInstance();
        ResponseT<User> r = services.EnterMarket();
        String shay_guest = r.getValue().getUserName();
        services.Register(shay_guest,"shay", "123");

        r = services.EnterMarket();
        String shahar_guest = r.getValue().getUserName();
        services.Register(shahar_guest,"shahar", "123");

        services.Login(shay_guest,"shay","123",null);
        services.CreateShop("testing shop","shay","shop");
        services.AddProductToShopInventory(1, "Product1", "testing product", "test",1.90,15, "shay", 1);
        services.AddProductToShopInventory(2, "Product2", "testing product", "test",20,5, "shay", 1);
        services.AddProductToShopInventory(3, "Product3", "testing product", "test",20,0, "shay", 1);
        services.AddToShoppingCart("shay",1,1,5);
        services.Checkout("shay","shay havivyan","patish", "0506874838", "12345", "02/24");
        r = services.Logout("shay");
        shay_guest = r.getValue().getUserName();
        services.LeaveMarket(shay_guest);
        services.LeaveMarket(shahar_guest);
        //services.AppointNewShopManager(1, "shahar","shay");
    }
}
