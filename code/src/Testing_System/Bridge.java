package Testing_System;

import domain.shop.TransactionInfo;

import java.util.List;

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

    Result<Boolean, String> PurchaseDelivery(TransactionInfo ti);

    Result<Boolean, String> StartMarket();

    Result<Boolean, String> AddExternalService(String path);

    Result<Boolean, String> RemoveExternalService(String path);
}
