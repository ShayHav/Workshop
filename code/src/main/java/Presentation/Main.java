package Presentation;

import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Response;
import domain.ResponseT;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.ShopInfo;
import domain.user.User;
import domain.user.filter.SearchShopFilter;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;


import java.util.*;

public class Main {

    static final int port = 8080;
    static final Services services = Services.getInstance();

    public static void main(String[] args) {
        services.StartMarket(new PaymentServiceImp(),new SupplyServiceImp(), "Admin","Admin");
        Javalin app =Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.before(ctx->{
            if(ctx.cookieStore("uid") == null){
                ResponseT<User> response = services.EnterMarket();
                if(response.isErrorOccurred()){
                    ctx.status(503);
                    ctx.render("errorPage.jte", Collections.singletonMap("errorMessage" ,response.errorMessage));
                }
                else {
                    PresentationUser user = new PresentationUser(response.getValue());
                    ctx.cookieStore("uid", user.getUsername());
                }
            }
        });

        app.post("/users" , ctx ->{
            //TODO get info of user from form and send to service
        });

        app.get("/", ctx ->{
            String username = ctx.cookieStore("uid");
            List<ResponseT<ShopInfo>> responses = services.GetShopsInfo(username, new SearchShopFilter());
            List<ShopInfo> shops = new ArrayList<>();
            for(ResponseT<ShopInfo> response : responses){
                if(response.isErrorOccurred()){
                    ctx.status(503);
                    ctx.render("errorPage.jte",Collections.singletonMap("errorMessage", response.errorMessage));
                }
                shops.add(response.getValue());
            }
            PresentationUser user = getUser(username,ctx);
            ctx.render("index.jte", Map.of("shops", shops, "user",user));
        });

        app.get("users/login", ctx->{
            String username = ctx.cookieStore("uid");
            PresentationUser user = getUser(username,ctx);
            ctx.render("login.jte", Map.of("user",user));
        });

        app.post("/users/login", ctx->{
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            ResponseT<User> response = services.Login(username,password);
            if(response.isErrorOccurred()){
                int errorCode = 401;
                ctx.status(errorCode);
                ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage,"status",errorCode));
            }
            else {
                ctx.cookieStore("uid", response.getValue().getId());
                ctx.redirect("/");
            }
        });

        app.ws("/users/new", ws ->{
            ws.onConnect(ctx->{

            });
            ws.onMessage(ctx->{
                PresentationUser requestedUser = ctx.messageAsClass(PresentationUser.class);
                Response response = services.Register(requestedUser.getUsername(), requestedUser.getPassword());
                ctx.send(response);
            });
        });

        app.get("/users/new", ctx -> {
            String username = ctx.cookieStore("uid");
            PresentationUser user = getUser(username,ctx);
            ctx.render("register.jte", Map.of("r_user",user));
        });

        app.post("/users/{id}/logout", ctx->
        {
           String username = ctx.pathParam("id");
           ResponseT<User> guest = services.Logout(username);
           if(guest.isErrorOccurred()){
               ctx.status(418);
               ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", guest.errorMessage));
           }
           ctx.cookieStore("uid", guest.getValue().getId());
            ctx.redirect("/");
        });

    }

    public static PresentationUser getUser(String username, Context ctx){
        ResponseT<User> user = services.GetUser(username);
        if(user.isErrorOccurred()){
            ctx.status(503);
            ctx.render("errorPage.jte",Collections.singletonMap("errorMessage", user.errorMessage));
        }
        return new PresentationUser(user.getValue());
    }

}
