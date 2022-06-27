package Presentation.Model;

import domain.shop.PurchaseFormats.BidFormat;

import java.util.HashMap;
import java.util.Map;

public class PresentationBid {

    private int serialNumber;
    private String productName;
    private int bidID;
    private int quantity;
    private double price;
    private PresentationUser requestedUser;
    private Map<String, Boolean> status;
    private boolean completed;
    private int shopID;

    public PresentationBid(BidFormat bid){
        this.serialNumber = bid.getId();
        this.quantity = bid.getAmount();
        this.price = bid.getPrice();
        this.bidID = bid.getBidID();
        this.productName = bid.getName();
        this.requestedUser = new PresentationUser(bid.getBuyer());
        this.status = new HashMap<>();
        bid.getToConfirm().forEach((user, approved) -> status.put(user.getUserName(), approved));
        this.completed = bid.isCompleted();
        this.shopID = bid.getShop().getShopID();
    }

    public double getPrice() {
        return price;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean didApproved(PresentationUser user){
        return status.getOrDefault(user.getUsername(), false);
    }

    public int getStatus(){
        if(completed)
            return 100;
        int totalNumber = status.size();
        int approved = 0;
        for(Boolean b  : status.values()){
            if(b)
                approved++;
        }
        return (approved / totalNumber) * 100;
    }

    public int getBidID() {
        return bidID;
    }

    public String getProductName() {
        return productName;
    }

    public PresentationUser getRequestedUser() {
        return requestedUser;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getShopID() {
        return shopID;
    }
}
