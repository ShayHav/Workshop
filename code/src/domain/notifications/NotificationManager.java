package domain.notifications;

import domain.ErrorLoggerSingleton;
import domain.Exceptions.ShopNotFoundException;
import domain.shop.ShopController;
import domain.user.User;
import domain.user.UserController;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class NotificationManager {
    private static NotificationManager single_instance = null;
    private Map<Integer,ShopObserved> shopObservedMap;
    private SystemManagerObserved systemManagerObserved;


    public static NotificationManager getInstance(){
        if(single_instance == null){
            single_instance = new NotificationManager();
        }
        return single_instance;
    }

    private NotificationManager() {
        shopObservedMap = new HashMap<>();
        systemManagerObserved = new SystemManagerObserved();
    }

    public synchronized void subscribeShop(Observer observer,int shopID){
        ShopObserved shopObserved = shopObservedMap.get(shopID);
        if(shopObserved!=null)
            shopObserved.subscribe(observer);
        else {
            shopObserved = new ShopObserved(shopID);
            shopObserved.subscribe(observer);
            shopObservedMap.putIfAbsent(shopID,shopObserved);
        }
    }
    public synchronized void unsubscribeShop(Observer observer,int shopID){
        ShopObserved shopObserved = shopObservedMap.get(shopID);
        if(shopObserved!=null)
            shopObserved.unsubscribe(observer);
    }
    public synchronized void setMessageShop(int shopID,String message) {
        ShopObserved shopObserved = shopObservedMap.get(shopID);
        if(shopObserved!=null)
            shopObserved.notifyAllObservers(message);
    }
    public synchronized void subscribeSystemManager(Observer observer){
        systemManagerObserved.subscribe(observer);
    }
    public synchronized void setMessageSystemManager(String message) {
        systemManagerObserved.notifyAllObservers(message);
    }
}
