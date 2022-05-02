package Testing_System;

import Service.Services;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.discount.Discount;
import domain.user.*;

import java.util.List;
import java.util.Map;

public class RealBridge implements  Bridge{

    private Services sv;
    public RealBridge()
    {
        sv = new Services();
    }

    @Override
    public Result<Boolean, Boolean> Login(String username, String pw) {
        return sv.Login(username, pw);
    }

    @Override
    public Result<Boolean, Boolean> Register(String username, String pw) {
        return sv.Register(username, pw);
    }

    @Override
    public Result<Boolean, String> EnterMarket() {
        return sv.EnterMarket();
    }

    @Override
    public Result<Boolean, String> LeaveMarket() {
        return sv.LeaveMarket();
    }

    @Override
    public Result<Boolean, String> Logout(String username) {
        return sv.Logout(username);
    }

    @Override
    public Result<Boolean, Integer> CreateShop(String username, String shopname) {
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
    public Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password) {
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
        return sv.GetShopsInfo(userID, filter);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<ProductInfo> f) {
        return sv.GetProductInfoInShop(userID, shopID, f);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID, String pName, Filter<ProductInfo> f) {
        return sv.SearchProductByName(userID, pName, f);
    }


    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        return sv.SearchProductByKeyword(userID, keyword, f);
    }

    @Override
    public Result<Boolean, Boolean> AddToShoppingCart(String userID, int shopID,int productId, int amount) {
        return sv.AddToShoppingCart(userID, shopID, productId, amount);
    }

    @Override
    public Result<Boolean, Boolean> EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return sv.EditShoppingCart(userId, shopId, productId, amount);
    }

    @Override
    public Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return sv.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate);
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
    public Result<Boolean, Boolean> CheckIfProductAvailable(Product p, int shopID) {
        return sv.CheckIfProductAvailable(p, shopID);
    }

    @Override
    public Result<Boolean, Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        return sv.AddProductToShopInventory(pName, pDis, pCat, price, amount, username, shopID);
    }

    @Override
    public Result<Boolean, Product> ChangeProduct(String username, Product p, int shopID) {
        return sv.ChangeProduct(username, p, shopID);
    }

    @Override
    public Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, int shopname) {
        return sv.RemoveProductFromShopInventory(productId, username, shopname);
    }

    @Override
    public Result<Boolean, String> AppointNewShopOwner(int key, String targetUser, String userId) {
        return sv.AppointNewShopOwner(key, targetUser, userId);
    }

    @Override
    public Result<Boolean, String> AppointNewShopManager(int key, String targetUser, String userId) {
        return sv.AppointNewShopManager(key, targetUser, userId);
    }

    @Override
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        return AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
    }

    @Override
    public Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        return sv.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
    }

    @Override
    public Result<Boolean, String> CloseShop(int shopID, String username) {
        return sv.CloseShop(shopID, username);
    }

    @Override
    public Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f,String userId) {
        return sv.RequestShopOfficialsInfo(shopName, f, userId);
    }

    @Override
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        return null;
    }

    @Override
    public Result<Boolean, List<Order>> RequestShopSalesInfo(int shopName, SearchOrderFilter f, String userId) {
        return sv.RequestInformationOfShopsSalesHistory(shopName, f, userId);
    }
}
