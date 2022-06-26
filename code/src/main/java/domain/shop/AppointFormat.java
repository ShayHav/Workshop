package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.notifications.NotificationManager;
import domain.user.Role;
import domain.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class AppointFormat {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    Map<User, Boolean> toConfirm;
    User userToAppoint;
    User appointUser;
    Shop shop;
    int id;

    public AppointFormat(User userToAppoint, User appointUser, Shop shop , List<User> toConfirm,int id) throws BidNotFoundException, IncorrectIdentification, InvalidSequenceOperationsExc, CriticalInvariantException, BlankDataExc {
        //super(product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getBasePrice(), product.getAmount());
        this.toConfirm = new HashMap<>();
        this.userToAppoint =  userToAppoint;
        this.appointUser = appointUser;
        this.shop = shop;
        this.id = id;
        if(toConfirm.size()>0) {
            NotificationManager notificationManager = NotificationManager.getInstance();

            String offerMessage = String.format("Appoint message: Shop: %d Owner:%s start Appoint process userToAppoint : %s",shop.getShopID(),appointUser.getUserName(),userToAppoint.getUserName());

            for (User user : toConfirm) {
                this.toConfirm.put(user, false);
                notificationManager.sendMessage(user, offerMessage, appointUser);
            }
        }
        else resolve();
    }

    public synchronized void approve(User user) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        if(!checkIfUserOnConfirmList(user))
            return;
        toConfirm.put(user, true);
        attemptToResolveBid(user);
    }

    public synchronized void attemptToResolveBid(User approver) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        for (Boolean confirmed: toConfirm.values()){
            if(!confirmed)
                return;
        }
        resolve();
    }

    public void resolve() throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        userToAppoint.AppointedMeOwner(shop, userToAppoint.getUserName());
        shop.putIfAbsentOwner(userToAppoint.getUserName(), userToAppoint);
        userToAppoint.addRole(shop.getShopID(), Role.ShopOwner);
        eventLogger.logMsg(Level.INFO, String.format("Appoint New Shop Owner User: %s", userToAppoint.getUserName()));
        return;
    }

    public synchronized void decline(User decliner){
        NotificationManager notificationManager = NotificationManager.getInstance();
        String declinedMessageBuyer = String.format("Dear Owner your request to appoint user: %s as ShopOwner decline", userToAppoint.getUserName());
        notificationManager.sendMessage(appointUser, declinedMessageBuyer, decliner);

        String declinedMessageOwners = String.format("Appoint message is not relevant anymore Appoint message: Shop: %d Owner:%s start Appoint process userToAppoint : %s",shop.getShopID(),appointUser.getUserName(),userToAppoint.getUserName());
        //String declinedMessageOwners = String.format();
        for(User user: toConfirm.keySet()) {
            notificationManager.sendMessage(user, declinedMessageOwners, appointUser);
        }
    }

    public boolean checkIfUserOnConfirmList(User user){
        return toConfirm.get(user) != null;
    }

    public int getId() {
        return id;
    }
}
