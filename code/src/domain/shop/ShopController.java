package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.User;
import domain.user.UserController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShopController {
    private Map<Integer, Shop> shopList;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private int shopCounter = 0;


    public ShopController(){
        shopList = new HashMap<>();
    }

    public void crearteShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, int id) {
        if(isUniqueName(name)) {
            shopCounter++;
            Shop newShop = new Shop(name, discountPolicy, purchasePolicy, id,shopCounter);
            shopList.put(shopCounter,newShop);
            eventLogger.logMsg(Level.INFO,String.format("create new shop. FounderId: %d , ShopName: %s",id,name));
        }
        errorLogger.logMsg(Level.WARNING,String.format("attempt to create a shop with exist name. id: %d , name: %s",id,name));
    }

    private boolean isUniqueName(String name) {
        for (Map.Entry<Integer, Shop> entry : shopList.entrySet()){
           if(entry.getValue().getName().equals(name))
               return false;
        }
        return true;
    }
}
