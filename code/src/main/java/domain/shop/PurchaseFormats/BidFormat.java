package domain.shop.PurchaseFormats;

import domain.Exceptions.BidNotFoundException;
import domain.Exceptions.CriticalInvariantException;
import domain.notifications.NotificationManager;
import domain.shop.ProductImp;
import domain.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidFormat extends ProductImp{
    Map<User, Boolean> toConfirm;
    User buyer;
    Shop shop;
    int bidID;

    //public ProductImp(int id, String name, String description, String category, double basePrice, int quantity){

    public BidFormat(ProductImp product, List<User> toConfirm, User buyer, int bidID, double basePrice, Shop shop){
        super(product.getId(), product.getName(), product.getDescription(), product.getCategory(), product.getBasePrice(), product.getAmount());
        this.toConfirm = new HashMap<>();
        this.buyer =  buyer;
        this.bidID = bidID;
        this.shop = shop;
        NotificationManager notificationManager = NotificationManager.getInstance();

        String offerMessage = String.format("an offer of %f was made for %d product %s (serial number %d) that has the original price %f", product.getPrice(), product.getAmount(), product.getName(), product.getId(), basePrice);

        for(User user: toConfirm) {
            this.toConfirm.put(user, false);
            notificationManager.sendMessage(user, offerMessage, buyer);
        }
    }

    public synchronized void approve(User user) throws BidNotFoundException, CriticalInvariantException {
        if(!checkIfUserOnConfirmList(user))
            return;
        toConfirm.put(user, true);
        attemptToResolveBid(user);
    }

    public synchronized void attemptToResolveBid(User approver) throws BidNotFoundException, CriticalInvariantException {
        for (Boolean confirmed: toConfirm.values()){
            if(!confirmed)
                return;
        }
        resolveBid(approver);
    }

    public void resolveBid(User approver) throws BidNotFoundException, CriticalInvariantException {
        NotificationManager notificationManager = NotificationManager.getInstance();
        String acceptedMessage = String.format("Dear customer, your offer of %f for (each), %d of product %s (serial number: %d) has been accepted", getPrice(), getAmount(), getName(), getId());
        notificationManager.sendMessage(buyer, acceptedMessage, approver);
        buyer.bidApproved(shop.getShopID(), bidID);
    }

    public synchronized void decline(User decliner){
        NotificationManager notificationManager = NotificationManager.getInstance();
        String declinedMessageBuyer = String.format("Dear customer, your offer of %f for (each) %d of product %s (serial number: %d) has been declined", getPrice(), getAmount(), getName(), getId());
        notificationManager.sendMessage(buyer, declinedMessageBuyer, decliner);

        String declinedMessageOwners = String.format("Dear Owner, the offer customer %s of %f for (each) %d of product %s (serial number: %d) has been declined", buyer.getUserName() ,getPrice(), getAmount(), getName(), getId());
        for(User user: toConfirm.keySet()) {
            notificationManager.sendMessage(user, declinedMessageOwners, buyer);
        }

        shop.removeBid(bidID);
        try {
            buyer.removeBid(shop.getShopID(), bidID);
        } catch (BidNotFoundException | CriticalInvariantException e) {
            return;
        }
    }

    public boolean checkIfUserOnConfirmList(User user){
        return toConfirm.get(user) != null;
    }

    public double getBidPrice(){
        return getPrice();
    }

    public int getBidID(){return bidID;}
}
