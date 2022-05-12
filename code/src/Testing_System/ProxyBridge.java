package Testing_System;

import domain.Response;
import domain.ResponseT;
import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;

import java.util.List;
import java.util.Map;

public class ProxyBridge implements  Bridge {
    @Override
    public ResponseT<User> Login(String username, String pw) {
        return new ResponseT();
    }

    @Override
    public Response Register(String username, String pw) {
        return new ResponseT();
    }

    @Override
    public ResponseT<User> EnterMarket() {
        return new ResponseT();
    }

    @Override
    public Response LeaveMarket() {
        return new ResponseT();
    }

    @Override
    public ResponseT<User> Logout(String username) {
        return new ResponseT();
    }

    @Override
    public ResponseT<Shop> CreateShop(String username, String shopname) {
        return new ResponseT();
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return new Result<Boolean, String>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        return new Result<Boolean, Boolean>(true,null);
    }

    @Override
    public Result<Boolean, Boolean> Payment(TransactionInfo ti) {
        return new Result(true,null);
    }

    @Override
    public Response StartMarket(PaymentService payment, SupplyService supply, String userID, String password) {
        return new ResponseT();
    }

    @Override
    public Result<Boolean, String> AddSupplyService(String path) {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, String> RemoveSupplyService(String path) {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, String> AddPaymentService(String path) {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, String> RemovePaymentService(String path) {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter) {
        {
            return new Result(true,null);
        }
    }

    @Override
    public Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID, int shopID, Filter<ProductInfo> f) {
        {
            return new Result(true,null);
        }
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByName(String userID, String pName, Filter<ProductInfo> f) {
        {
            return new Result(true,null);
        }
    }

    @Override
    public Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID, String keyword, Filter<ProductInfo> f) {
        {
            return new Result(true,null);
        }
    }

    @Override
    public Response AddToShoppingCart(String userID, int shopID, int productId, int amount) {
        return new ResponseT();
    }

    @Override
    public Response EditShoppingCart(String userId, int shopId, int productId, int amount) {
        return new ResponseT();
    }

    @Override
    public Result<Boolean, List<String>> Checkout(String userID, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate)  {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname)  {
        return new Result(true,null);
    }

    @Override
    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname)  {
        return new Result(true,null);
    }
    @Override
    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname)  {
        return new Result(true,null);
    }
    @Override
    public ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID) {
        return new ResponseT();
    }

    @Override
    public ResponseT<Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID) {
        return new ResponseT();
    }

    @Override
    public ResponseT<Product> ChangeProduct(String username, Product p, int shopID) {
        return new ResponseT();
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
