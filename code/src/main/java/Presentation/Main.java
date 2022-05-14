package Presentation;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Response;
import domain.ResponseList;
import domain.ResponseT;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.Shop;
import domain.user.User;
import domain.user.filter.SearchShopFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {

    static final int port = 8080;
    static final Services services = Services.getInstance();

    public static void main(String[] args) {
        services.StartMarket(new PaymentServiceImp(), new SupplyServiceImp(), "Admin", "Admin");
        Javalin app = Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.before(ctx -> {
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
        });

        app.get("/", ctx -> {
            String username = ctx.cookieStore("uid");
            ResponseList<Shop> response = services.GetShopsInfo(username, new SearchShopFilter());
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            }

            List<Shop> shops = response.getValue();
            PresentationUser user = getUser(ctx);
            ctx.render("index.jte", Map.of("shops", shops, "user", user));
        });

        app.get("users/login", ctx -> {
            PresentationUser user = getUser(ctx);
            ctx.render("login.jte", Map.of("user", user));
        });

        app.post("/users/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            ResponseT<User> response = services.Login(username, password);
            if (response.isErrorOccurred()) {
                int errorCode = 401;
                ctx.status(errorCode);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", errorCode));
            } else {
                ctx.cookieStore("uid", response.getValue().getUserName());
                ctx.redirect("/");
            }
        });

        app.ws("/users/new", ws -> {
            ws.onConnect(ctx -> {

            });
            ws.onMessage(ctx -> {
                PresentationUser requestedUser = ctx.messageAsClass(PresentationUser.class);
                Response response = services.Register(requestedUser.getUsername(), requestedUser.getPassword());
                ctx.send(response);
            });
        });

        app.get("/users/new", ctx -> {
            PresentationUser user = getUser(ctx);
            ctx.render("register.jte", Map.of("r_user", user));
        });

        app.post("/users/{id}/logout", ctx ->
        {
            String username = ctx.pathParam("id");
            ResponseT<User> guest = services.Logout(username);
            if (guest.isErrorOccurred()) {
                ctx.status(418);
                ctx.render("errorPage.jte", Map.of("errorMessage", guest.errorMessage, "status", 418));
            }
            ctx.cookieStore("uid", guest.getValue().getUserName());
            ctx.redirect("/");
        });

        app.post("/shops", ctx -> {
            String shopName = ctx.formParam("shopName");
            //TODO: also send desc
            String description = ctx.formParam("description");
            String username = ctx.cookieStore("uid");
            ResponseT<Shop> response = services.CreateShop(username, shopName);
            if (response.isErrorOccurred()) {
                ctx.status(418);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 418));
            } else {
                ctx.redirect("/");
            }
        });

        app.get("/shops/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            PresentationUser user = getUser(ctx);
            ResponseT<Shop> response = services.GetShop(id);
            if (response.isErrorOccurred()) {
                ctx.status(400);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
                return;
            }
            PresentationShop shop = new PresentationShop(response.getValue());
            ctx.render("shop.jte", Map.of("user", user, "shop", shop));
        });

        app.get("/shops/{shopID}/{serialNumber}", ctx->{
            int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
            int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
            PresentationUser user = getUser(ctx);
            ResponseT<Product> response =  services.getProduct(user.getUsername(),shopID, serialNumber);
            if(response.isErrorOccurred()){
               ctx.status(400);
               ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
               return;
            }
            //TODO get permission for user in the shop
            ctx.render("product.jte", Map.of("user", user, "product", response.getValue(), "shopId", shopID));
        });
    }

    public static PresentationUser getUser(Context ctx) {
        String username = ctx.cookieStore("uid");
        ResponseT<User> user = services.GetUser(username);
        if (user.isErrorOccurred()) {
            ctx.status(503);
            ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", user.errorMessage));
        }
        return new PresentationUser(user.getValue());
    }

}
