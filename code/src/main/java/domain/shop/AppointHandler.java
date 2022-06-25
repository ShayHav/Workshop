package domain.shop;

import domain.Exceptions.*;
import domain.shop.PurchaseFormats.BidFormat;
import domain.user.User;

import java.util.ArrayList;
import java.util.List;

public class AppointHandler {
    private int bidIDCounter;
    private List<AppointFormat> bids;

    public AppointHandler(){
        bids = new ArrayList<>();
        bidIDCounter = 1;
    }

    public int addNewAppoint(User userToAppoint, User appointUser, Shop shop , List<User> toConfirm){
        AppointFormat newBid = new AppointFormat(userToAppoint,appointUser,shop,toConfirm,bidIDCounter);
        bidIDCounter++;
        synchronized (bids) {
            bids.add(newBid);
        }
        return newBid.getId();
    }

    public boolean removeBid(int bidID){
        synchronized (bids) {
            for (AppointFormat bid : bids) {
                if (bid.getId() == bidID)
                    return bids.remove(bid);
            }
        }
        return false;
    }

    public AppointFormat getBid(int bidID) throws BidNotFoundException {
        for(AppointFormat bid: bids){
            if (bid.getId() == bidID)
                return bid;
        }
        throw new BidNotFoundException(String.format("Bid %d was not found", bidID));
    }

    public void acceptAppoint(int bidID, User approver) throws BidNotFoundException, CriticalInvariantException, IncorrectIdentification, InvalidSequenceOperationsExc, BlankDataExc {
        getBid(bidID).approve(approver);
    }

    public void declineAppoint(int bidID, User decliner) throws BidNotFoundException, CriticalInvariantException {
        getBid(bidID).decline(decliner);
    }
}
