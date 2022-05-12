package Service;


import Testing_System.Result;
import domain.Response;
import domain.ResponseT;
import domain.market.*;
import domain.shop.*;

import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;


import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Services {

    private MarketSystem marketSystem;

    private Services(){
        marketSystem = MarketSystem.getInstance();
    }

    private static class ServicesHolder{
        private static final Services instance = new Services();
    }

    public static Services getInstance(){
        return ServicesHolder.instance;
    }

    //General Guest-Visitor
    //Make:nitay InvalidSequenceOperationsExc, BlankDataExc, IncorrectIdentification
    public ResponseT<User> Login(String username, String pw) {
        ResponseT<User> output;
        try {
            User b = marketSystem.logIn(username, pw);
            output = new ResponseT<>(b);
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
    public ResponseT<User> EnterMarket() { //
        User EnterMarket = marketSystem.EnterMarket();
        if (EnterMarket != null)
            return new ResponseT<>(EnterMarket);
        else return new ResponseT<>();
    }
    //Make:nitay   IncorrectIdentification, InvalidSequenceOperationsExc
    //TODO: need to check
    public Response LeaveMarket() {
        try {
            User output = marketSystem.LeaveMarket("");
            if (output != null)
                return new Response();
            else return null;
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
    }


    //General Member-Visitor
    public ResponseT<User> Logout(String username) {
        User output;
        try {
            output = marketSystem.logout(username);
            return new ResponseT<>(output);
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc) {
            return new ResponseT<>(null,invalidSequenceOperationsExc.getLocalizedMessage());
        } catch (IncorrectIdentification incorrectIdentification) {
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
        }
    }
    //Make:nitay BlankDataExc  BlankDataExc, IncorrectIdentification
    public ResponseT<Shop> CreateShop(String username, String shopName) {
        try {
            Shop output = marketSystem.createShop(shopName, null, null, username);
            if (output != null)
                return new ResponseT<>(output);
            else return new ResponseT<>();
        } catch (BlankDataExc blankDataExc) {
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        } catch (IncorrectIdentification incorrectIdentification) {
            return new ResponseT<>(null,incorrectIdentification.getLocalizedMessage());
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
    public List<ResponseT<ShopInfo>> GetShopsInfo(String userName, Filter<ShopInfo> filter) {
        try {
            List<ResponseT<ShopInfo>> list = new LinkedList<>();
            List<ShopInfo> shopInfo = marketSystem.getInfoOfShops(userName, filter);
            for (ShopInfo shopInfo1 : shopInfo){
                list.add(new ResponseT<>(shopInfo1));
            }
            return list;
        } catch (BlankDataExc blankDataExc) {
            return null;
        }
    }

    //Make:nitay
    public List<ResponseT<ProductInfo>> GetProductInfoInShop(String userName ,int shopID, Filter<ProductInfo> f)
    {
        List<ProductInfo> GetProductInfoInShop = marketSystem.getInfoOfProductInShop(userName,shopID,f);
        List<ResponseT<ProductInfo>> CreateShop = new LinkedList<>();
        if (GetProductInfoInShop == null || GetProductInfoInShop.size() == 0)
            return null;
        for(ProductInfo productInfo:GetProductInfoInShop){
            CreateShop.add(new ResponseT<>(productInfo));
        }
        return CreateShop;
    }//display information of a product?


    //make:shahar
    public List<ResponseT<ProductInfo>> SearchProductByName(String userName ,String pName, Filter<ProductInfo> f) //done
    {
        List<ResponseT<ProductInfo>> result = new LinkedList<>();
        List<ProductInfo> products = marketSystem.searchProductByName(userName ,pName, f);
        for(ProductInfo productInfo: products){
            result.add(new ResponseT<>(productInfo));
        }
        return result;
    }

    //make:shahar
    public List<ResponseT<ProductInfo>> SearchProductByKeyword(String userName ,String keyword, Filter<ProductInfo> f)
    {
        List<ResponseT<ProductInfo>> result = new LinkedList<>();
        List<ProductInfo> products = marketSystem.searchProductByKeyword(userName ,keyword, f);
        for(ProductInfo productInfo: products){
            result.add(new ResponseT<>(productInfo));
        }
        return result;
    }

    //make: shahar
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
    public Response RemoveFromShoppingCart(String userName, int shopId, int productId) throws InvalidSequenceOperationsExc {
        boolean success = marketSystem.removeProductFromCart(userName, shopId, productId);
        if(success)
            return new Response();
        else
            return new Response();
    }

    //Make nitay  IncorrectIdentification, BlankDataExc
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
    public ResponseT<Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username,int shopID)
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
        return new ResponseT<>(p);
    }

    //shay
    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID)
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
        return new ResponseT<>(changed);
    }

    //Make:nitay
    //done IncorrectIdentification, BlankDataExc
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
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
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
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
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
        }
    }
    //Make:nitay
    //done  IncorrectIdentification, BlankDataExc
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
    //done  IncorrectIdentification, BlankDataExc
    public Response CloseShop(int shopId,String userName) {
        try {
            String s = marketSystem.CloseShop(shopId, userName);
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
        catch (InvalidSequenceOperationsExc blankDataExc){
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        }
    }
    public Response OpenShop(int shopId,String userName) {
        try {
            String s = marketSystem.OpenShop(shopId, userName);
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
        catch (InvalidSequenceOperationsExc blankDataExc){
            return new ResponseT<>(null,blankDataExc.getLocalizedMessage());
        }
    }
    //Make:nitay  IncorrectIdentification
    public List<ResponseT<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName)
    {
        List<ResponseT<UserSearchInfo>> responseTList = new LinkedList<>();
        try {
            List<UserSearchInfo> s = marketSystem.RequestShopOfficialsInfo(shopName, f, userName);
            if (s != null)
                for(UserSearchInfo userSearchInfo: s)
                    responseTList.add(new ResponseT<>(userSearchInfo));
            return responseTList;
        }
        catch (IncorrectIdentification incorrectIdentification){
            responseTList.add(new ResponseT<>(null,incorrectIdentification.getLocalizedMessage()));
            return responseTList;
        }
    }
    //Make:nitay  IncorrectIdentification
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
        catch (IncorrectIdentification incorrectIdentification){
            output.add(new ResponseT<>(null,incorrectIdentification.getLocalizedMessage()));
            return output;
        }
    }
    //Make:nitay
    public Response DeleteUserTest(String[] usernames) {
        try {
            marketSystem.deleteUserTest(usernames);
        } catch (InvalidSequenceOperationsExc e) {
            return new Response(e.getMessage());
        }
        return new Response();
    }
    //TODO:
    public Response DeleteUser(String usernames) {
        try {
            if(marketSystem.deleteUser(usernames))
                return new Response();
        }
        catch (BlankDataExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
        catch (InvalidSequenceOperationsExc invalidSequenceOperationsExc){
            return new Response(invalidSequenceOperationsExc.getLocalizedMessage());
        }
        return null;
    }
    //Make:nitay  InvalidAuthorizationException, IncorrectIdentification, BlankDataExc
    public Response RemoveProductFromShopInventory(int productId, String username, int shopname)
    {
        int removedProductID;
        try {
            removedProductID = marketSystem.RemoveProductFromShopInventory(productId, username, shopname);
        }catch (InvalidAuthorizationException iae){
            return new Response(iae.getLocalizedMessage());
        }
        catch (BlankDataExc blankDataExc){
            return new Response(blankDataExc.getLocalizedMessage());
        }
        catch (IncorrectIdentification incorrectIdentification){
            return new Response(incorrectIdentification.getLocalizedMessage());
        }
        if(removedProductID != -1)
            return new Response();
        return null;
    }

    //make:shahar   InvalidAuthorizationException, IncorrectIdentification
    public List<ResponseT<Order>> getOrderHistoryForShops(String userName, Filter<Order> f, List<Integer> shopID){
        List<ResponseT<Order>> output = new LinkedList<>();
        try {
             List<Order> result = marketSystem.getOrderHistoryForShops(userName, f, shopID);
             for(Order o :result)
                 output.add(new ResponseT<>(o));
        }
        catch (InvalidAuthorizationException iae){
            output.add(new ResponseT(iae.getLocalizedMessage()));
            return output;
        }
        catch (IncorrectIdentification incorrectIdentification){
            output.add(new ResponseT(incorrectIdentification.getLocalizedMessage()));
            return output;
        }
        return output;
    }

    //make:shahar   InvalidAuthorizationException, IncorrectIdentification
    public List<ResponseT<Order>> getOrderHistoryForUser(String userName, Filter<Order> f, List<String>  userNames){
        List<ResponseT<Order>> output= new LinkedList<>();
        List<Order> result;
        try {
            result = marketSystem.getOrderHistoryForUser(userName, f, userNames);
            for(Order order:result)
                output.add(new ResponseT<>(order));
        }
        catch (InvalidAuthorizationException iae){
            output.add(new ResponseT(iae.getLocalizedMessage()));
            return output;
        }
        catch (IncorrectIdentification incorrectIdentification){
            output.add(new ResponseT(incorrectIdentification.getLocalizedMessage()));
            return output;
        }
        return output;
    }
/*
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
 */
}
