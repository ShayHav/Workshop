package Presentation.Controllers;

import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Response;
import domain.ResponseT;
import domain.user.User;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserController {

    private final Map<String, PresentationUser> requestedUser;
    private final Services services;

    public UserController(){
        requestedUser = new HashMap<>();
        services = Services.getInstance();
    }


    public void login(Context ctx) {
        String username = ctx.formParam("username");
        String password = ctx.formParam("password");
        ResponseT<User> response = services.Login(username, password);
        if (response.isErrorOccurred()) {
            int errorCode = 401;
            ctx.status(errorCode);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", errorCode));
        } else {
            PresentationUser user = new PresentationUser(response.getValue());
            synchronized (requestedUser){
                requestedUser.put(user.getUsername(), user);
            }
            ctx.cookieStore("uid",user.getUsername());
            ctx.redirect("/");
        }
    }

    public void renderLogin(Context ctx){
        PresentationUser user = getUser(ctx);
        ctx.render("login.jte", Map.of("user", user));
    }

    public PresentationUser getUser(Context ctx) {
        String username = ctx.cookieStore("uid");
        synchronized (requestedUser){
            if(requestedUser.containsKey(username))
                return requestedUser.get(username);
        }
        ResponseT<User> response = services.GetUser(username);
        if (response.isErrorOccurred()) {
            ctx.status(503);
            ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            return null;
        }
        PresentationUser user = new PresentationUser(response.getValue());
        synchronized (requestedUser){
            requestedUser.put(user.getUsername(), user);
        }
        return user;
    }

    public void register(WsConfig ws) {
        ws.onConnect(wsConnectContext -> {

        });
        ws.onMessage(ctx -> {
            PresentationUser requestedUser = ctx.messageAsClass(PresentationUser.class);
            Response response = services.Register(requestedUser.getUsername(), requestedUser.getPassword());
            ctx.send(response);
        });
    }

    public void renderRegister(Context ctx) {
        PresentationUser user = getUser(ctx);
        ctx.render("register.jte", Map.of("r_user", user));
    }

    public void logout(Context ctx) {
        String username = ctx.pathParam("id");
        ResponseT<User> guest = services.Logout(username);
        if (guest.isErrorOccurred()) {
            ctx.status(418);
            ctx.render("errorPage.jte", Map.of("errorMessage", guest.errorMessage, "status", 418));
        }
        ctx.cookieStore("uid", guest.getValue().getUserName());
        ctx.redirect("/");
    }

    public void validateUser(Context ctx){
        if (ctx.cookieStore("uid") == null){
            ResponseT<User> response = services.EnterMarket();
            if (response.isErrorOccurred()) {
                ctx.status(503);
                ctx.render("errorPage.jte", Collections.singletonMap("errorMessage", response.errorMessage));
            } else {
                PresentationUser user = new PresentationUser(response.getValue());
                ctx.cookieStore("uid", user.getUsername());
            }
        }
    }
}
