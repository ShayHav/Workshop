package Presentation;

import Presentation.Model.PresentationShop;
import Service.Services;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

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

            List<PresentationShop> shops = services.getShops();
            ctx.render("index.jte", );
        });

        app.ws("/user/new", ws ->{
            ws.onMessage(ctx->{
                String username = ctx.message();
            });
        });

        app.before(ctx->{
            if(ctx.cookie("uid") != null){
                //enter market from servic;
                ctx.cookie("uid", )
            }
        });

    }

}
