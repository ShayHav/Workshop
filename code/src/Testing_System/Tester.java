package Testing_System;

import domain.shop.Product;
import domain.shop.TransactionInfo;

import java.util.List;

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

    public



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
