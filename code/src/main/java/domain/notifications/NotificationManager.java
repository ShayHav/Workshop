package domain.notifications;

import domain.shop.user.User;

import java.util.*;

public class NotificationManager {
    private static NotificationManager single_instance = null;
    private final Map<String, List<UserObserver>> observers;
    private final Map<User, List<Message>> userMessages;
    private final Map<User, List<AdminObserver>> adminObservers;

    public static NotificationManager getInstance(){
        if(single_instance == null){
            single_instance = new NotificationManager();
        }
        return single_instance;
    }

    private NotificationManager() {
        userMessages = new HashMap<>();
        observers = new HashMap<>();
        adminObservers = new HashMap<>();
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

    public void registerAdminObserver(User user, AdminObserver adminObserver){
        if(!adminObservers.containsKey(user)){
            adminObservers.put(user, new ArrayList<>());
        }
        adminObservers.get(user).add(adminObserver);
    }

    public synchronized void notifyAdmin(){
        for (User admin: adminObservers.keySet()) {
            if(admin.isLoggedIn()){
                adminObservers.get(admin).forEach(AdminObserver::notifyAdmin);
            }
        }
    }

    public long getNumberOfUnreadMessage(User addressee){
        if(userMessages.containsKey(addressee)){
            return userMessages.get(addressee).stream().filter(message -> !message.isRead()).count();
        }
        return 0;
    }

    public Map<String, List<UserObserver>> getObservers() {
        return observers;
    }

    public Map<User, List<AdminObserver>> getAdminObservers() {
        return adminObservers;
    }

    public Map<User, List<Message>> getUserMessages() {
        return userMessages;
    }

    public List<Message> getUserMessages(String username){
        for (User u: userMessages.keySet()) {
            if(u.getUserName().equals(username))
                return userMessages.get(u);
        }
        return null;
    }
}