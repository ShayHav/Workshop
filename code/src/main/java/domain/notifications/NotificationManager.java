/*package domain.notifications;

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
        ShopObserved shopObserved;
        if(shopObservedMap.containsKey(shopID)) {
             shopObserved = shopObservedMap.get(shopID);
            if (shopObserved != null)
                shopObserved.subscribe(observer);
        }
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
 */



package domain.notifications;

import domain.Exceptions.ShopNotFoundException;
import domain.shop.ShopController;
import domain.user.User;
import domain.user.UserController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    private static NotificationManager single_instance = null;
    private Map<User, UserObserver> userSocketChannelMap;
    private Map<User, List<String>> userListMap;



    public static NotificationManager getInstance(){
        if(single_instance == null){
            single_instance = new NotificationManager();
        }
        return single_instance;
    }

    private NotificationManager() {
        userListMap = new HashMap<>();
        userSocketChannelMap = new HashMap<>();
    }

    public synchronized void newSocketChannel (User user,UserObserver userObserver){
        userSocketChannelMap.putIfAbsent(user,userObserver);
        getMessage(user);
    }
    public synchronized void disConnected(User user){
        userSocketChannelMap.remove(user);
    }
    public synchronized void setMessage(User forUser,String message) {
        if (userSocketChannelMap.containsKey(forUser)) {
            userSocketChannelMap.get(forUser).notify(message);
        } else if (!userListMap.containsKey(forUser)) {
            List<String> messages = new LinkedList<>();
            messages.add(message);
            userListMap.put(forUser, messages);
        } else userListMap.get(forUser).add(message);
    }

    public synchronized void getMessage (User user){
        if (!userSocketChannelMap.containsKey(user))
            return;
        else {
            if (!userListMap.containsKey(user))
                return;
            UserObserver userObserver = userSocketChannelMap.get(user);
            List<String> messages = userListMap.get(user);
            messages.forEach((s) -> {
                userObserver.notify(s);
            });
        }
    }

    public void systeManagerMessage(String message) {
        UserController.getInstance().getAdminUser().forEach((u)->{setMessage(u,message);});
    }

    public void shopOwnerMessage(int shop, String message){
        try {
            ShopController.getInstance().getShop(shop).getShopOwners().forEach((u) -> {
                setMessage(u, message);
            });
        }
        catch (ShopNotFoundException shopNotFoundException){
            return;
        }
    }
}