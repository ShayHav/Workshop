package Testing_System;

import Service.Services;
import domain.Response;
import domain.ResponseT;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filters.Filter;
import domain.user.filters.SearchOfficialsFilter;
import domain.user.filters.SearchOrderFilter;

import java.util.List;
import java.util.Map;

public class RealBridge implements  Bridge{

    private Services sv;
    public RealBridge()
    {
        sv = new Services();
    }

    @Override
    public ResponseT<User> Login(String username, String pw) {
        return sv.Login(username, pw);
    }

    @Override
    public Response Register(String username, String pw) {
        return sv.Register(username, pw);
    }

    @Override
    public ResponseT<User> EnterMarket() {
        return sv.EnterMarket();
    }

    @Override
    public Response LeaveMarket() {
        return sv.LeaveMarket();
    }

    @Override
    public ResponseT<User> Logout(String username) {
        return sv.Logout(username);
    }

    @Override
    public ResponseT<Shop> CreateShop(String username, String shopname) {
        return sv.CreateShop(username, shopname);
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return null;
    }

    @Override
    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        return sv.PurchaseDelivery(ti, products);
    }

    @Override
    public Result<Boolean, Boolean> Payment(TransactionInfo ti) {
        return sv.Payment(ti);
    }

    @Override
    public Response StartMarket(PaymentService payment, SupplyService supply, String userID, String password) {
        return sv.StartMarket(payment, supply, userID, password);
    }

    @Override
    public Result<Boolean, String> AddSupplyService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveSupplyService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddPaymentService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemovePaymentService(String path) {
        return null;
    }

    @Override
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter) {
        return (Result<Boolean, List<ShopInfo>>) sv.GetShopsInfo(userID, filter);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<ProductInfo> f) {
        return (Result<Boolean, List<ProductInfo>>) sv.GetProductInfoInShop(userID, shopID, f);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID, String pName, Filter<ProductInfo> f) {
        return (Result<Boolean, List<ProductInfo>>) sv.SearchProductByName(userID, pName, f);
    }


    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        return (Result<Boolean, List<ProductInfo>>) sv.SearchProductByKeyword(userID, keyword, f);
    }

    @Override
    public Response AddToShoppingCart(String userID, int shopID, int productId, int amount) {
        return sv.AddToShoppingCart(userID, shopID, productId, amount);
    }

    @Override
    public Response EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return sv.EditShoppingCart(userId, shopId, productId, amount);
    }

    @Override
    public Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return (Result<Boolean, List<String>>) sv.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    @Override
    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname) {
        return null;
    }

    @Override
    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID) {
        return sv.CheckIfProductAvailable(p, shopID);
    }

    @Override
    public ResponseT<Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        return sv.AddProductToShopInventory(pName, pDis, pCat, price, amount, username, shopID);
    }

    @Override
    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) {
        return sv.ChangeProduct(username, p, shopID);
    }

    @Override
    public Response RemoveProductFromShopInventory(int productId, String username, int shopname) {
        return sv.RemoveProductFromShopInventory(productId, username, shopname);
    }

    @Override
    public Response AppointNewShopOwner(int key, String targetUser, String userId) {
        return sv.AppointNewShopOwner(key, targetUser, userId);
    }

    @Override
    public Response AppointNewShopManager(int key, String targetUser, String userId) {
        return sv.AppointNewShopManager(key, targetUser, userId);
    }

    @Override
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        return AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
    }

    @Override
    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        return sv.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
    }

    @Override
    public Response CloseShop(int shopID, String username) {
        return sv.CloseShop(shopID, username);
    }

    @Override
    public Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userId) {
        return (Result<Boolean, List<UserSearchInfo>>) sv.RequestShopOfficialsInfo(shopName, f, userId);
    }

    @Override
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        return null;
    }

    @Override
    public Result<Boolean, List<Order>> RequestShopSalesInfo(int shopName, SearchOrderFilter f, String userId) {
        return (Result<Boolean, List<Order>>) sv.RequestInformationOfShopsSalesHistory(shopName, f, userId);
    }
}
