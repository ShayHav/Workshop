package Service;


import Testing_System.Result;
import domain.market.*;
import domain.shop.*;
import domain.user.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Services implements Runnable {
    String userName = "";
    UtilP utilP = new UtilP();
    BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
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
    public Result<Boolean,Boolean> Login(String username, String pw) {
        try {
            boolean b = marketSystem.logIn(username, pw);
            Result<Boolean, Boolean> output = new Result<>(b, b);
            return output;
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, false);
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new Result<>(false, false);
        } catch (IncorrectIdentification incorrectIdentification) {
            return new Result<>(false, false);
        } catch (InvalidAuthorizationException invalidAuthorizationException) {
            return new Result<>(false, false);
        }
    }
    //Make:nitay
    public Result<Boolean,Boolean> Register(String username, String pw) {
        try {
            boolean b = marketSystem.register(username, pw);
            Result<Boolean, Boolean> output = new Result<>(b, b);
            return output;
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, false);
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new Result<>(false, false);
        }
    }
    //Make:nitay
    public Result<Boolean,String> EnterMarket() {
        String EnterMarket = marketSystem.EnterMarket();
        if (EnterMarket != null)
            return new Result<>(true, EnterMarket);
        else return new Result<>(false, null);
    }
    //Make:nitay   IncorrectIdentification, InvalidSequenceOperationsExc
    //TODO: need to check
    public Result<Boolean,String> LeaveMarket() {
        try {
            String output = marketSystem.LeaveMarket(userName);
            Result<Boolean, String> leaveMarket;
            if (output != null)
                leaveMarket = new Result<>(true, output);
            else leaveMarket = new Result<>(false, null);
            return leaveMarket;
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,incorrectIdentification.getLocalizedMessage());
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Result<>(false,invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }


    //General Member-Visitor
    public Result<Boolean,String> Logout(String username) {
        try {
            String b = marketSystem.logout(username);
            Result<Boolean, String> output = new Result(b, b);
            return output;
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new Result<>(false, invalidSequenceOperationsExc.getLocalizedMessage());
        } catch (IncorrectIdentification incorrectIdentification) {
            return new Result<>(false, incorrectIdentification.getLocalizedMessage());
        }
    }
    //Make:nitay BlankDataExc  BlankDataExc, IncorrectIdentification
    public Result<Boolean, Integer> CreateShop(String username, String shopName) {
        try {
            Integer output = marketSystem.createShop(shopName, null, null, username);
            Result<Boolean, Integer> CreateShop;
            if (output != -1)
                CreateShop = new Result<>(true, output);
            else CreateShop = new Result<>(false, output);
            return CreateShop;
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, -1);
        } catch (IncorrectIdentification incorrectIdentification) {
            return new Result<>(false, -1);
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
        MarketSystem m = MarketSystem.getInstance();
        boolean ans  = m.supply(ti, products);
        return new Result<>(ans, ans);
    }

    //shay  //TODO: only for test?
    public Result<Boolean, Boolean> Payment(TransactionInfo ti)
    {
        //MarketSystem m = MarketSystem.getInstance();
        boolean ans = marketSystem.pay(ti);
        return new Result<>(ans, ans);
    }

    public Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password)
    {
        try {
            boolean b = marketSystem.start(payment, supply, userID, password);
            return new Result<>(b, b);
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Result<>(false,false);
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
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter) {
        try {
            Result<Boolean, List<ShopInfo>> result;
            List<ShopInfo> shopInfo = marketSystem.getInfoOfShops(userID, filter);
            if (shopInfo == null) {
                result = new Result<>(false, shopInfo);
            } else
                result = new Result<>(true, shopInfo);

            return result;
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, null);
        }
    }

    //Make:nitay
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<ProductInfo> f)
    {
        List<ProductInfo> GetProductInfoInShop = marketSystem.getInfoOfProductInShop(userID,shopID,f);
        Result<Boolean, List<ProductInfo>> CreateShop;
        if (GetProductInfoInShop == null || GetProductInfoInShop.size() == 0)
            CreateShop = new Result<>(false, GetProductInfoInShop);
        else CreateShop = new Result<>(true, GetProductInfoInShop);
        return CreateShop;
    }//display information of a product?


    //make:shahar
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID ,String pName, Filter<ProductInfo> f) //done
    {
        Result<Boolean, List<ProductInfo>> result;
        List<ProductInfo> products = marketSystem.searchProductByName(userID ,pName, f);
        if(products == null)
            result = new Result<>(true, null);
        else
            result = new Result<>(false, products);

        return result;

    }

    //make:shahar
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID ,String keyword, Filter<ProductInfo> f)
    {
        Result<Boolean, List<ProductInfo>> result;
        List<ProductInfo> products = marketSystem.searchProductByKeyword(userID ,keyword, f);
        if(products == null)
            result = new Result<>(true, null);
        else
            result = new Result<>(false, products);

        return result;
    }

    //make: shahar
    public Result<Boolean, Boolean> AddToShoppingCart(String userID, int shopID,int productId, int amount)
    {
        try {

            boolean success = marketSystem.AddProductToCart(userID, shopID, productId, amount);
            if (success)
                return new Result<>(true, true);
            else
                return new Result<>(false, false);
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Result<>(false,null);
        }
        catch (ShopNotFoundException shopNotFoundException){
            return new Result<>(false,null);
        }
    }

    //make:shahar
    public Result<Boolean, Boolean> EditShoppingCart(String userId, int shopId, int productId, int amount)
    {
        try {
            boolean success = marketSystem.EditShoppingCart(userId, shopId, productId, amount);
            if (success)
                return new Result<>(true, true);
            else
                return new Result<>(false, false);
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Result<>(false,null);
        }
    }

    //make:shahar
    public Result<Boolean, Boolean> RemoveFromShoppingCart(String userId, int shopId, int productId) throws InvalidSequenceOperationsExc {
        boolean success = marketSystem.removeProductFromCart(userId, shopId, productId);
        if(success)
            return new Result<>(true, true);
        else
            return new Result<>(false, false);
    }

    //Make nitay  IncorrectIdentification, BlankDataExc
    public Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        List<String> checkout = new LinkedList<>();
        try {
            checkout = marketSystem.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
            if (checkout.size() > 0)
                return new Result<>(false, checkout);
            else return new Result<>(true, null);
        } catch (IncorrectIdentification incorrectIdentification) {
            checkout.add(incorrectIdentification.getLocalizedMessage());
            return new Result<>(false, checkout);
        } catch (BlankDataExc blankDataExc) {
            checkout.add(blankDataExc.getLocalizedMessage());
            return new Result<>(false, checkout);
        }
    }


    //shay
    public Result<Boolean, Boolean> CheckIfProductAvailable(Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new Result<>(false, null);
        }
        return new Result<>(shop.getProduct(p.getId()) != null, shop.getInventory().isInStock(p.getId()));
    }

    //Shay
    //done
    public Result<Boolean, Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username,int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new Result<>(false, null);
        }
        Product p;
        try {
            p = shop.addListing(pName, pDis, pCat, price, amount, username);
        }catch (InvalidAuthorizationException iae){
            return new Result<>(false, null);
        }catch (InvalidProductInfoException ipie){
            //todo prints????
            return new Result<>(false, null);
        }
        if(p == null){
            return new Result<>(false, null);
        }
        return new Result<>(true, p);
    }

    //shay
    public Result<Boolean, Product> ChangeProduct(String username, Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop;
        try{
            shop = controller.getShop(shopID);
        }catch (ShopNotFoundException snfe){
            return new Result<>(false, null);
        }
        int newAmount = ((ServiceProduct)p).getAmount();
        double newPrice = ((ServiceProduct)p).getPrice();
        Product changed;
        try{
            changed = shop.changeProductDetail(p.getId(),p.getName(),p.getDescription(), p.getCategory(),username, newAmount, newPrice);
        }catch (ProductNotFoundException pnfe){
            return new Result<>(false, null);
        }
        catch (InvalidProductInfoException ipie){
            return new Result<>(false, null);
        }
        return new Result<>(changed == null, changed);
    }

    //Make:nitay
    //done IncorrectIdentification, BlankDataExc
    public Result<Boolean, String> AppointNewShopOwner(int key,String targetUser, String userId)
    {
        try {
            String s = marketSystem.AppointNewShopOwner(key, targetUser, userId);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,incorrectIdentification.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new Result<>(false,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
    public Result<Boolean, String> AppointNewShopManager(int key,String targetUser, String userId) {
        try {
            String s = marketSystem.AppointNewShopManager(key, targetUser, userId);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        } catch (IncorrectIdentification incorrectIdentification) {
            return new Result<>(false, incorrectIdentification.getLocalizedMessage());
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID) {
        try {
            String s = marketSystem.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        } catch (IncorrectIdentification incorrectIdentification) {
            return new Result<>(false, incorrectIdentification.getLocalizedMessage());
        } catch (BlankDataExc blankDataExc) {
            return new Result<>(false, blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
    public Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID)
    {
        try {
            String s = marketSystem.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,incorrectIdentification.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new Result<>(false,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
    public Result<Boolean, String> CloseShop(int shopId,String userId) {
        try {
            String s = marketSystem.CloseShop(shopId, userId);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,incorrectIdentification.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new Result<>(false,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay  IncorrectIdentification
    public Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f,String userId)
    {
        try {
            List<UserSearchInfo> s = marketSystem.RequestShopOfficialsInfo(shopName, f, userId);
            if (s != null)
                return new Result<>(true, s);
            else return new Result<>(false, null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,null);
        }
    }
    //Make:nitay  IncorrectIdentification
    public Result<Boolean, List<Order>> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f,String userId)
    {
        try {
            List<Order> orders = marketSystem.RequestInformationOfShopsSalesHistory(shopName, f, userId);
            if (orders != null)
                return new Result<>(true, orders);
            else return new Result<>(false, null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,null);
        }
    }
    //Make:nitay
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        marketSystem.deleteUserTest(usernames);
        return new Result<>(true, null);
    }
    //Make:nitay  InvalidAuthorizationException, IncorrectIdentification, BlankDataExc
    public Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, int shopname)
    {
        int removedProductID;
        try {
            removedProductID = marketSystem.RemoveProductFromShopInventory(productId, username, shopname);
        }catch (InvalidAuthorizationException iae){
            return new Result<>(false, -1);
        }
        catch (BlankDataExc blankDataExc){
            return new Result<>(false,null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,null);
        }
        if(removedProductID != -1)
            return new Result<>(true,productId);
        else return new Result<>(false,-1);
    }

    //make:shahar   InvalidAuthorizationException, IncorrectIdentification
    public Result<Boolean,List<Order>> getOrderHistoryForShops(String userID, Filter<Order> f, List<Integer> shopID){
        List<Order> result;
        try {
             result = marketSystem.getOrderHistoryForShops(userID, f, shopID);
        }
        catch (InvalidAuthorizationException iae){
            return new Result<>(false,null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,null);
        }
        return new Result<>(true, result);
    }

    //make:shahar   InvalidAuthorizationException, IncorrectIdentification
    public Result<Boolean,List<Order>> getOrderHistoryForUser(String userID, Filter<Order> f, List<String>  userIDs){
        List<Order> result;
        try {
            result = marketSystem.getOrderHistoryForUser(userID, f, userIDs);
        }
        catch (InvalidAuthorizationException iae){
            return new Result<>(false,null);
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Result<>(false,null);
        }
        return new Result<>(true,result);
    }

    @Override
    public void run() {
        try {
            deploy();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void deploy() throws IOException {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        boolean exit = false;
        while (!exit) {
            System.out.println("Choose what you want to do:");
            for (int i = 1; i <= Options.getFirstOts().length; i++) {
                System.out.println((i) + ") " + Options.getFirstOts()[i - 1]);
            }
            int op = utilP.scanInt(scanner);
            switch (op) {
                case 1:
                    firstTime();
                    break;
                case 2:
                    logIn();
                    break;
                case 3:
                    exit = true;
                    break;
            }
        }
    }
    private void firstTime(){
        StartMarket(null,null,"admin","password");
        Register("nitay","password");
        Register("omry","password");
    }
    private void logIn() throws IOException {
        String password;
        boolean logged = false;
        while (!logged) {
            System.out.println("In order to Login, please insert your ID:");
            synchronized (this) {
                userName = scanner.readLine(); //input from gui
            }
            System.out.println("In order to Login, please insert your password:");
            synchronized (this) {
                password = scanner.readLine();
            }
            System.out.println("Line 555");
            Result<Boolean,Boolean> r = Login(userName,password);
            if (r.GetFirstElement()) {
                logged = true;
                System.out.println(String.format("%s is logged in",userName));
            }
        }
    }
}
