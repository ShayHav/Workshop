package Testing_System;

import domain.shop.Product;
import domain.shop.TransactionInfo;

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

    Result<Boolean, String> GetProductInfo(Product p); //display information of a product?

    Result<Boolean, String> SearchProducts(Product p);

    Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount);

    Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount);

    Result<Boolean, String> Checkout();

    Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname);

    Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname); //

    Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname);

    Result<Boolean, String> CheckIfProductAvailable(Product p, String shopname);

    Result<Boolean, String> AddProductToShopInventory(Product p, String shopname);

    Result<Boolean, String> RemoveProductToShopInventory(Product p, String shopname);

    Result<Boolean, String> ChangeProductDetails(Product p, String shopname);

    Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String, String> newinfo);
}
