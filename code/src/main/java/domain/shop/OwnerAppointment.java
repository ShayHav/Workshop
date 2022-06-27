package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.notifications.NotificationManager;
import domain.user.Role;
import domain.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class OwnerAppointment {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    Map<User, Boolean> OwnersToApprove;
    User userToAppoint;
    User appointUser;
    Shop shop;
    int id;
    boolean completed;

    public OwnerAppointment(User userToAppoint, User appointUser, Shop shop , List<User> toConfirm, int id) throws BidNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc, CriticalInvariantException, BlankDataExc {
        //super(product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getBasePrice(), product.getAmount());
        this.OwnersToApprove = new HashMap<>();
        this.userToAppoint =  userToAppoint;
        this.appointUser = appointUser;
        this.shop = shop;
        this.id = id;
        NotificationManager notificationManager = NotificationManager.getInstance();

        String offerMessage = String.format("Appoint message: Shop: %d Owner:%s start Appoint process userToAppoint : %s",shop.getShopID(),appointUser.getUserName(),userToAppoint.getUserName());
        completed = false;
        for(User user: toConfirm) {
            if(!user.equals(appointUser)) {
                this.OwnersToApprove.put(user, false);
                notificationManager.sendMessage(user, offerMessage, appointUser);
            }
        }
        if(toConfirm.size() == 1){
            resolve();
        }
    }

    public synchronized void approve(User user) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if(!checkIfUserOnConfirmList(user))
            return;
        OwnersToApprove.put(user, true);
        attemptToResolveBid(user);
    }

    public synchronized void attemptToResolveBid(User approver) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        for (Boolean confirmed: OwnersToApprove.values()){
            if(!confirmed)
                return;
        }
        resolve();
    }

    public void resolve() throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        appointUser.AppointedMeOwner(shop,userToAppoint.getUserName());
        shop.addUserAsOwner(userToAppoint);
        completed = true;
        eventLogger.logMsg(Level.INFO, String.format("Appoint New ShopManager User: %s", userToAppoint.getUserName()));
    }

    public synchronized void decline(User decliner){
        NotificationManager notificationManager = NotificationManager.getInstance();
        String declinedMessageBuyer = String.format("Dear Owner your request to appoint user: %s as ShopManager decline", userToAppoint.getUserName());
        notificationManager.sendMessage(appointUser, declinedMessageBuyer, decliner);

        String declinedMessageOwners = String.format("Appoint message is not relevant anymore Appoint message: Shop: %d Owner:%s start Appoint process userToAppoint : %s",shop.getShopID(),appointUser.getUserName(),userToAppoint.getUserName());
        //String declinedMessageOwners = String.format();
        for(User user: OwnersToApprove.keySet()) {
            notificationManager.sendMessage(user, declinedMessageOwners, appointUser);
        }
    }

    public boolean checkIfUserOnConfirmList(User user){
        return OwnersToApprove.get(user) != null;
    }

    public int getId() {
        return id;
    }

    public User getUserToAppoint() {
        return userToAppoint;
    }

    public User getAppointUser() {
        return appointUser;
    }

    public Map<User, Boolean> getOwnersToApprove() {
        return Collections.unmodifiableMap(OwnersToApprove);
    }

    public boolean isCompleted() {
        return completed;
    }
}
