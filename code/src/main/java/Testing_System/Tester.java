package Testing_System;

import domain.Response;
import domain.ResponseT;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;

import java.util.List;
import java.util.Map;


public class Tester {
    private Bridge br;

    public Tester() {
        br = Selector.GetBridge();
    }

    /*Result<#t | # f,  return value>
     * t - success
     * f - failed
     *
     */

    //Guest-Visitor General
    //done
    public ResponseT<User> Login(String username, String pw) {
        return br.Login(username, pw);
    }

    //done
    public Response Register(String username, String pw) {
        return br.Register(username, pw);
    }

    //done - not needed
    public ResponseT<User> EnterMarket() {
        return br.EnterMarket();
    }

    //done - not needed
    public Response LeaveMarket() {
        return br.LeaveMarket();
    }

    //Guest-Visitor Purchase
    //
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter) {
        return br.GetShopsInfo(userID, filter);
    }

    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<ProductInfo> f) {
        return br.GetProductInfoInShop(userID, shopID, f);
    }

    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID ,String pName, Filter<ProductInfo> f) {
        return br.SearchProductByName(userID, pName, f);
    }

//    public Result<Boolean, List<ProductInfo>> SearchProductByCategory(String userID ,String category,Filter<ProductInfo> f ) {
//        return br.SearchProductByCategory(userID, category,f);
//    }

    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID ,String keyword, Filter<ProductInfo> f) {
        return br.SearchProductByKeyword(userID, keyword, f);
    }

    public Response AddToShoppingCart(String userID, int shopID, int productId, int amount) {
        return br.AddToShoppingCart(userID, shopID, productId, amount);
    }

    public Response EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return br.EditShoppingCart(userId, shopId, productId, amount);
    }

    public Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return br.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
    }//???

    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname) {
        return br.CalculatePriceForProduct(p, shopname);
    }

    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname) {
        return br.CheckDiscountPolicyForProduct(p, shopname);
    }

    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname) {
        return br.CheckDiscountForProduct(p, shopname);
    }

    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID) {
        return br.CheckIfProductAvailable(p, shopID);
    }

    //Shop-Owner Options
    public ResponseT<Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID)
    {
        return br.AddProductToShopInventory(pName,pDis, pCat, price, amount, username, shopID);
    }

    public Response RemoveProductFromShopInventory(int productId, String username, int shopname) {
        return br.RemoveProductFromShopInventory(productId,username, shopname);
    }

    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) {
        return br.ChangeProduct(username,p,shopID);
    }

//    public Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr) { return br.AddBuyingShopPolicy(shopname,pr); }
//
//    public Result<Boolean, String> RemoveBuyingShopPolicy(String shopname) { return br.RemoveBuyingShopPolicy(shopname); }
//
//    public Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount) { return br.AddDiscountShopPolicy(shopname,discount); }
//
//    public Result<Boolean, String> RemoveDiscountShopPolicy(String shopname) { return br.RemoveDiscountShopPolicy(shopname); }
//
//    public Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr ) { return br.AddBuyingProductPolicy(shopname, pr); }
//
//    public Result<Boolean, String> RemoveBuyingProductPolicy(String shopname) { return br.RemoveBuyingProductPolicy(shopname);}

    public Response AppointNewShopOwner(int key, String targetUser, String userId) {
        return br.AppointNewShopOwner(key, targetUser, userId);
    }

    public Response AppointNewShopManager(int key, String targetUser, String userId) {
        return br.AppointNewShopManager(key, targetUser, userId);
    }

    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID)
    {
        return br.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
    }

    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID) {
        return br.RemoveShopManagerPermissions(key,shopManagersPermissionsList, managerUser, ownerID);
    }

    public Response CloseShop(int shopID, String userID) {
        return br.CloseShop(shopID, userID);
    }

    /*
    the following tests cover both cases for system manager as well
     */
    public Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userId) {
        return br.RequestShopOfficialsInfo(shopName, f, userId);
    }

    public Result<Boolean, List<Order>> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userId) {
        return br.RequestShopSalesInfo(shopName, f, userId);
    }


    //Member-Visitor General
    //DONE
    public ResponseT<User> Logout(String username) {
        return br.Logout(username);
    }

    //done
    public ResponseT<Shop> CreateShop(String username, String shopname) {
        return br.CreateShop(username, shopname);
    }


    //System Scnerios
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return br.RealTimeNotification(users, msg);
    }

    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products) /*supply*/ {
        return br.PurchaseDelivery(ti, products);
    }

    public Result<Boolean, Boolean> Payment(TransactionInfo ti) {
        return br.Payment(ti);
    }

    //done
    public Response StartMarket(PaymentService payment, SupplyService supply, String userID, String password) {
        return br.StartMarket(payment, supply, userID, password);
    }

    public Result<Boolean, String> AddSupplyService(String path) {
        return br.AddSupplyService(path);
    }

    public Result<Boolean, String> RemoveSupplyService(String path) {
        return br.RemoveSupplyService(path);
    }

    public Result<Boolean, String> AddPaymentService(String path) {
        return br.AddPaymentService(path);
    }

    public Result<Boolean, String> RemovePaymentService(String path) {
        return br.RemovePaymentService(path);
    }




    public Result<Boolean, String> DeleteUserTest(String[] usernames)
    {
        return br.DeleteUserTest(usernames);
    }







}
