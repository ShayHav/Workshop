package Presentation;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Response;
import domain.ResponseT;
import domain.market.PaymentServiceImp;
import domain.market.SupplyServiceImp;
import domain.shop.Shop;
import domain.shop.ShopInfo;
import domain.user.User;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;


import java.util.*;

public class Main {

    static final int port = 8080;
    static Services services;

    public static void main(String[] args) {
        services = new Services();
        services.StartMarket(new PaymentServiceImp(),new SupplyServiceImp(), "Admin","Admin");
        Javalin app =Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.post("/users" , ctx ->{
            //TODO get info of user from form and send to service
        });

        app.get("/", ctx ->{
            String username = ctx.cookie("uid");
            List<ResponseT<ShopInfo>> responses = services.GetShopsInfo(username, null);
            List<ShopInfo> shops = new ArrayList<>();
            for(ResponseT<ShopInfo> response : responses){
                if(response.isErrorOccurred()){
                    ctx.status(503);
                    ctx.render("errorPage.jte",Collections.singletonMap("error", response.errorMessage));
                }
                shops.add(response.getValue());
            }
            ctx.render("index.jte", Map.of("shops", shops));
        });

        app.post("/users/login", ctx->{
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            ResponseT<User> response = services.Login(username,password);
            if(response.isErrorOccurred()){
                ctx.status(418);
                ctx.render("errorPage.jte", Collections.singletonMap("error", response.errorMessage));
            }
            ctx.cookie("uid", response.getValue().getId());
            ctx.redirect("/");
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

        app.before(ctx->{
            if(ctx.cookie("uid") != null){
                ResponseT<User> response = services.EnterMarket();
                if(response.isErrorOccurred()){
                    ctx.status(503);
                    ctx.render("error.jte", Collections.singletonMap("error" ,response.errorMessage));
                }
                else {
                    PresentationUser user = new PresentationUser(response.getValue());
                    ctx.cookie("uid", user.getUsername());
                }
            }
        });

    }

}
