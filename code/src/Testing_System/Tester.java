package Testing_System;

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


    //Member-Visitor General
    public Result<Boolean, String> Logout(String username)
    {
        return br.Logout(username);
    }

    public Result<Boolean, String> CreateShop(String username, String shopname) { return br.CreateShop(username, shopname);}


    //System Scnerios
    public Result<Boolean, String> RealTimeNotification(List<String> users, String msg) { return br.RealTimeNotification(users, msg);}

    public Result<Boolean, String> PurchaseDelivery(TransactionInfo ti) /*supply*/ { return br.PurchaseDelivery(ti);}

    public Result<Boolean, String>  StartMarket() { return br.StartMarket(); }

    public Result<Boolean, String>  AddExternalService(String path) { return br.AddExternalService(path);}

    public Result<Boolean, String> RemoveExternalService(String path) { return br.RemoveExternalService(path);}







}
