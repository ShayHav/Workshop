package domain.notifications;

import domain.Exceptions.ShopNotFoundException;
import domain.shop.ShopController;
import domain.user.User;
import domain.user.UserController;
import java.util.*;

public class NotificationManager {
    private static NotificationManager single_instance = null;
    private final Map<String, List<UserObserver>> observers;
    private final Map<User, List<Message>> userMessages;

    public static NotificationManager getInstance(){
        if(single_instance == null){
            single_instance = new NotificationManager();
        }
        return single_instance;
    }

    private NotificationManager() {
        userMessages = new HashMap<>();
        observers = new HashMap<>();
    }

    public synchronized void removeFromObserversList(String user){
        observers.remove(user);
    }

    public synchronized void sendMessage(User addressee,String content, User sender) {
        Message message = new Message(sender, addressee, content);
        userMessages.putIfAbsent(addressee, new ArrayList<>());
        userMessages.get(addressee).add(message);
        if(observers.containsKey(addressee.getUserName()))
            observers.get(addressee.getUserName()).forEach(observer -> observer.notify(message));
    }

    public void registerObserver(User user, UserObserver observer){
        String username = user.getUserName();
        if(!observers.containsKey(username)){
            observers.put(username, new ArrayList<>());
        }
        observers.get(username).add(observer);
        userMessages.putIfAbsent(user, new ArrayList<>());
        List<Message> messages = userMessages.get(user);
        messages.forEach(observer::notify);
    }
}