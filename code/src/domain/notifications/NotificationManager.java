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
    private Map<User, SocketChannel> userSocketChannelMap;
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

    public synchronized void newSocketChannel (User user,SocketChannel socketChannel){
        userSocketChannelMap.putIfAbsent(user,socketChannel);
    }
    public synchronized void setMessage(User forUser,String message) {
        if (userSocketChannelMap.containsKey(forUser)) {
            userSocketChannelMap.get(forUser).send(message);
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
                SocketChannel socketChannel = userSocketChannelMap.get(user);
                List<String> messages = userListMap.get(user);
                messages.forEach((s) -> {
                    socketChannel.send(s);
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
