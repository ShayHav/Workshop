package Presentation;

import Presentation.Controllers.ShopController;
import Presentation.Controllers.UserController;
import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Response;
import domain.ResponseList;
import domain.ResponseMap;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.Product;
import domain.shop.Shop;
import domain.user.filter.SearchProductFilter;
import domain.user.filter.SearchShopFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

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
        UserController userController  = new UserController();
        ShopController shopController = new ShopController(userController);
        Javalin app;
        try{
            ip =  Inet4Address.getLocalHost();
            System.out.println(ip.getHostAddress());
        }
        catch (UnknownHostException e) {
            return;
        }
        app = Javalin.create(JavalinConfig::enableWebjars).start(port);
        app.before(userController::validateUser);

        app.get("/", ctx -> {
            String username = ctx.cookieStore("uid");
            ResponseList<Shop> response = Services.getInstance().GetShopsInfo(username, new SearchShopFilter());
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            }
            List<PresentationShop> shops = response.getValue().stream().map(PresentationShop::new).collect(Collectors.toList());
            PresentationUser user = userController.getUser(ctx);
            ctx.render("index.jte", Map.of("shops", shops, "user", user));
        });

        app.routes(() ->{
            // all the users interfaces
            path("users",() ->{
                path("login", ()->{
                    get(userController::renderLogin);
                    post(userController::login);
                });
                path("new", () ->{
                    get(userController::renderRegister);
                    ws(userController::register);
                });
                path("{id}/logout", () ->{
                    post(userController::logout);

                    path("{id}/addToCart/{shopID}/?item={serialNum}", ()->{
                        get(userController::addToCart);
                    });
                });
            });

            //all the shop interfaces
            path("shops", () -> {
                post(shopController::createShop);

                path("{shopID}", () ->{
                    get(shopController::renderShop);
                    get("/addProduct", shopController::renderAddProductPage);
                    post("/addProduct",shopController::addProduct);

                    path("{serialNumber}", () ->{
                        get(shopController::renderProductPage);
                        post("/edit", shopController::editProduct);
                    });
                });
            });

        });

        app.get("/search/", ctx ->{
            String query = ctx.pathParam("query");
            String searchBy = ctx.pathParam("searchBy");
            PresentationUser user = userController.getUser(ctx);
            switch (searchBy){
                case "Product":
                    ResponseMap<Integer, List<Product>> reposnse = Services.getInstance().SearchProductByName(user.getUsername(), query, new SearchProductFilter());
                    if(reposnse.isErrorOccurred()){
                        ctx.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", reposnse.errorMessage));
                        return;
                    }
                    List<PresentationProduct> searchResult = new ArrayList<>();
                    reposnse.getValue().forEach((shopId, productList) -> searchResult.addAll(PresentationProduct.convertProduct(productList, shopId)));
                    ctx.render("searchProducts.jte", Map.of("products", searchResult));
            }
        });
    }
}
