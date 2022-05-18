package Testing_System;

import Presentation.Model.PresentationShop;
import Presentation.Model.PresentationUser;
import domain.Response;
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

import java.util.List;
import java.util.Map;

public interface Bridge {

    //General Guest-Visitor
    ResponseT<PresentationUser> Login(String username, String pw); //done

    Response Register(String username, String pw, UserObserver uo); //done

    ResponseT<User> EnterMarket();

    Response LeaveMarket();

    //General Member-Visitor
    ResponseT<User> Logout(String username); //done

    ResponseT<PresentationShop> CreateShop(String dis, String username, String shopname); //done


    //System
    Result<Boolean, String> RealTimeNotification(List<String> users, String msg);

    Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products); //supply

    Result<Boolean, Boolean> Payment(TransactionInfo ti);

    Response StartMarket(PaymentService payment, SupplyService supply, String userID, String password); //done

    Result<Boolean, String> AddSupplyService(String path);

    Result<Boolean, String> RemoveSupplyService(String path);

    Result<Boolean, String> AddPaymentService(String path);

    Result<Boolean, String> RemovePaymentService(String path);

    //Guest-Visitor Shop option
    Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<Shop> filter); //done

    Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<Product> f);

    Result<Boolean, List<ProductInfo>> SearchProductByName(String userID ,String pName, Filter<Product> f); //done

    Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID ,String keyword, Filter<Product> f);    //done

    Response AddToShoppingCart(String userID, int shopID, int productId, int amount);

    Response EditShoppingCart(String userId, int shopId, int productId, int amount);

    Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);

    Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname);

    Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname); //

    Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname);

    ResponseT<Boolean> CheckIfProductAvailable(Product p, int shopID);

    ResponseT<Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID); //done

    ResponseT<Product> ChangeProduct(String username, Product p, int shopID);
    /*
    Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingShopPolicy(String shopname);

    Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount);

    Result<Boolean, String> RemoveDiscountShopPolicy(String shopname);

    Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingProductPolicy(String shopname);

     */

    Response RemoveProductFromShopInventory(int productId, String username, int shopname); //done

    Response AppointNewShopOwner(int key, String targetUser, String userId); //done

    Response AppointNewShopManager(int key, String targetUser, String userId);//done

    Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID);//done

    Response RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID); //done

    Response CloseShop(int shopID, String username);//done

    Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userId);

    Result<Boolean, String> DeleteUserTest(String[] usernames);

    Result<Boolean, List<Order>> RequestShopSalesInfo(int shopName, SearchOrderFilter f, String userId);
}