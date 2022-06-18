package Testing_System;

import Service.Services;
import domain.Responses.Response;
import domain.Responses.ResponseList;
import domain.Responses.ResponseMap;
import domain.Responses.ResponseT;
import domain.ExternalConnectors.PaymentService;
import domain.ExternalConnectors.SupplyService;
import domain.shop.*;
import domain.user.*;
import domain.user.TransactionInfo;
import domain.user.filter.Filter;
import domain.user.filter.SearchOfficialsFilter;
import domain.user.filter.SearchOrderFilter;
import domain.user.filter.SearchUserFilter;

import java.util.List;
import java.util.Map;

public class RealBridge implements Bridge{

    private Services sv;
    public RealBridge()
    {
        sv = Services.getInstance();
    }

    @Override
    public ResponseT<User> Login(String guest, String username, String pw) {
        return sv.Login(guest, username, pw);
    }

    @Override
    public Response Register(String guest, String username, String pw) {
        return sv.Register(guest,username, pw);
    }

    @Override
    public ResponseT<User> EnterMarket() {
        return sv.EnterMarket();
    }

    @Override
    public Response LeaveMarket(String username) {
        return sv.LeaveMarket(username);
    }

    @Override
    public ResponseT<User> Logout(String username) {
        return sv.Logout(username);
    }

    @Override
    public ResponseT<Shop> CreateShop(String dis, String username, String shopname) {
        return sv.CreateShop(dis, username, shopname);
    }

    @Override
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) {
        return null;
    }

    @Override
    public ResponseT<Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer, Integer> products) {
        return sv.PurchaseDelivery(ti, products);
    }

    @Override
    public ResponseT<Boolean> Payment(TransactionInfo ti) {
        return sv.Payment(ti);
    }

    @Override
    public Response StartMarket(PaymentService payment, SupplyService supply) {
        return sv.StartMarket(payment, supply);
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
        return sv.GetShopsInfo(userName, filter);
    }

    @Override
    public ResponseList<Product> GetProductInfoInShop(String userName, int shopID, Filter<Product> f) {
        return sv.GetProductInfoInShop(userName, shopID, f);
    }

    @Override
    public ResponseMap<Integer,List<Product>> SearchProductByName(String userName, String pName, Filter<Product> f) {
        return sv.SearchProductByName(userName, pName, f);
    }


    @Override
    public ResponseMap<Integer,List<Product>> SearchProductByKeyword(String userName, String keyword, Filter<Product> f) {
        return  sv.SearchProductByKeyword(userName, keyword, f);
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
       return (Result<Boolean, List<String>>) sv.Checkout(userID, fullName, address, phoneNumber, cardNumber, expirationDate).getValue();
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
    public ResponseT<Product> AddProductToShopInventory(int serialNumber, String pName, String pDis, String pCat, double price, int amount, String username, int shopID)
    {
        return sv.AddProductToShopInventory(serialNumber, pName, pDis, pCat, price, amount, username, shopID);
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
    public Response AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser, String ownerID) {
        return sv.AddShopMangerPermissions(key, shopManagersPermissionsList, targetUser, ownerID);
    }

    @Override
    public Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID) {
        return sv.RemoveShopManagerPermissions(key, shopManagersPermissionsList, managerUser, ownerID);
    }

    @Override
    public Response CloseShop(int shopID, String username) {
        return sv.closeShop(shopID, username);
    }

    @Override
    public Response OpenShop(int shopId, String userName) {
        return sv.reopenShop(shopId,userName);
    }

    @Override
    public ResponseMap<Integer, List<Product>> SearchProductByCategory(String userName, String category, Filter<Product> f) {
        return sv.SearchProductByCategory(userName,category,f);
    }

    @Override
    public ResponseList<User> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userName) {
        return sv.RequestShopOfficialsInfo(shopName, f, userName);
    }

    @Override
    public Response DeleteUserTest(String[] usernames) {
        return sv.DeleteUserTest(usernames);
    }

    @Override
    public ResponseList<Order> RequestInformationOfShopsSalesHistory(int shopName, SearchOrderFilter f, String userName) {
        return sv.RequestInformationOfShopsSalesHistory(shopName, f, userName);
    }

    @Override
    public Response DismissalUserBySystemManager(String usernames,String targetUser) {
        return sv.DismissalUserBySystemManager(usernames,targetUser);
    }

    @Override
    public Response DismissalOwnerByOwner(String usernames,String targetUser,int shop) {
     return sv.DismissalOwnerByOwner(usernames, targetUser, shop);
    }

    @Override
    public ResponseList<User> RequestUserInfo(SearchUserFilter f, String userName)
    {
        return sv.RequestUserInfo(f, userName);
    }

    @Override
    public Response CreateSystemManager(String systemManager,String username, String pw) {
        return sv.CreateSystemManager(systemManager, username, pw);
    }


}
