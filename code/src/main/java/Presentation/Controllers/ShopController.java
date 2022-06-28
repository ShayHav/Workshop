package Presentation.Controllers;

import Presentation.Model.Messages.AppointMangerMessage;
import Presentation.Model.Messages.AppointOwnerMessage;
import Presentation.Model.Messages.EditShopMessage;
import Presentation.Model.PresentationOrder;
import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Service.Services;
import domain.Exceptions.BlankDataExc;
import domain.Exceptions.InvalidParamException;
import domain.Responses.Response;
import domain.Responses.ResponseList;
import domain.Responses.ResponseT;
import domain.shop.*;
import domain.shop.predicate.PRPredType;
import domain.shop.predicate.ToBuildDiscountPredicate;
import domain.shop.predicate.ToBuildPRPredicateFrom;
import domain.user.filter.SearchOrderFilter;
import domain.user.filter.SearchProductFilter;
import domain.user.filter.SearchShopFilter;
import io.javalin.http.Context;
import io.javalin.websocket.WsConfig;

import java.time.format.DateTimeFormatter;
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
        String description = ctx.formParam("description");
        String username = ctx.cookieStore("uid");
        ResponseT<Shop> response = services.CreateShop(description,username, shopName);
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

        if(!shop.isOpen() && !shop.isFounder(user)){
            ctx.status(403);
            String errorMessage = "You have no privilege to access this page";
            ctx.render("errorPage.jte", Map.of("errorMessage", errorMessage, "status", 403));
            return;
        }


        SearchProductFilter filter = getProductFilter(ctx);
        ResponseList<Product> products= services.GetProductInfoInShop(user.getUsername(), shopID,filter);
        if(products.isErrorOccurred()) {
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }

        shop.products = PresentationProduct.convertProduct(products.getValue(), shopID);
        ResponseList<ShopManagersPermissions> permission = services.CheckPermissionsForManager(user.getUsername(), shopID);
        if(permission.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }
        else {
            user.setPermission(shopID, permission.getValue());

            Double minPrice = filter.getMinPrice();
            Double maxPrice = filter.getMaxPrice();
            String category = filter.getCategory() == null? "" : filter.getCategory();

            ctx.render("shop.jte", Map.of("user", user, "shop", shop, "minPrice", minPrice, "maxPrice", maxPrice, "category", category));
        }
    }

    public void renderProductPage(Context ctx) {
        int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        PresentationUser user = userController.getUser(ctx);
        ResponseT<Product> response =  services.getProduct(user.getUsername(),shopID, serialNumber);
        if(response.isErrorOccurred()){
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        ResponseList<ShopManagersPermissions> permission = services.CheckPermissionsForManager(user.getUsername(), shopID);
        if(permission.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        PresentationProduct product = new PresentationProduct(response.getValue(), shopID);
        user.setPermission(shopID, permission.getValue());
        ctx.render("product.jte", Map.of("user", user, "product", product, "shopId", shopID));
    }

    public void renderEditProduct(Context ctx){
        PresentationUser user = userController.getUser(ctx);
        int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        ResponseT<Product> response = services.getProduct(user.getUsername(),shopID,serialNumber);
        if(response.isErrorOccurred()){
            ctx.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        PresentationProduct product = new PresentationProduct(response.getValue(),shopID);
        if(user.hasInventoryPermission(shopID)) {
            ctx.render("editProduct.jte", Map.of("user", user, "shopID", shopID, "product", product));
            return;
        }
        ctx.status(400).render("errorPage.jte", Map.of("errorMessage", "you don't have permission to view this page", "status", 400));
    }

    public void editProduct(Context ctx) {
        String username = ctx.cookieStore("uid");
        int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = ctx.pathParamAsClass("serialNumber", Integer.class).get();
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");
        String category = ctx.formParam("category");
        String path = "/shops/" + shopID + "/" + serialNumber;
        int amount = ctx.formParamAsClass("amount",Integer.class).get();
        double price = ctx.formParamAsClass("price", Double.class).get();
        ServiceProduct product = new ServiceProduct(serialNumber, name, description, category, price, amount);
        ResponseT<Product> response = services.ChangeProduct(username, product ,shopID);
        if(response.isErrorOccurred()){
            ctx.status(400);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
        }
        else {
            ctx.redirect(path);
        }
    }

    public void addProduct(Context ctx) throws BlankDataExc {
        PresentationUser user = userController.getUser(ctx);
        int shopID = ctx.pathParamAsClass("shopID",Integer.class ).get();
        int serialNumber = ctx.formParamAsClass("serialNumber", Integer.class).get();
        String name = ctx.formParam("name");
        String description = ctx.formParam("description");
        String category = ctx.formParam("category");
        double price = ctx.formParamAsClass("price", Double.class).get();
        int quantity = ctx.formParamAsClass("amount", Integer.class).get();
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

    public void removeProduct(Context context) {
        PresentationUser user = userController.getUser(context);
        int shopID = context.pathParamAsClass("shopID", Integer.class).get();
        int serialNumber = context.pathParamAsClass("serialNumber", Integer.class).get();
        Response response = services.RemoveProductFromShopInventory(serialNumber, user.getUsername(), shopID);
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));

        }
        else {
            String path = "/shops/" + shopID;
            context.redirect(path);
        }
    }

    public void closeShop(Context context) {
        int shopId = context.pathParamAsClass("shopID", Integer.class).get();
        String username = context.cookieStore("uid");
        Response response = services.closeShop(shopId, username);
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
        }
        else{
            context.redirect("/shops/"+shopId);
        }
    }

    public void reopenShop(Context context){
        int shopId = context.pathParamAsClass("shopID", Integer.class).get();
        String username = context.cookieStore("uid");
        Response response = services.reopenShop(shopId, username);
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
        }
        else{
            context.redirect("/shops/"+shopId);
        }
    }

    public void renderEditShop(Context context){
        int shopId = context.pathParamAsClass("shopID", Integer.class).get();
        PresentationUser user = userController.getUser(context);
        ResponseT<Shop> response = services.GetShop(shopId);
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
            return;
        }
        PresentationShop shop = new PresentationShop(response.getValue());
        if(user.hasRoleInShop(shopId)) {
            context.render("editShop.jte", Map.of("user", user, "shop", shop));
            return;
        }
        context.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", "you don't have permission to view this page"));
    }

    public void editShop(WsConfig wsConfig) {
        wsConfig.onConnect(ctx -> {});
        wsConfig.onMessage(ctx->{
            EditShopMessage message = ctx.messageAsClass(EditShopMessage.class);
            int shopID = ctx.pathParamAsClass("shopID", Integer.class).get();
            switch (message.type) {
                case "removeManager" -> {
                    Response r = services.removeManager(shopID, message.requestingUser, message.subject);
                    ctx.send(r);
                }
                case "addManager" -> {
                    Response response = services.AppointNewShopManager(shopID, message.subject, message.getRequestingUser());
                    AppointMangerMessage returnMessage = new AppointMangerMessage(response.errorMessage, message.subject);
                    ctx.send(returnMessage);
                }
                case "addOwner" -> {
                    Response response = services.AppointNewShopOwner(shopID, message.subject, message.getRequestingUser());
                    AppointOwnerMessage returnMessage = new AppointOwnerMessage(response.errorMessage, message.subject);
                    ctx.send(returnMessage);
                }
                case "removeOwner" -> {
                    Response response = services.DismissalOwnerByOwner(message.requestingUser, message.subject, shopID);
                    ctx.send(response);
                }
            }
        });
    }

    public void renderOrderHistory(Context ctx) {
        PresentationUser user= userController.getUser(ctx);
        int shopID = ctx.pathParamAsClass("shopID",Integer.class).get();
        SearchOrderFilter filter = userController.getOrderFilter(ctx);

        ResponseList<Order> response = services.RequestInformationOfShopsSalesHistory(shopID,filter, user.getUsername());
        if(response.isErrorOccurred()){
            ctx.status(400).render("errorPage.jte", Map.of("status", 400, "errorMessage", response.errorMessage));
            return;
        }
        List<PresentationOrder> orders = response.getValue().stream().map(PresentationOrder::new).collect(Collectors.toList());

        Double minPrice = filter.getMinPrice();
        Double maxPrice = filter.getMaxPrice();
        String minDate = filter.getMinDate() == null? "" : filter.getMinDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String maxDate = filter.getMaxDate() == null? "" : filter.getMaxDate().format(DateTimeFormatter.ISO_LOCAL_DATE);

        ctx.render("shopOrderHistory.jte", Map.of("user", user, "orders",orders, "shopID", shopID, "minPrice", minPrice, "maxPrice", maxPrice, "minDate", minDate, "maxDate", maxDate));
    }

    public void renderHomepage(Context ctx){
        String username = ctx.cookieStore("uid");
        SearchShopFilter filter = userController.getShopFilter(ctx);
        String name = filter.getName() == null? "": filter.getName();

        ResponseList<Shop> response = Services.getInstance().GetShopsInfo(username, filter);
        if (response.isErrorOccurred()) {
            ctx.status(503);
            ctx.render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 503));
        }
        List<PresentationShop> shops = response.getValue().stream().map(PresentationShop::new).collect(Collectors.toList());
        PresentationUser user = userController.getUser(ctx);
        ctx.render("index.jte", Map.of("shops", shops, "user", user, "filteredName", name));
    }

    public SearchProductFilter getProductFilter(Context ctx){
        Double minPrice = ctx.queryParamAsClass("minPrice", Double.class).getOrDefault(null);
        Double maxPrice = ctx.queryParamAsClass("maxPrice", Double.class).getOrDefault(null);
        String category = ctx.queryParam("category");
        category = category == null || category.isEmpty()? null : category;

        return new SearchProductFilter(minPrice, maxPrice,null, null, category);
    }

    public void addPurchaseRule(Context context) {
        String ruleType = context.formParam("ruleType");
        if (ruleType == null){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", "ruleType is null", "status", 400));
            return;
        }
        Response response;
        switch (ruleType){
            case "ProhibitPurchaseHour" -> response = addProhibitedTimeRule(context);
            case "minimumQuantity" -> response = addMinimumRule(context);
            case "maximusQuantity" -> response = addMaximumRule(context);
            default -> response = new Response("unsupported option in rule type");
        }
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        int shopID = context.pathParamAsClass("shopID", Integer.class).get();
        context.redirect("/shops/"+ shopID+ "/edit");

    }


    private Response addProhibitedTimeRule(Context context){
        String ruleBase = context.formParam("ruleBase");
        if(ruleBase == null){
            return new Response("the basis of the rule is null");
        }
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        double fromHour = context.formParamAsClass("fromHour", int.class).get();
        double endHour = context.formParamAsClass("toHour", int.class).get();
        ToBuildPRPredicateFrom builder = new ToBuildPRPredicateFrom(fromHour, endHour, PRPredType.TimeConstraint);
        switch (ruleBase){
            case "category" -> {
                String category = context.formParam("productOrCategory");
                return services.addCategoryPurchasePolicy(shopID, category, builder);
            }
            case "allProduct" -> {
                return services.addShopAllProductsPurchasePolicy(shopID,builder);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategory", int.class).get();
                return services.addProductPurchasePolicy(shopID, serialNumber, builder);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }


    private Response addMaximumRule(Context context){
        String ruleBase = context.formParam("ruleBase");
        if(ruleBase == null){
            return new Response("the basis of the rule is null");
        }
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        int maximumNumber = context.formParamAsClass("maximum", int.class).get();
        int targetProduct = context.formParamAsClass("targetProduct", int.class).get();
        ResponseT<Product> response = services.getProduct(username, shopID, targetProduct);
        if(response.isErrorOccurred()){
            return response;
        }
        ToBuildPRPredicateFrom builder = new ToBuildPRPredicateFrom(maximumNumber,  targetProduct, response.getValue().getName() , PRPredType.MaximumAmount);;
        switch (ruleBase){
            case "category" -> {
                String category = context.formParam("productOrCategory");
                return services.addCategoryPurchasePolicy(shopID, category, builder);
            }
            case "allProduct" -> {
                return services.addShopAllProductsPurchasePolicy(shopID,builder);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategory", int.class).get();;
                return services.addProductPurchasePolicy(shopID, serialNumber, builder);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }

    private Response addMinimumRule(Context context){
        String ruleBase = context.formParam("ruleBase");
        if(ruleBase == null){
            return new Response("the basis of the rule is null");
        }
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        int minimumNumber = context.formParamAsClass("minimum", int.class).get();
        int targetProduct = context.formParamAsClass("targetProduct", int.class).get();

        String username = context.cookieStore("uid");
        ResponseT<Product> response = services.getProduct(username, shopID, targetProduct);
        if(response.isErrorOccurred()){
            return response;
        }

        ToBuildPRPredicateFrom builder = new ToBuildPRPredicateFrom(minimumNumber, targetProduct, response.getValue().getName(), PRPredType.MinimumAmount);

        switch (ruleBase){
            case "category" -> {
                String category = context.formParam("productOrCategory");
                return services.addCategoryPurchasePolicy(shopID, category, builder);
            }
            case "allProduct" -> {
                return services.addShopAllProductsPurchasePolicy(shopID,builder);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategory", int.class).get();
                return services.addProductPurchasePolicy(shopID, serialNumber, builder);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }

    public void combineRules(Context context) {
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        List<Integer> rules = context.formParams("rule").stream().map(Integer::parseInt).collect(Collectors.toList());
        String type = context.formParam("combineRuleType");
        if(type == null){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", "message type cannot be null", "status", 400));
            return;
        }
        Response response;
        switch (type){
            case "and" -> response = services.addAndPurchaseRule(rules.get(0), rules.get(1), shopID);
            case "or" -> response = services.addOrPurchaseRule(rules.get(0), rules.get(1), shopID);
            default -> response = new Response("Not supported operation");
        }
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        context.redirect("/shops/"+ shopID+"/edit");
    }

    public void addDiscount(Context context) {
        String ruleType = context.formParam("discountType");
        if (ruleType == null){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", "discount type is null", "status", 400));
            return;
        }
        Response response;
        switch (ruleType){
            case "regularDiscount" -> response = addRegularDiscount(context);
            case "bundleDiscount" -> response = addBundleDiscount(context);
            case "productConditionalDiscount" -> response = addProductConditionalDiscount(context);
            case "basketConditionalDiscount" -> response = addBasketConditionalDiscount(context);
            default -> response = new Response("unsupported option in discount type");
        }
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        int shopID = context.pathParamAsClass("shopID", Integer.class).get();
        context.redirect("/shops/"+ shopID+ "/edit");

    }

    private Response addProductConditionalDiscount(Context context) {
        String discountBase = context.formParam("discountBase");
        if(discountBase == null){
            return new Response("the basis of the rule is null");
        }
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        double percentage = context.formParamAsClass("percentage", Double.class).get();
        int productIDCond = context.formParamAsClass("productIDCond", int.class).get();
        int amountToBuy = context.formParamAsClass("amountToBuyFromProduct", int.class).get();

        ResponseT<Product> response = services.getProduct(username,shopID,productIDCond);
        if(response.isErrorOccurred()){
            return response;
        }

        String productName = response.getValue().getName();
        ToBuildDiscountPredicate predicate;
        try {
            predicate = new ToBuildDiscountPredicate(productIDCond, productName, amountToBuy);
        }catch(InvalidParamException e){
            return new Response(e.getMessage());
        }


        switch (discountBase){
            case "category" -> {
                String category = context.formParam("productOrCategoryDiscount");
                return services.addConditionalCategoryDiscount(shopID,category,percentage,predicate);
            }
            case "allProduct" -> {
                return services.addConditionalShopAllProductsDiscount( shopID,percentage,predicate);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategoryDiscount", int.class).get();
                response = services.getProduct(username, shopID, serialNumber);
                if(response.isErrorOccurred()){
                    return response;
                }
                return services.addConditionalProductDiscount( shopID,serialNumber,percentage,predicate);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }

    private Response addBasketConditionalDiscount(Context context) {
        String discountBase = context.formParam("discountBase");
        if(discountBase == null){
            return new Response("the basis of the rule is null");
        }
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        double percentage = context.formParamAsClass("percentage", Double.class).get();
        double basketPrice = context.formParamAsClass("basketPrice", double.class).get();


        ToBuildDiscountPredicate predicate;
        try {
            predicate = new ToBuildDiscountPredicate(basketPrice);
        }catch(InvalidParamException e){
            return new Response(e.getMessage());
        }


        switch (discountBase){
            case "category" -> {
                String category = context.formParam("productOrCategoryDiscount");
                return services.addConditionalCategoryDiscount(shopID,category,percentage,predicate);
            }
            case "allProduct" -> {
                return services.addConditionalShopAllProductsDiscount(shopID,percentage,predicate);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategoryDiscount", int.class).get();
                ResponseT<Product> response = services.getProduct(username, shopID, serialNumber);
                if(response.isErrorOccurred()){
                    return response;
                }
                return services.addConditionalProductDiscount(shopID,serialNumber,percentage,predicate);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }

    private Response addRegularDiscount(Context context) {
        String discountBase = context.formParam("discountBase");
        if(discountBase == null){
            return new Response("the basis of the rule is null");
        }
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        Double percentage = context.formParamAsClass("percentage", Double.class).get();

        switch (discountBase){
            case "category" -> {
                String category = context.formParam("productOrCategoryDiscount");
                return services.addCategoryDiscount( shopID, category, percentage);
            }
            case "allProduct" -> {
                return services.addShopAllProductsDiscount( shopID,percentage);
            }
            case "product" -> {
                int serialNumber = context.formParamAsClass("productOrCategoryDiscount", int.class).get();
                ResponseT<Product> response = services.getProduct(username, shopID, serialNumber);
                if(response.isErrorOccurred()){
                    return response;
                }
                return services.addProductDiscount( shopID, serialNumber, percentage);
            }
            default -> {
                return new Response("not a legal ruleBase");
            }
        }
    }

    private Response addBundleDiscount(Context context) {
        return new Response("unsupported operation yet");
    }



    public void deleteRule(Context context) {
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        int ruleId = context.formParamAsClass("deleteRuleId", int.class).get();
        String username = context.cookieStore("uid");
        Response response = services.removePR(ruleId, shopID);
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        context.redirect("/shops/"+ shopID+ "/edit");
    }

    public void deleteDiscount(Context context) {
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        int discountID = context.formParamAsClass("discountIDToDelete", int.class).get();
        String username = context.cookieStore("uid");
        Response response = services.removeDiscount(username,discountID,shopID);

        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        context.redirect("/shops/"+ shopID+ "/edit");
    }

    public void composeDiscounts(Context context) {
        String username = context.cookieStore("uid");
        int shopID = context.pathParamAsClass("shopID", int.class).get();
        List<Integer> discounts = context.formParams("discount").stream().map(Integer::parseInt).collect(Collectors.toList());
        String type = context.formParam("discountComposeOperator");

        if(type == null){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", "message type cannot be null", "status", 400));
            return;
        }
        Response response;
        switch (type){
            case "and" -> response = services.addAndDiscount(username, discounts.get(0), discounts.get(1), shopID);
            case "or" -> response = services.addOrDiscount(username, discounts.get(0), discounts.get(1), shopID);
            case "xor" -> response = services.addXorDiscount(username, discounts.get(0), discounts.get(1), shopID);
            default -> response = new Response("Not supported operation");
        }
        if(response.isErrorOccurred()){
            context.status(400).render("errorPage.jte", Map.of("errorMessage", response.errorMessage, "status", 400));
            return;
        }
        context.redirect("/shops/"+ shopID+"/edit");
    }
}
