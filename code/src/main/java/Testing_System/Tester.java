package Testing_System;


import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Exceptions.ShopNotFoundException;
import domain.Response;
import domain.ResponseList;
import domain.ResponseMap;
import domain.ResponseT;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.notifications.UserObserver;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;
import domain.user.filter.SearchUserFilter;

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
    public ResponseT<User> Login(String guest, String username, String pw) {
        return br.Login(guest, username, pw);
    }

    //done
    public Response Register(String guest, String username, String pw) {
        return br.Register(guest, username, pw);
    }

    //done - not needed
    public ResponseT<User> EnterMarket() {
        return br.EnterMarket();
    }

    //done - not needed
    public Response LeaveMarket(String username) {
        return br.LeaveMarket(username);
    }

    //Guest-Visitor Purchase
    //
    public ResponseList<Shop> GetShopsInfo(String userName, Filter<Shop> filter) {
        return br.GetShopsInfo(userName, filter);
    }

    public ResponseList<Product> GetProductInfoInShop(String userName, int shopID, Filter<Product> f) {
        return br.GetProductInfoInShop(userName, shopID, f);
    }

    public ResponseMap<Integer,List<Product>> SearchProductByName(String userName, String pName, Filter<Product> f) {
        return br.SearchProductByName(userName, pName, f);
    }

//    public Result<Boolean, List<ProductInfo>> SearchProductByCategory(String userID ,String category,Filter<ProductInfo> f ) {
//        return br.SearchProductByCategory(userID, category,f);
//    }

    public ResponseMap<Integer,List<Product>> SearchProductByKeyword(String userName, String keyword, Filter<Product> f) {
        return br.SearchProductByKeyword(userName, keyword, f);
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
    public ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID)
    {
        return br.AddProductToShopInventory(serialNumber, pName, pDis, pCat, price, amount, username, shopID);
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

    public Response AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID)
    {
        return br.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
    }

    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID) {
        return br.RemoveShopManagerPermissions(key,shopManagersPermissionsList, managerUser, ownerID);
    }

    public Response CloseShop(int shopID, String userID) {
        return br.CloseShop(shopID, userID);
    }

    public Response OpenShop(int shopId, String userName){
        return br.OpenShop(shopId,userName);
    }

    /*
    the following tests cover both cases for system manager as well
     */
    public ResponseList<User> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName) {
        return br.RequestShopOfficialsInfo(shopName, f, userName);
    }

    public ResponseList<Order> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userName) {
        return br.RequestInformationOfShopsSalesHistory(shopName, f, userName);
    }


    //Member-Visitor General
    //DONE
    public ResponseT<User> Logout(String username) {
        return br.Logout(username);
    }

    //done
    public ResponseT<Shop> CreateShop(String dis, String username, String shopname) {
        return br.CreateShop(dis, username, shopname);
    }


    //System Scnerios
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return br.RealTimeNotification(users, msg);
    }

    public ResponseT<Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products)
        /*supply*/ {
        return br.PurchaseDelivery(ti, products);
    }

    public ResponseT<Boolean> Payment(TransactionInfo ti) {
        return br.Payment(ti);
    }

    //done
    public Response StartMarket(PaymentService payment, SupplyService supply) {
        return br.StartMarket(payment, supply);
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

    public Response DeleteUserTest(String[] usernames)
    {
        return br.DeleteUserTest(usernames);
    }

    public Response DismissalUserBySystemManager(String usernames,String targetUser) {
        return br.DismissalUserBySystemManager(usernames, targetUser);
    }

    public Response DismissalOwnerByOwner(String usernames,String targetUser,int shop) {
        return br.DismissalOwnerByOwner(usernames, targetUser, shop);
    }

    public ResponseList<User> RequestUserInfo(SearchUserFilter f, String userName)
    {
       return br.RequestUserInfo(f,userName);
    }

    public Response CreateSystemManager(String systemManager,String username, String pw) {
        return br.CreateSystemManager(systemManager, username, pw);
    }
    public ResponseMap<Integer, List<Product>> SearchProductByCategory(String userName, String category, Filter<Product> f){
            return br.SearchProductByCategory(userName,category,f);
    }

}
