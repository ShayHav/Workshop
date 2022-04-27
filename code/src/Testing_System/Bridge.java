package Testing_System;

import domain.market.PaymentService;
import domain.market.SupplyService;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.discount.Discount;
import domain.user.*;

import java.util.List;
import java.util.Map;

public interface Bridge {

    //General Guest-Visitor
    Result<Boolean, Boolean> Login(String username, String pw); //done

    Result<Boolean, Boolean> Register(String username, String pw); //done

    Result<Boolean, String> EnterMarket();

    Result<Boolean, String> LeaveMarket();


    //General Member-Visitor
    Result<Boolean, String> Logout(String username); //done

    Result<Boolean, Integer> CreateShop(String username, String shopname); //done


    //System
    Result<Boolean, String> RealTimeNotification(List<String> users, String msg);

    Result<Boolean, Boolean> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products); //supply

    Result<Boolean, Boolean> Payment(TransactionInfo ti);

    Result<Boolean, Boolean> StartMarket(PaymentService payment, SupplyService supply, String userID, String password); //done

    Result<Boolean, String> AddSupplyService(String path);

    Result<Boolean, String> RemoveSupplyService(String path);

    Result<Boolean, String> AddPaymentService(String path);

    Result<Boolean, String> RemovePaymentService(String path);

    //Guest-Visitor Shop option
    Result<Boolean, List<ShopInfo>> GetShopsInfo(String userID, Filter<ShopInfo> filter); //done

    Result<Boolean, List<ProductInfo>> GetProductInfoInShop(String userID ,int shopID, Filter<ProductInfo> f);

    Result<Boolean, List<ProductInfo>> SearchProductByName(String userID ,String pName, Filter<ProductInfo> f); //done

    Result<Boolean, List<ProductInfo>> SearchProductByKeyword(String userID ,String keyword, Filter<ProductInfo> f);    //done

    Result<Boolean, Boolean> AddToShoppingCart(String userID, int shopID,int productId, int amount);

    Result<Boolean, Boolean> EditShoppingCart(String userId, int shopId, int productId, int amount);

    Result<Boolean, List<String>> Checkout(String userID,String fullName, String address, String phoneNumber, String cardNumber, String expirationDate);

    Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname);

    Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname); //

    Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname);

    Result<Boolean, Boolean> CheckIfProductAvailable(Product p, int shopID);

    Result<Boolean, Product> AddProductToShopInventory(String pName, String pDis, String pCat, double price, int amount, String username, int shopID); //done

    Result<Boolean, Product> ChangeProduct(String username, Product p, int shopID);
    /*
    Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingShopPolicy(String shopname);

    Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount);

    Result<Boolean, String> RemoveDiscountShopPolicy(String shopname);

    Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingProductPolicy(String shopname);

     */

    Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, int shopname); //done

    Result<Boolean, String> AppointNewShopOwner(int key,String targetUser, String userId); //done

    Result<Boolean, String> AppointNewShopManager(int key,String targetUser, String userId);//done

    Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser , String ownerID);//done

    Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser, String ownerID); //done

    Result<Boolean, String> CloseShop(int shopID, String username);//done

    Result<Boolean, List<UserSearchInfo>> RequestShopOfficialsInfo(int shopName, SearchOfficialsFilter f, String userId);

    Result<Boolean, String> DeleteUserTest(String[] usernames);

    Result<Boolean, List<Order>> RequestShopSalesInfo(int shopName, SearchOrderFilter f, String userId);
}