package Presentation;

import Presentation.Controllers.ShopController;
import Presentation.Controllers.UserController;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.ResponseList;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.Shop;
import domain.user.filter.SearchShopFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

import static io.javalin.apibuilder.ApiBuilder.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    static final int port = 8080;


    public static void main(String[] args) {
        Services.getInstance().StartMarket(new PaymentServiceImp(), new SupplyServiceImp(), "Admin", "Admin");
        UserController userController  = new UserController();
        ShopController shopController = new ShopController();
        Javalin app = Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.before(userController::validateUser);

        app.get("/", ctx -> {
            String username = ctx.cookieStore("uid");
            ResponseList<Shop> response = Services.getInstance().GetShopsInfo(username, new SearchShopFilter());
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            }
            List<Shop> shops = response.getValue();
            PresentationUser user = userController.getUser(ctx);
            ctx.render("index.jte", Map.of("shops", shops, "user", user));
        });

        app.routes(() ->{
            // all the users interfaces
            path("users",() ->{
                path("login", ()->{
                    get(userController::renderLogin);
                    post(userController::renderLogin);
                });
                path("new", () ->{
                    get(userController::renderRegister);
                    ws(userController::register);
                });
                path("{id}/logout", () ->{
                    post(userController::logout);
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
                        ;
                        post("/edit", shopController::editProduct);
                    });
                });
            });

        });
    }
}
