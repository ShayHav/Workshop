package Testing_System;

import domain.shop.Product;
import domain.shop.ProductInfo;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.ShopManagersPermissions;
import domain.shop.discount.Discount;
import domain.user.Filter;
import domain.user.TransactionInfo;

import java.util.List;
import java.util.Map;

public interface Bridge {

    //General Guest-Visitor
    Result<Boolean,Boolean> Login(String username, String pw); //done

    Result<Boolean,Boolean> Register(String username, String pw); //done

    Result<Boolean,String> EnterMarket();

    Result<Boolean,String> LeaveMarket();


    //General Member-Visitor
    Result<Boolean,String> Logout(String username);

    Result<Boolean, Integer> CreateShop(String username, String shopname);


    //System
    Result<Boolean, String> RealTimeNotification(List<String> users, String msg);

    Result<Boolean, String> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products); //supply

    Result<Boolean, String> Payment(TransactionInfo ti);

    Result<Boolean, String> StartMarket();

    Result<Boolean, String> AddSupplyService(String path);

    Result<Boolean, String> RemoveSupplyService(String path);

    Result<Boolean, String> AddPaymentService(String path);

    Result<Boolean, String> RemovePaymentService(String path);

    //Guest-Visitor Shop options
    Result<Boolean, String> GetShopsInfo();

    Result<Boolean, List<ProductInfo>> GetProductInfoInShop(int shopname, Filter<ProductInfo> f); //display information of a product?

    Result<Boolean, String> SearchProductByName(String pName);

    Result<Boolean, String> SearchProductByCategory(String pName);

    Result<Boolean, String> SearchProductByKeyword(String keyword);

    Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount);

    Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount);

    Result<Boolean, String> Checkout();

    Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname);

    Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname); //

    Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname);

    Result<Boolean, String> CheckIfProductAvailable(Product p, String shopname);

    Result<Boolean, Integer> AddProductToShopInventory(Product p, String usernmae,String shopname);
    
    Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String, String> newinfo);

    /*
    Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingShopPolicy(String shopname);

    Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount);

    Result<Boolean, String> RemoveDiscountShopPolicy(String shopname);

    Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingProductPolicy(String shopname);

     */

    Result<Boolean, String> AppointNewShopOwner(int key,String targetUser, String userId);

    Result<Boolean, String> AppointNewShopManager(int key,String targetUser, String userId);

    Result<Boolean, String> AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser , String ownerID);

    Result<Boolean, String> RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String managerUser , String ownerID);

    Result<Boolean, String> CloseShop(int shopID, String username);

    Result<Boolean, String> RequestShopOfficialsInfo(String shopName, Filter f);

    Result<Boolean, String> DeleteUserTest(String[] usernames);

    Result<Boolean, Integer> RemoveProductFromShopInventory(int productId, String username, String shopName);
}
