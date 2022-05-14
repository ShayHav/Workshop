package Presentation.Controllers;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.ResponseList;
import domain.ResponseT;
import domain.shop.Product;
import domain.shop.ServiceProduct;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import io.javalin.http.Context;

import java.util.Map;

public class ShopController {

    private final Services services;
    private UserController userController;

    public ShopController(){
        services = Services.getInstance();
    }

    public void createShop(Context ctx) {
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
    }

    public void renderShop(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("shopID"));
        PresentationUser user = userController.getUser(ctx);
        ResponseT<Shop> response = services.GetShop(id);
        if (response.isErrorOccurred()) {
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        PresentationShop shop = new PresentationShop(response.getValue());
        ctx.render("shop.jte", Map.of("user", user, "shop", shop));
    }

    public void renderProductPage(Context ctx) {
        int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        PresentationUser user = userController.getUser(ctx);
        ResponseT<Product> response =  services.getProduct(user.getUsername(),shopID, serialNumber);
        if(response.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        ResponseList<ShopManagersPermissions> permission = services.CheckPermissionsForManager(user.getUsername(), shopID);
        if(permission.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        user.setPermission(permission.getValue());
        ctx.render("product.jte", Map.of("user", user, "product", response.getValue(), "shopId", shopID));
    }

    public void editProduct(Context ctx) {
        String username = ctx.cookieStore("uid");
        int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");
        String category = ctx.formParam("category");
        int amount = ctx.formParamAsClass("amount",Integer.class).get();
        double price = ctx.formParamAsClass("price", Double.class).get();
        ServiceProduct product = new ServiceProduct(serialNumber, name, description, category, price, amount);
        ResponseT<Product> response = services.ChangeProduct(username, product ,shopID);
        if(response.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        ctx.redirect(String.format("shops/%d/%d", shopID, serialNumber));
    }

    public void addProduct(Context ctx) {
    }
}
