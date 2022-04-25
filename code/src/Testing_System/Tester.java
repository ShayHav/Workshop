package Testing_System;

import domain.shop.Product;
import domain.shop.PurchasePolicys.PurchaseRule;
import domain.shop.TransactionInfo;
import domain.shop.discount.Discount;

import java.util.List;
import java.util.Map;

public class Tester {
    private Bridge br;
    public Tester() {
        br = Selector.GetBridge();
    }

    //Guest-Visitor General
    public Result<Boolean,String> Login(String username, String pw)
    {
        return br.Login(username, pw);
    }

    public Result<Boolean,String> Register(String username, String pw)  { return br.Register(username, pw);  }

    public Result<Boolean,String> EnterMarket() { return br.EnterMarket(); }

    public Result<Boolean,String> LeaveMarket() { return br.LeaveMarket(); }

    //Guest-Visitor Purchase
    public Result<Boolean, String> GetProductInfo(Product p) {return br.GetProductInfo(p); }

    public Result<Boolean, String> SearchProducts(Product p) { return br.SearchProducts(p);}

    public Result<Boolean, String> AddToShoppingCart(Product p, String shopname, int amount) { return br.AddToShoppingCart(p, shopname, amount);}

    public Result<Boolean, String> EditShoppingCart(Product p, String shopname, int amount) { return br.EditShoppingCart(p, shopname, amount);}

    public Result<Boolean, String> Checkout() { return br.Checkout(); }//???

    public Result<Boolean, Integer> CalculatePriceForProduct(Product p, String shopname) { return br.CalculatePriceForProduct(p, shopname);}

    public Result<Boolean, Integer> CheckDiscountPolicyForProduct(Product p, String shopname) { return br.CheckDiscountPolicyForProduct(p,shopname);}

    public Result<Boolean, Integer> CheckDiscountForProduct(Product p, String shopname) { return br.CheckDiscountForProduct(p,shopname);}

    public Result<Boolean, String> CheckIfProductAvailable(Product p, String shopname) { return br.CheckIfProductAvailable(p, shopname);}

    //Shop-Owner Options
    public Result<Boolean, String> AddProductToShopInventory(Product p, String shopname) { return br.AddProductToShopInventory(p, shopname);}

    public Result<Boolean, String> RemoveProductToShopInventory(Product p, String shopname) { return br.RemoveProductToShopInventory(p, shopname);}

    public Result<Boolean, String> ChangeProductDetail(Product p, String shopname, Map<String,String> newinfo) { return br.ChangeProductDetail(p, shopname, newinfo);}

//    public Result<Boolean, String> AddBuyingShopPolicy(String shopname, PurchaseRule pr) { return br.AddBuyingShopPolicy(shopname,pr); }
//
//    public Result<Boolean, String> RemoveBuyingShopPolicy(String shopname) { return br.RemoveBuyingShopPolicy(shopname); }
//
//    public Result<Boolean, String> AddDiscountShopPolicy(String shopname, Discount discount) { return br.AddDiscountShopPolicy(shopname,discount); }
//
//    public Result<Boolean, String> RemoveDiscountShopPolicy(String shopname) { return br.RemoveDiscountShopPolicy(shopname); }
//
//    public Result<Boolean, String> AddBuyingProductPolicy(String shopname, PurchaseRule pr ) { return br.AddBuyingProductPolicy(shopname, pr); }
//
//    public Result<Boolean, String> RemoveBuyingProductPolicy(String shopname) { return br.RemoveBuyingProductPolicy(shopname);}

      public Result<Boolean, String> AppointNewShopOwner(String username) { return br.AppointNewShopOwner(username);}

     public Result<Boolean, String> AppointNewShopManager(String username, )


    //Member-Visitor General
    public Result<Boolean, String> Logout(String username)
    {
        return br.Logout(username);
    }

    public Result<Boolean, String> CreateShop(String username, String shopname) { return br.CreateShop(username, shopname);}



    //System Scnerios
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) { return br.RealTimeNotification(users, msg);}

    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti) /*supply*/ { return br.PurchaseDelivery(ti);}

    public Result<Boolean, String> Payment(TransactionInfo ti) { return br.Payment(ti); }

    public Result<Boolean, String>  StartMarket() { return br.StartMarket(); }

    public Result<Boolean, String> AddExternalService(String path) { return br.AddExternalService(path);}

    public Result<Boolean, String> RemoveExternalService(String path) { return br.RemoveExternalService(path);}











}
