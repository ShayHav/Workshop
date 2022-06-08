package Testing_System;

import Presentation.Model.PresentationShop;
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

public class ProxyBridge implements  Bridge {

    @Override
    public ResponseT<User> Login(String guest, String username, String pw) {
        return null;
    }

    @Override
    public Response Register(String guest, String username, String pw) {
        return null;
    }

    @Override
    public ResponseT<User> EnterMarket() {
        return null;
    }

    @Override
    public Response LeaveMarket(String username) {
        return null;
    }

    @Override
    public ResponseT<User> Logout(String username) {
        return null;
    }

    @Override
    public ResponseT<Shop> CreateShop(String dis, String username, String shopname) {
        return null;
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return null;
    }

    @Override
    public ResponseT<Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        return null;
    }

    @Override
    public ResponseT<Boolean> Payment(TransactionInfo ti) {
        return null;
    }

    @Override
    public Response StartMarket(PaymentService payment, SupplyService supply) {
        return null;
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
    public ResponseList<Shop> GetShopsInfo(String userName, Filter<Shop> filter) {
        return null;
    }

    @Override
    public ResponseList<Product> GetProductInfoInShop(String userName, int shopID, Filter<Product> f) {
        return null;
    }

    @Override
    public ResponseMap<Integer, List<Product>> SearchProductByName(String userName, String pName, Filter<Product> f) {
        return null;
    }

    @Override
    public ResponseMap<Integer, List<Product>> SearchProductByKeyword(String userName, String keyword, Filter<Product> f) {
        return null;
    }

    @Override
    public Response AddToShoppingCart(String userID, int shopID, int productId, int amount) {
        return null;
    }

    @Override
    public Response EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return null;
    }

    @Override
    public Result<Boolean, List<String>> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return null;
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
        return null;
    }

    @Override
    public ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        return null;
    }

    @Override
    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) {
        return null;
    }

    @Override
    public Response RemoveProductFromShopInventory(int productId, String username, int shopname) {
        return null;
    }

    @Override
    public Response AppointNewShopOwner(int key, String targetUser, String userId) {
        return null;
    }

    @Override
    public Response AppointNewShopManager(int key, String targetUser, String userId) {
        return null;
    }

    @Override
    public Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        return null;
    }

    @Override
    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        return null;
    }

    @Override
    public Response CloseShop(int shopID, String username) {
        return null;
    }

    @Override
    public Response OpenShop(int shopId, String userName) {
        return null;
    }

    @Override
    public ResponseMap<Integer, List<Product>> SearchProductByCategory(String userName, String category, Filter<Product> f) {
        return null;
    }

    @Override
    public ResponseList<User> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName) {
        return null;
    }

    @Override
    public Result<Boolean, String> DeleteUserTest(String[] usernames) {
        return null;
    }

    @Override
    public ResponseList<Order> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userName) {
        return null;
    }

    @Override
    public Response DismissalUserBySystemManager(String usernames, String targetUser) {
        return null;
    }

    @Override
    public Response DismissalOwnerByOwner(String usernames, String targetUser, int shop) {
        return null;
    }

    @Override
    public ResponseList<User> RequestUserInfo(SearchUserFilter f, String userName) {
        return null;
    }

    @Override
    public Response CreateSystemManager(String systemManager, String username, String pw) {
        return null;
    }
}
