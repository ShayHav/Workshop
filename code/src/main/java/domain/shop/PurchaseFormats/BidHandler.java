package domain.shop.PurchaseFormats;

import domain.Exceptions.BidNotFoundException;
import domain.Exceptions.CriticalInvariantException;
import domain.shop.ProductImp;
import domain.shop.PurchaseFormats.BidFormat;
import domain.shop.Shop;
import domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class BidHandler {
    private int bidIDCounter;
    private List<BidFormat> bids;

    public BidHandler(){
        bids = new ArrayList<>();
    }

    public int addNewBid(ProductImp product, List<User> neededToApprove, User buyer, double basePrice, Shop shop){
        BidFormat newBid = new BidFormat(product, neededToApprove, buyer, bidIDCounter++, basePrice, shop);
        synchronized (bids) {
            bids.add(newBid);
        }
        return newBid.getBidID();
    }

    public boolean removeBid(int bidID){
        synchronized (bids) {
            for (BidFormat bid : bids) {
                if (bid.getBidID() == bidID)
                    return bids.remove(bid);
            }
        }
        return false;
    }

    public double getBidBasePrice(int bidID){
        for(BidFormat bid: bids){
            if (bid.getBidID() == bidID)
                return  bid.getBidPrice();
        }
        return 0;
    }

    public BidFormat getBid(int bidID) throws BidNotFoundException {
        for(BidFormat bid: bids){
            if (bid.getBidID() == bidID)
                return bid;
        }
        throw new BidNotFoundException(String.format("Bid %d was not found", bidID));
    }

    public void acceptBid(int bidID, User approver) throws BidNotFoundException, CriticalInvariantException {
        getBid(bidID).approve(approver);
    }

    public void declineBid(int bidID, User decliner) throws BidNotFoundException {
        getBid(bidID).decline(decliner);
    }
}
