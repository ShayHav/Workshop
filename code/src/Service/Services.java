package Service;


import Testing_System.Result;
import domain.shop.Product;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.ShopManagersPermissions;
import domain.shop.discount.Discount;
import domain.user.Filter;
import domain.user.TransactionInfo;

import java.util.List;
import java.util.Map;

public class Services {


    //General Guest-Visitor
    public Result<Boolean,Boolean> Login(String username, String pw){

    }

    public Result<Boolean,Boolean> Register(String username, String pw)
    {

    }

    public Result<Boolean,String> EnterMarket()
    {

    }

    public Result<Boolean,String> LeaveMarket()
    {

    }


    //General Member-Visitor
    public Result<Boolean,Boolean> Logout(String username)
    {

    }

    public Result<Boolean, String> CreateShop(String username, String shopname)
    {

    }


    //System
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg)
    {

    }

    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti, Map<Integer,Integer> products)
    {

    }//supply

    public Result<Boolean, String> Payment(TransactionInfo ti)
    {

    }

    public Result<Boolean, String> StartMarket()
    {

    }

    public Result<Boolean, String> AddSupplyService(String path)
    {

    }

    public Result<Boolean, String> RemoveSupplyService(String path)
    {

    }

    public Result<Boolean, String> AddPaymentService(String path)
    {

    }

    public Result<Boolean, String> RemovePaymentService(String path)
    {

    }

    //Guest-Visitor Shop options
    public Result<Boolean, String> GetShopsInfo()
    {

    }

    public Result<Boolean, String> GetProductInfoInShop(String shopname)
    {

    }//display information of a product?

    public Result<Boolean, String> SearchProductByName(String pName)
    {

    }

    public Result<Boolean, String> SearchProductByCategory(String pName)
    {

    }

    public Result<Boolean, String> SearchProductByKeyword(String keyword)
    {

    }

    public Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount)
    {

    }

    public Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount)
    {

    }

    public Result<Boolean, String> Checkout()
    {

    }

    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname)
    {

    }

    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname)
    {

    }

    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname)
    {

    }

    public Result<Boolean, String> CheckIfProductAvailable(Product p, String shopname)
    {

    }

    public Result<Boolean, Integer> AddProductToShopInventory(Product p, String usernmae,String shopname)
    {

    }

    public Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String, String> newinfo)
    {

    }

    public Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr)
    {

    }

    public Result<Boolean, String> RemoveBuyingShopPolicy(String shopname)
    {

    }

    public Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount)
    {

    }

    public Result<Boolean, String> RemoveDiscountShopPolicy(String shopname)
    {

    }

    public Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr)
    {

    }

    public Result<Boolean, String> RemoveBuyingProductPolicy(String shopname)
    {

    }

    public Result<Boolean, String> AppointNewShopOwner(String username)
    {

    }

    public Result<Boolean, String> AppointNewShopManager(String username)
    {

    }

    public Result<Boolean, String> AddShopMangerPermissions(String username, List<ShopManagersPermissions> permissions)
    {

    }

    public Result<Boolean, String> RemoveShopManagerPermissions(String username, List<ShopManagersPermissions> permissions)
    {

    }

    public Result<Boolean, String> CloseShop(String shopname)
    {

    }

    public Result<Boolean, String> RequestShopOfficialsInfo(String shopname, Filter f)
    {

    }

    public Result<Boolean, String> DeleteUserTest(String[] usernames)
    {

    }

    public Result<Boolean, String> RemoveProductFromShopInventory(int id, String username, String shopname)
    {

    }


}
