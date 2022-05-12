package Presentation;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.ResponseT;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {

    static final int port = 8080;
    static Services services;

    public static void main(String[] args) {
        services = new Services();
        services.StartMarket(null,null, "","");
        Javalin app =Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.post("/users" , ctx ->{
            //TODO get info of user from form and send to service
        });

        app.get("/", ctx ->{

            List<PresentationShop> shops = services.GetShopsInfo();
            ctx.render("index.jte", );
        });

        app.post("/users/login", ctx->{
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");
            ResponseT<PresentationUser> user = services.Login(username,password);
            if(user.isErrorOccurred()){
                ctx.redirect("errorPage", Collections.singletonMap("error", user.errorMessage));
            }
            ctx.cookie("uid", username);
            ctx.redirect("/");
        });

        app.ws("/users/new", ws ->{
            ws.onConnect(ctx->{

            });
            ws.onMessage(ctx->{
                PresentationUser requestedUser = ctx.messageAsClass(PresentationUser.class);
                ResponseT<PresentationUser> response = services.Register(requestedUser.getUsername(), requestedUser.getPassword());
                ctx.send(response);
            });
        });

        app.before(ctx->{
            if(ctx.cookie("uid") != null){
                ResponseT<PresentationUser> response = services.EnterMarket();
                if(response.isErrorOccurred()){
                    ctx.status(503);
                    ctx.render("error.jte", Collections.singletonMap("error" ,response.errorMessage));
                }
                else {
                    PresentationUser user = response.getValue();
                    ctx.cookie("uid", user.getUsername());
                }
            }
        });

    }

}
