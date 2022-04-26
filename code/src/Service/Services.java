package Service;


import Testing_System.Result;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.discount.Discount;
import domain.user.Filter;
import domain.user.TransactionInfo;
import domain.user.User;
import domain.user.UserController;

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

    public Result<Boolean,String> EnterMarket()
    {
        //TODO: happens at   MarketSystem getInstance()
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
        boolean b = marketSystem.logOut(username);
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

    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products)
    {

    }


    //supply
    public Result<Boolean, String> Payment(TransactionInfo ti)
    {

    }

    public Result<Boolean, String> StartMarket()
    {
        marketSystem.start();
    }

    public Result<Boolean, String> AddSupplyService(String path)
    {

    }

    public Result<Boolean, String> RemoveSupplyService(String path)
    {

    }

    public Result<Boolean, String> AddPaymentService(String path)
    {

    }

    public Result<Boolean, String> RemovePaymentService(String path)
    {

    }

    //Guest-Visitor Shop options
    public Result<Boolean, String> GetShopsInfo()
    {

    }
    //Make:nitay
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(int shopname, Filter<ProductInfo> f)
    {
        List<ProductInfo> GetProductInfoInShop = marketSystem.getInfoOfProductInShop(shopname,f);
        Result<Boolean, List<ProductInfo>> CreateShop;
        if (GetProductInfoInShop.size()>=0)
            CreateShop = new Result<>(true, GetProductInfoInShop);
        else CreateShop = new Result<>(false, GetProductInfoInShop);
        return CreateShop;
    }//display information of a product?

    public Result<Boolean, String> SearchProductByName(String pName)
    {

    }

    public Result<Boolean, String> SearchProductByCategory(String pName)
    {

    }

    public Result<Boolean, String> SearchProductByKeyword(String keyword)
    {

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

    //Shay
    public Result<Boolean, Integer> CalculatePriceForProduct(Map<Integer, Integer>items, int shopID)
    {
        Shop shop = ShopController.getInstance().getShop(shopID);
        if(shop == null)
            return new Result<>(false, null);
        double totalPrice = shop.calculateTotalAmountOfOrder(items);
        return new Result<>(true, totalPrice);
    }

    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname)
    {

    }

    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname)
    {

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

    public Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr)
    {

    }

    public Result<Boolean, String> RemoveBuyingShopPolicy(String shopname)
    {

    }

    public Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount)
    {

    }

    public Result<Boolean, String> RemoveDiscountShopPolicy(String shopname)
    {

    }

    public Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr)
    {

    }

    public Result<Boolean, String> RemoveBuyingProductPolicy(String shopname)
    {

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

    public Result<Boolean, String> RequestShopOfficialsInfo(String shopname, Filter f)
    {

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
