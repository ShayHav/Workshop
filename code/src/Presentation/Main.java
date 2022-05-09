package Presentation;

import Service.Services;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class Main {

    static final int port = 8080;


    public static void main(String[] args) {
        Javalin app =Javalin.create(JavalinConfig::enableWebjars).start(port);

        app.post("/users" , ctx ->{
            //TODO get info of user from form and send to service
        });

        app.get("/", ctx ->{
            Services services = new Services();
            List<Shop> shops = services.getShops();
            ctx.render("index.jte", );
        });

        app.before(ctx->{
            if(ctx.cookie("uid") != null){
                //enter market from servic;
                ctx.cookie("uid", )
            }
        })

    }

}
