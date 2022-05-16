package Presentation.Controllers;

import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.ResponseList;
import domain.ResponseT;
import domain.shop.Product;
import domain.shop.ServiceProduct;
import domain.shop.Shop;
import domain.shop.ShopManagersPermissions;
import domain.user.filter.SearchProductFilter;
import domain.user.filter.SearchShopFilter;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopController {

    private final Services services;
    private final UserController userController;

    public ShopController(UserController userController){
        services = Services.getInstance();
        this.userController = userController;
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
            ResponseList<Shop> shops = services.GetShopsInfo(username, new SearchShopFilter());
            if(shops.isErrorOccurred()){
                ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            }
            ctx.redirect("/");
        }
    }

    public void renderShop(Context ctx) {
        int shopID = Integer.parseInt(ctx.pathParam("shopID"));
        PresentationUser user = userController.getUser(ctx);
        ResponseT<Shop> response = services.GetShop(shopID);
        if (response.isErrorOccurred()) {
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        PresentationShop shop = new PresentationShop(response.getValue());
        ResponseList<Product> products= services.GetProductInfoInShop(user.getUsername(), shopID,new SearchProductFilter());
        if(products.isErrorOccurred()) {
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }


        shop.products = products.getValue().stream().map(PresentationProduct::new).collect(Collectors.toList());
        ResponseList<ShopManagersPermissions> permission = services.CheckPermissionsForManager(user.getUsername(), shopID);
        if(permission.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        user.setPermission(shopID, permission.getValue());
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
        PresentationProduct product = new PresentationProduct(response.getValue());
        user.setPermission(shopID, permission.getValue());
        ctx.render("product.jte", Map.of("user", user, "product", product, "shopId", shopID));
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
        PresentationUser user = userController.getUser(ctx);
        int shopID = ctx.pathParamAsClass("shopID",Integer.class ).get();
        int serialNumber = ctx.formParamAsClass("serialNumber", Integer.class).get();
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");
        String category = ctx.formParam("category");
        double price = ctx.formParamAsClass("price", Double.class).get();
        int quantity = ctx.formParamAsClass("amount", Integer.class).get();
        //TODO change all follow method to add serial number
        ResponseT<Product> response = services.AddProductToShopInventory(serialNumber,name,description, category, price, quantity, user.getUsername(), shopID);
        if(response.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
            return;
        }
        ctx.redirect(String.format("/shops/%d",shopID));
    }

    public void renderAddProductPage(Context context) {
        PresentationUser user = userController.getUser(context);
        int shopID = context.pathParamAsClass("shopID", Integer.class).get();
        ResponseList<ShopManagersPermissions> permissions = services.CheckPermissionsForManager(user.getUsername(), shopID);
        if(permissions.isErrorOccurred()){
            context.render("errorPage.jte",Map.of("status", 400, "errorMessage", "You have no permission to add products"));
        }
        user.setPermission(shopID, permissions.getValue());
        if(user.hasInventoryPermission(shopID)){
            context.render("addProduct.jte", Map.of("user", user, "shopId", shopID));
            return;
        }
        context.render("errorPage.jte",Map.of("status", 400, "errorMessage", "You have no permission to add products"));
    }
}
