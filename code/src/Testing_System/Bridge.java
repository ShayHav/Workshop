package Testing_System;

import domain.shop.Product;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.ShopManagersPermissions;
import domain.shop.TransactionInfo;
import domain.shop.discount.Discount;
import domain.user.Filter;

import java.util.List;
import java.util.Map;

public interface Bridge {

    //General Guest-Visitor
    Result<Boolean,String> Login(String username, String pw);

    Result<Boolean,String> Register(String username, String pw);

    Result<Boolean,String> EnterMarket();

    Result<Boolean,String> LeaveMarket();


    //General Member-Visitor
    Result<Boolean,String> Logout(String username);

    Result<Boolean, String> CreateShop(String username, String shopname);


    //System
    Result<Boolean, String> RealTimeNotification(List<String> users, String msg);

    Result<Boolean, String> PurchaseDelivery(TransactionInfo ti); //supply

    Result<Boolean, String> Payment(TransactionInfo ti);

    Result<Boolean, String> StartMarket();

    Result<Boolean, String> AddExternalService(String path);

    Result<Boolean, String> RemoveExternalService(String path);

    //Guest-Visitor Shop options
    Result<Boolean, String> GetShopsInfo();

    Result<Boolean, String> GetProductInfoInShop(String shopname); //display information of a product?

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

    Result<Boolean, String> AddProductToShopInventory(Product p, String shopname);

    Result<Boolean, String> RemoveProductToShopInventory(Product p, String shopname);

    Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String, String> newinfo);

    Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingShopPolicy(String shopname);

    Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount);

    Result<Boolean, String> RemoveDiscountShopPolicy(String shopname);

    Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr);

    Result<Boolean, String> RemoveBuyingProductPolicy(String shopname);

    Result<Boolean, String> AppointNewShopOwner(String username);

    Result<Boolean, String> AppointNewShopManager(String username);

    Result<Boolean, String> AddShopMangerPermissions(String username, List<ShopManagersPermissions> permissions);

    Result<Boolean, String> RemoveShopManagerPermissions(String username, List<ShopManagersPermissions> permissions);

    Result<Boolean, String> CloseShop(String shopname);

    Result<Boolean, String> RequestShopOfficialsInfo(String shopname, Filter f);

    Result<Boolean, String> DeleteUserTest(String[] usernames);
}
