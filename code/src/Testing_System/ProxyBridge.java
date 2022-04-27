package Testing_System;

import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.user.*;

import java.util.List;
import java.util.Map;

public class ProxyBridge implements  Bridge {
    @Override
    public Result<Boolean, Boolean> Login(String username, String pw) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> Register(String username, String pw) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> EnterMarket() {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> LeaveMarket() {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> Logout(String username) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Integer> CreateShop(String username, String shopname) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> Payment(TransactionInfo ti) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> AddSupplyService(String path) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> RemoveSupplyService(String path) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> AddPaymentService(String path) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, String> RemovePaymentService(String path) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID, int shopID, Filter<ProductInfo> f) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID, String pName, Filter<ProductInfo> f) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> AddToShoppingCart(String userID, int shopID, int productId, int amount) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, List<String>> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> CheckIfProductAvailable(Product p, int shopID) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Product> ChangeProduct(String username, Product p, int shopID) {
        return new Result<>(true,null);
    }

    @Override
    public Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, int shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> AppointNewShopOwner(int key, String targetUser, String userId) {
        return null;
    }

    @Override
    public Result<Boolean, String> AppointNewShopManager(int key, String targetUser, String userId) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        return null;
    }

    @Override
    public Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        return null;
    }

    @Override
    public Result<Boolean, String> CloseShop(int shopID, String username) {
        return null;
    }

    @Override
    public Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userId) {
        return null;
    }

    @Override
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        return null;
    }

    @Override
    public Result<Boolean, List<Order>> RequestShopSalesInfo(int shopName, SearchOrderFilter f, String userId) {
        return null;
    }
}
