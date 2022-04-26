package Service;


import Testing_System.Result;
import domain.market.*;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.discount.Discount;
import domain.user.*;

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
    //Make:nitay
    public Result<Boolean,Boolean> Login(String username, String pw){
        boolean b = marketSystem.logIn(username,pw);
        Result<Boolean,Boolean> output = new Result<>(b,b);
        return output;
    }
    //Make:nitay
    public Result<Boolean,Boolean> Register(String username, String pw)
    {
        boolean b = marketSystem.register(username,pw);
        Result<Boolean,Boolean> output = new Result<>(b,b);
        return output;
    }
    //Make:nitay
    public Result<Boolean,String> EnterMarket()
    {
        String EnterMarket = marketSystem.EnterMarket();
        if(EnterMarket!=null)
            return new Result<>(true,EnterMarket);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean,String> LeaveMarket() {
        String output = marketSystem.LeaveMarket();
        Result<Boolean, String> leaveMarket;
        if (output != null)
            leaveMarket = new Result<>(true, output);
        else leaveMarket = new Result<>(false, null);
        return leaveMarket;

    }


    //General Member-Visitor
    public Result<Boolean,String> Logout(String username)
    {
        String b = marketSystem.logOut(username);
        Result<Boolean,String> output = new Result(b,b);
        return output;
    }
    //Make:nitay
    public Result<Boolean, Integer> CreateShop(String username, String shopname)
    {
        Integer output = marketSystem.createShop(username,null,null,username);
        Result<Boolean, Integer> CreateShop;
        if (output != -1)
            CreateShop = new Result<>(true, output);
        else CreateShop = new Result<>(false, output);
        return CreateShop;
    }

    //TODO: impl on later version
    //System
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg)
    {
        return null;
    }

    //shay
    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products)
    {
        MarketSystem m = MarketSystem.getInstance();
        boolean ans  = m.supply(ti, products);
        return new Result<>(ans, ans);
    }


    //shay
    public Result<Boolean, Boolean> Payment(TransactionInfo ti)
    {
        MarketSystem m = MarketSystem.getInstance();
        boolean ans = m.pay(ti);
        return new Result<>(ans, ans);
    }

    public Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password)
    {
       boolean b = marketSystem.start(payment,supply,userID,password);
       return new Result<>(b,b);
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
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter)
    {
        Result<Boolean, List<ShopInfo>> result;
        List<ShopInfo> shopInfo = marketSystem.getInfoOfShops(userID, filter);
        if(shopInfo == null || shopInfo.size() == 0){
            result = new Result<>(false,shopInfo);
        }
        else
            result = new Result<>(true, shopInfo);

        return result;
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
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID ,String pName, Filter<ProductInfo> f)
    {
        Result<Boolean, List<ProductInfo>> result;
        List<ProductInfo> products = marketSystem.searchProductByName(userID ,pName, f);
        if(products.size() > 0)
            result = new Result<>(true, products);
        else
            result = new Result<>(false, products);

        return result;

    }

    //make:shahar
    public Result<Boolean, List<ProductInfo>> SearchProductByCategory(String userID ,String category,Filter<ProductInfo> f )
    {
        Result<Boolean, List<ProductInfo>> result;
        List<ProductInfo> products = marketSystem.searchProductByCategory(userID ,category, f);
        if(products.size() > 0)
            result = new Result<>(true, products);
        else
            result = new Result<>(false, products);

        return result;
    }

    //make:shahar
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID ,String keyword, Filter<ProductInfo> f)
    {
        Result<Boolean, List<ProductInfo>> result;
        List<ProductInfo> products = marketSystem.searchProductByKeyword(userID ,keyword, f);
        if(products.size() > 0)
            result = new Result<>(true, products);
        else
            result = new Result<>(false, products);

        return result;

    }

    public Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount)
    {

    }

    public Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount)
    {

    }
    //Make nitay
    public Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate)
    {
        List<String> Checkout = marketSystem.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
        if(Checkout.size()>0)
            return new Result<>(false,Checkout);
        else return new Result<>(true,null);
    }


    //shay
    public Result<Boolean, Boolean> CheckIfProductAvailable(Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop = controller.getShop(shopID);
        if(shop == null)
            return new Result<>(false, null);

        return new Result<>(shop.getProduct(p.getId()) != null, shop.getInventory().isInStock(p.getId()));
    }

    //Shay
    public Result<Boolean, Integer> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String usernmae,int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop = controller.getShop(shopID);
        if(shop == null)
            return new Result<>(false, -1);
        Product p  = shop.addListing(pName,pDis,pCat,price,amount,usernmae);
        if(p == null){
            return new Result<>(false, -1);
        }
        return new Result<>(true, p.getId());
    }

    //shay
    public Result<Boolean, Product> ChangeProduct(String username, Product p, int shopID)
    {
        ShopController controller = ShopController.getInstance();
        Shop shop = controller.getShop(shopID);
        if(shop == null){
            return new Result<>(false, null);
        }
        Product changed= shop.changeProductDetail(p.getId(),p.getName(),p.getDescription(), p.getCategory(),username);
        return new Result<>(changed == null, changed);
    }

    //Make:nitay
    public Result<Boolean, String> AppointNewShopOwner(int key,String targetUser, String userId)
    {
        String s = marketSystem.AppointNewShopOwner(key,targetUser,userId);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean, String> AppointNewShopManager(int key,String targetUser, String userId)
    {
        String s = marketSystem.AppointNewShopManager(key,targetUser,userId);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID)
    {
        String s = marketSystem.AddShopMangerPermissions(key,shopManagersPermissionsList,targetUser,ownerID);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID)
    {
        String s = marketSystem.RemoveShopManagerPermissions(key,shopManagersPermissionsList,managerUser,ownerID);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean, String> CloseShop(int shopId,String userId) {
        String s = marketSystem.CloseShop(shopId, userId);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false, null);
    }
    //Make:nitay
    public Result<Boolean, String> RequestShopOfficialsInfo(int shopname, SearchOfficialsFilter f)
    {
        String s = marketSystem.RequestShopOfficialsInfo(shopname,f);
        if(s!=null)
            return new Result<>(true,s);
        else return new Result<>(false,null);
    }
    //Make:nitay
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        marketSystem.deleteUserTest(usernames);
        return new Result<>(true, null);
    }
    //Make:nitay
    public Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, int shopname)
    {
        if(marketSystem.RemoveProductFromShopInventory(productId, username, shopname)!=-1)
            return new Result<>(true,productId);
        else return new Result<>(false,-1);
    }

}
