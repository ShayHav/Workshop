package Service;


import Presentation.Model.PresentationProduct;
import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import Testing_System.Result;
import domain.Exceptions.*;
import domain.Response;
import domain.ResponseT;
import domain.market.*;
import domain.shop.*;

import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filters.Filter;
import domain.user.filters.SearchOfficialsFilter;
import domain.user.filters.SearchOrderFilter;
import domain.user.filters.SearchUserFilter;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Services {
    MarketSystem marketSystem = MarketSystem.getInstance();

    public Services(){
    }

    /*Result<#t | # f,  return value>
     * t - success
     * f - failed
     *
     */
    //General Guest-Visitor
    //Make:nitay InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification

    /**
     * User logged in to the system
     * @param username - user identifier
     * @param pw - given password
     * @return - Response object
     */
    public ResponseT<PresentationUser> Login(String username, String pw) {
        ResponseT<PresentationUser> output;
        try {
            User b = marketSystem.logIn(username, pw);
            output = new ResponseT<>(new PresentationUser(b));
            return output;
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new ResponseT<>(null,invalidSequenceOperationsExc.getLocalizedMessage());
        } catch (IncorrectIdentification incorrectIdentification) {
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        } catch (InvalidAuthorizationException invalidAuthorizationException) {
            return new ResponseT<>(null, invalidAuthorizationException.actualAuthorization);
        }
    }
    //Make:nitay

    /**
     * User registered to the market
     * @param username - given user identifier
     * @param pw - given user password
     * @return
     */
    public Response Register(String username, String pw) {
        try {
            if(marketSystem.register(username, pw))
                return new Response();
        } catch (BlankDataExc blankDataExc) {
            return new Response(blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
        return new Response();
    }
    //Make:nitay
    /**
     * User enters the system
     * @return response object
     */
    public ResponseT<PresentationUser> EnterMarket() { //
        User EnterMarket = marketSystem.EnterMarket();
        if (EnterMarket != null)
            return new ResponseT<>(new PresentationUser(EnterMarket));
        else return new ResponseT<>();
    }
    //Make:nitay   IncorrectIdentification, InvalidSequenceOperationsExc
    //TODO: need to check

    /**
     * User closes the system
     * @return - null
     */
    public Response LeaveMarket() {
        try {
            User output = marketSystem.LeaveMarket("");
            if (output != null)
                return new Response();
            else return null;
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc incorrectIdentification){
            return new Response(incorrectIdentification.getLocalizedMessage());
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(blankDataExc.getMessage());
        }
    }


    //General Member-Visitor

    /**
     * User logs off
     * @param username - user identifier
     * @return Response object
     */
    public ResponseT<PresentationUser> Logout(String username) {
        User output;
        try {
            output = marketSystem.logout(username);
            return new ResponseT<>(new PresentationUser(output));
        } catch (BlankDataExc | InvalidSequenceOperationsExc | IncorrectIdentification blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay

    /***
     * Create a store object to represent the relevant store in the app
     * @param description -  description of the created shop
     * @param username - user identifier
     * @param shopName - shop name
     * @return Response object
     */
    public ResponseT<PresentationShop> CreateShop(String description ,String username, String shopName) {
        try {
            Shop output = marketSystem.createShop(description ,shopName, null, null, username);
            if (output != null)
                return new ResponseT<>(new PresentationShop(output));
            else return new ResponseT<>();
        } catch (BlankDataExc | IncorrectIdentification blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        }
    }

    //TODO: impl on later version
    //System
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg)
    {
        return null;
    }

    //shay  //TODO: only for test?
    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products)
    {
        try {
            MarketSystem m = MarketSystem.getInstance();
            boolean ans = m.supply(ti, products);
            return new Result<>(ans, ans);
        }
        catch (BlankDataExc blankDataExc){
            return null;
        }
    }

    //shay  //TODO: only for test?
    public Result<Boolean, Boolean> Payment(TransactionInfo ti)
    {
        //MarketSystem m = MarketSystem.getInstance();
        boolean ans = marketSystem.pay(ti);
        return new Result<>(ans, ans);
    }

    /**
     * Boot of a user who started the system
     * @param payment - External device for payment
     * @param supply - External device for supply
     * @param userName - user identifier
     * @param password - user's given password
     * @return Response object
     */
    public Response StartMarket(PaymentService payment, SupplyService supply, String userName, String password) {
        try {
            boolean b = marketSystem.start(payment, supply, userName, password);
            return new Response();
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }

//    public Result<Boolean, String> AddSupplyService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, Boolean> RemoveSupplyService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, String> AddPaymentService(String path)
//    {
//        return null;
//    }
//
//    public Result<Boolean, String> RemovePaymentService(String path)
//    {
//        return null;
//    }

    //make: shahar
    //Guest-Visitor Shop options

    /**
     * Query for certain shops
     * @param userName - user identifier
     * @param filter - Identifying parameters
     * @return list of Response object
     */
    public List<ResponseT<PresentationShop>> GetShopsInfo(String userName, Filter<Shop> filter) {
        try {
            List<ResponseT<PresentationShop>> list = new LinkedList<>();
            List<Shop> shop = marketSystem.getInfoOfShops(userName, filter);
            for (Shop shopInfo1 : shop){
                list.add(new ResponseT<>(new PresentationShop(shopInfo1)));
            }
            return list;
        } catch (BlankDataExc blankDataExc) {
            return null;
        }
    }

    //Make:nitay

    /***
     * Query for certain products in the relevant store.
     * @param userName - user identifier
     * @param shopID - shop identifier
     * @param f
     * @return list of Response object
     */
    public List<ResponseT<PresentationProduct>> GetProductInfoInShop(String userName , int shopID, Filter<Product> f)
    {
        try {
            List<Product> GetProductInfoInShop = marketSystem.getInfoOfProductInShop(userName, shopID, f);
            List<ResponseT<PresentationProduct>> CreateShop = new LinkedList<>();
            if (GetProductInfoInShop == null || GetProductInfoInShop.size() == 0)
                return null;
            for (Product productInfo : GetProductInfoInShop) {
                CreateShop.add(new ResponseT<>(new PresentationProduct(new ServiceProduct(productInfo))));
            }
            return CreateShop;
        }
        catch (BlankDataExc blankDataExc){
            return null;
        }
    }//display information of a product?


    //make:shahar

    /**
     * Query for certain products by name in the relevant store
     * @param userName - user identifier
     * @param pName - shop's name
     * @param f
     * @return list of Response object
     */
    public List<ResponseT<PresentationProduct>> SearchProductByName(String userName ,String pName, Filter<Product> f) //done
    {
        try {
            List<ResponseT<PresentationProduct>> result = new LinkedList<>();
            List<Product> products = marketSystem.searchProductByName(userName, pName, f);
            for (Product productInfo : products) {
                result.add(new ResponseT<>(new PresentationProduct(new ServiceProduct(productInfo))));
            }
            return result;
        }
        catch (BlankDataExc blankDataExc){
            return null;
        }
    }

    //make:shahar

    /**
     * Query for certain products by keyword in the relevant store
     * @param userName
     * @param keyword
     * @param f
     * @return list Response object
     */
    public List<ResponseT<PresentationProduct>> SearchProductByKeyword(String userName ,String keyword, Filter<Product> f)
    {
        try {
            List<ResponseT<PresentationProduct>> result = new LinkedList<>();
            List<Product> products = marketSystem.searchProductByKeyword(userName, keyword, f);
            for (Product productInfo : products) {
                result.add(new ResponseT<>(new PresentationProduct(new ServiceProduct(productInfo))));
            }
            return result;
        }
        catch (BlankDataExc blankDataExc){
            return null;
        }
    }

    //make: shahar

    /**
     * Add a product to the user's shopping cart (member or guest)
     * @param userName - user identifier
     * @param shopID - shop identifier
     * @param productId - product identifier
     * @param amount
     * @return Response object
     */
    public Response AddToShoppingCart(String userName, int shopID,int productId, int amount)
    {
        try {
            boolean success = marketSystem.AddProductToCart(userName, shopID, productId, amount);
            if (success)
                return new Response();
            else
                return new Response();
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
        catch (ShopNotFoundException shopNotFoundException){
            return new Response(shopNotFoundException.getLocalizedMessage());
        }
    }

    //make:shahar

    /**
     * Edit a certain product quantity in the user's shopping cart
     * @param userName - user identifier
     * @param shopId - shop identifier
     * @param productId - product identifier
     * @param amount
     * @return Response object
     */
    public Response EditShoppingCart(String userName, int shopId, int productId, int amount)
    {
        try {
            boolean success = marketSystem.EditShoppingCart(userName, shopId, productId, amount);
            if (success)
                return new Response();
            else
                return new Response();
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }

    //make:shahar

    //TODO:

    /**
     * Remove a product from the user's shopping cart
     * @param userName - user identifier
     * @param shopId -  shop identifier
     * @param productId - product identifier
     * @return Response object
     * @throws InvalidSequenceOperationsExc
     */
    public Response RemoveFromShoppingCart(String userName, int shopId, int productId) throws InvalidSequenceOperationsExc {
        boolean success = marketSystem.removeProductFromCart(userName, shopId, productId);
        if(success)
            return new Response();
        else
            return new Response();
    }

    //Make nitay

    /**
     * Checkout
     * @param userName - user identifier
     * @param fullName - user full name
     * @param address - user address
     * @param phoneNumber - user phone number
     * @param cardNumber - user credit card number
     * @param expirationDate - user credit card expiration date
     * @return list of Response object
     */
    public List<Response> Checkout(String userName,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        List<Response> checkout = new LinkedList<>();
        try {
            List<String> temp = marketSystem.Checkout(userName, fullName, address, phoneNumber, cardNumber, expirationDate);
            if (temp.size() > 0) {
                for (String s : temp)
                    checkout.add(new Response(s));
                return checkout;
            }
            return null;
        } catch (IncorrectIdentification incorrectIdentification) {
            checkout.add(new Response(incorrectIdentification.getLocalizedMessage()));
            return checkout;
        } catch (BlankDataExc blankDataExc) {
            checkout.add(new Response(blankDataExc.getLocalizedMessage()));
            return checkout;
        }
    }


    //shay

    /**
     * Check If Product Available in the shop
     * @param p - relevant product object
     * @param shopID - shop identifier
     * @return Response object
     */
    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new ResponseT<>(null,snfe.getLocalizedMessage());
        }
        return new ResponseT<>(shop.getProduct(p.getId()) != null);
    }

    //Shay
    //done

    /**
     * Add a product to the shop inventory
     * @param pName - product name
     * @param pDis - product description
     * @param pCat - product category
     * @param price - product price
     * @param amount - amount of added product
     * @param username - active user identifier
     * @param shopID - shop identifier
     * @return Response object
     */
    public ResponseT<PresentationProduct> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username,int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new ResponseT<>(null,snfe.getLocalizedMessage());
        }
        Product p;
        try {
            p = shop.addListing(pName, pDis, pCat, price, amount, username);
        }catch (InvalidAuthorizationException iae){
            return new ResponseT<>(null,iae.getLocalizedMessage());
        }catch (InvalidProductInfoException ipie){
            //todo prints????
            return new ResponseT<>(null,ipie.getLocalizedMessage());
        }
        if(p == null){
            return null;
        }
        return new ResponseT<>(new PresentationProduct(new ServiceProduct(p)));
    }

    //shay

    /**
     *
     * @param username
     * @param p
     * @param shopID
     * @return Response object
     */
    public ResponseT<PresentationProduct> ChangeProduct(String username, Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new ResponseT<>(null,snfe.getLocalizedMessage());
        }
        int newAmount = ((ServiceProduct)p).getAmount();
        double newPrice = ((ServiceProduct)p).getPrice();
        Product changed;
        try{
            changed = shop.changeProductDetail(p.getId(),p.getName(),p.getDescription(), p.getCategory(),username, newAmount, newPrice);
        }catch (ProductNotFoundException pnfe){
            return new ResponseT<>(null,pnfe.getLocalizedMessage());
        }
        catch (InvalidProductInfoException ipie){
            return new ResponseT<>(null,ipie.getLocalizedMessage());
        }
        return new ResponseT<>(new PresentationProduct(new ServiceProduct(changed)));
    }

    //Make:nitay
    //done

    /**
     * Appoint a new Shop owner
     * @param key - shop identifier
     * @param targetUser - Passive user identifier
     * @param userName - Active user identifier
     * @return Response object
     */
    public Response AppointNewShopOwner(int key,String targetUser, String userName)
    {
        try {
            String s = marketSystem.AppointNewShopOwner(key, targetUser, userName);
            if (s != null)
                return new Response(s);
            return null;
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new ResponseT<>(null,invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done

    /**
     * Appoint a new Shop manager
     * @param key - shop identifier
     * @param targetUser - Passive user identifier
     * @param userName - Active user identifier
     * @return Response object
     */
    public Response AppointNewShopManager(int key,String targetUser, String userName) {
        try {
            String s = marketSystem.AppointNewShopManager(key, targetUser, userName);
            if (s != null)
                return new Response(s);
            return null;
        } catch (IncorrectIdentification incorrectIdentification) {
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new ResponseT<>(null,invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }
    //Make:nitay

    /**
     * add a specific Shop manager's permission
     * @param key - shop identifier
     * @param shopManagersPermissionsList - wanted permissions to add
     * @param targetUser - the manager who receives the permissions identifier.
     * @param ownerID - the user who gives  the permissions identifier.
     * @return Response object
     */
    public Response AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID) {
        try {
            String s = marketSystem.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
            if (s != null)
                return new Response(s);
            return null;
        } catch (IncorrectIdentification incorrectIdentification) {
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new ResponseT<>(null,invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }
    //Make:nitay

    /**
     * Remove a specific Shop manager's permission
     * @param key
     * @param shopManagersPermissionsList
     * @param managerUser
     * @param ownerID
     * @return Response object
     */
    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID)
    {
        try {
            String s = marketSystem.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
            if (s != null)
                return new Response(s);
            return null;
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done

    /**
     * Close an active shop
     * @param shopId
     * @param userName
     * @return Response object
     */
    public Response CloseShop(int shopId,String userName) {
        try {
            String s = marketSystem.CloseShop(shopId, userName);
            if (s != null)
                return new Response(s);
            return null;
        }
        catch (IncorrectIdentification | BlankDataExc | InvalidSequenceOperationsExc incorrectIdentification){
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        }
    }

    /**
     * Make a closed shop -> active
     * @param shopId
     * @param userName
     * @return Response object
     */
    public Response OpenShop(int shopId,String userName) {
        try {
            String s = marketSystem.OpenShop(shopId, userName);
            if (s != null)
                return new Response(s);
            return null;
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc | BlankDataExc incorrectIdentification){
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        }
    }
    //Make:nitay

    /**
     * Query for information about the relevant shop's employees
     * @param shopName - shop identifier
     * @param f
     * @param userName - user identifier
     * @return Response object
     */
    public List<ResponseT<PresentationUser>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName)
    {
        List<ResponseT<PresentationUser>> responseTList = new LinkedList<>();
        try {
            List<User> s = marketSystem.RequestShopOfficialsInfo(shopName, f, userName);
            if (s != null)
                for(User userSearchInfo: s)
                    responseTList.add(new ResponseT<>(new PresentationUser(userSearchInfo)));
            return responseTList;
        }
        catch (IncorrectIdentification incorrectIdentification){
            responseTList.add(new ResponseT<>(null,incorrectIdentification.getLocalizedMessage()));
            return responseTList;
        }
    }
    //Make:nitay

    /**
     * Query for store purchase history
     * @param shopName - shop identifier
     * @param f
     * @param userName - user identifier
     * @return list of Response object
     */
    public List<ResponseT<Order>> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userName)
    {
        List<ResponseT<Order>> output = new LinkedList<>();
        try {
            List<Order> orders = marketSystem.RequestInformationOfShopsSalesHistory(shopName, f, userName);
            if (orders != null)
                for(Order order:orders)
                    output.add(new ResponseT<>(order));
            return output;
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc incorrectIdentification){
            output.add(new ResponseT<>(null,incorrectIdentification.getLocalizedMessage()));
            return output;
        }
    }
    //Make:nitay
    public Response DeleteUserTest(String[] usernames) {
        try {
            marketSystem.deleteUserTest(usernames);
            return new Response();
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }

    }
    //TODO:
    public Response DeleteUser(String usernames) {
        try {
            if(marketSystem.deleteUser(usernames))
                return new Response();
        }
        catch (BlankDataExc | InvalidSequenceOperationsExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
        return null;
    }
    //Make:nitay

    /**
     * Remove a product from the shop inventory
     * @param productId
     * @param username
     * @param shopname
     * @return Response object
     */
    public Response RemoveProductFromShopInventory(int productId, String username, int shopname)
    {
        int removedProductID;
        try {
            removedProductID = marketSystem.RemoveProductFromShopInventory(productId, username, shopname);
        }catch (InvalidAuthorizationException | IncorrectIdentification | BlankDataExc iae){
            return new Response(iae.getLocalizedMessage());
        }
        if(removedProductID != -1)
            return new Response();
        return null;
    }

    //make:shahar

    /**
     * Query for store purchase history
     * @param userName
     * @param f
     * @param shopID
     * @return list of Response object
     */
    public List<ResponseT<Order>> getOrderHistoryForShops(String userName, Filter<Order> f, List<Integer> shopID){
        List<ResponseT<Order>> output = new LinkedList<>();
        try {
             List<Order> result = marketSystem.getOrderHistoryForShops(userName, f, shopID);
             for(Order o :result)
                 output.add(new ResponseT<>(o));
        }
        catch (InvalidAuthorizationException | IncorrectIdentification | ShopNotFoundException iae){
            output.add(new ResponseT(iae.getLocalizedMessage()));
            return output;
        }
        return output;
    }

    //make:shahar

    /**
     * Query for the user's purchase history
     * @param userName
     * @param f
     * @param userNames
     * @return list of Response object
     */
    public List<ResponseT<Order>> getOrderHistoryForUser(String userName, Filter<Order> f, List<String>  userNames){
        List<ResponseT<Order>> output= new LinkedList<>();
        List<Order> result;
        try {
            result = marketSystem.getOrderHistoryForUser(userName, f, userNames);
            for(Order order:result)
                output.add(new ResponseT<>(order));
        }
        catch (InvalidAuthorizationException | IncorrectIdentification iae){
            output.add(new ResponseT(iae.getLocalizedMessage()));
            return output;
        }
        return output;
    }

    /**
     * Remove registered user from the Market if it has no permissions
     * @param usernames - active user identifier
     * @param targetUser - positive user identifier
     * @return Response object
     */
    public Response DismissalUserBySystemManager(String usernames,String targetUser) {
        try {
            if(marketSystem.DismissalUser(usernames,targetUser))
                return new Response();
            return new ResponseT(null,"");
        }
        catch (BlankDataExc | IncorrectIdentification | InvalidSequenceOperationsExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
    }

    /**
     * Remove a store owner's permission in a particular store
     * @param usernames - active user identifier
     * @param targetUser - positive user identifier
     * @param shop - shop identifier
     * @return Response object
     */
    public Response DismissalOwnerByOwner(String usernames,String targetUser,int shop) {
        try {
            if(marketSystem.DismissalOwner(usernames,targetUser,shop))
                return new Response();
            return new ResponseT(null,"");
        }
        catch (BlankDataExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
        catch ( IncorrectIdentification incorrectIdentification){
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
        catch (ShopNotFoundException shopNotFoundException){
            return new Response(shopNotFoundException.getLocalizedMessage());
        }
    }

    /**
     * System manager query for user's Info
     * @param f
     * @param userName
     * @return list of Response object
     */
    public List<ResponseT<PresentationUser>> RequestUserInfo(SearchUserFilter f, String userName)
    {
        List<ResponseT<PresentationUser>> responseTList = new LinkedList<>();
        try {
            List<User> s = marketSystem.RequestUserInfo(f, userName);
            if (s != null)
                for(User userSearchInfo: s)
                    responseTList.add(new ResponseT<>(new PresentationUser(userSearchInfo)));
            return responseTList;
        }
        catch (IncorrectIdentification | InvalidSequenceOperationsExc incorrectIdentification){
            responseTList.add(new ResponseT<>(null,incorrectIdentification.getLocalizedMessage()));
            return responseTList;
        }
    }
}
