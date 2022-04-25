package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;
import java.util.logging.Level;


public class Member implements UserState{
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    @Override
    public List<ShopInfo> getInfoOfShops() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void leaveMarket(Cart cart) {
        //TODO: how to accesses to the user cart?
    }


    /**
     *
     * @param name
     * @param discountPolicy
     * @param purchasePolicy
     * @param id
     */
    @Override
    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy,int id) {
        MarketSystem.getInstance().createShop(name, discountPolicy, purchasePolicy, id);
    }

    /***
     *  @param user - appointed for shopOwner
     * @param shop - relevant shop
     * @param id - appointee id
     * @param ownerAppointmentList
     */
    @Override
    public void appointOwner(User user, Shop shop,int id, List<OwnerAppointment> ownerAppointmentList) {
        if (shop.isFounder(id) || shop.isOwner(id)) {
            user.addRole(Role.ShopOwner);
            shop.setOwner(user.getId());
            if(isAppointedMeOwner(user,id)) {
                OwnerAppointment newAppointment = new OwnerAppointment(shop,id,user);
                ownerAppointmentList.add(newAppointment);
                eventLogger.logMsg(Level.INFO, String.format("appointOwner = {appointeeId: %d , appointedId: %d , ShopId %d}", id, user.getId(), shop.getShopID()));
            }
            else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
    } //TODO: should be at upper level

    private boolean isAppointedMeOwner(User user,int id){
        List<OwnerAppointment> Appointmentusers = user.getOwnerAppointmentList();
        for(OwnerAppointment run : Appointmentusers){
            if(run.getAppointed().getId()==id)
                return true;
        }
        return false;
    }
    /***
     *
     * @param user - The appointed user's object
     *               TODO: decide if need to add param Permissions or to receive permission in the
     */
    @Override
    public void appointManager(User user, Shop shop, int id, List<ManagerAppointment> managerAppointmentList) {
        if(shop.isOwner(id)){
            user.addRole(Role.ShopManager);
            shop.setManager(user.getId());
            if(isAppointedMeManager(user,id)) {
                ManagerAppointment newAppointment = new ManagerAppointment(shop, id, user);
                managerAppointmentList.add(newAppointment);
                eventLogger.logMsg(Level.INFO, String.format("appointManager = {appointeeId: %d , appointedId: %d , ShopId %d}", id, user.getId(), shop.getShopID()));
            }
            else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
    }

    private boolean isAppointedMeManager(User user,int id){
        List<ManagerAppointment> Appointmentusers = user.getManagerAppointeeList();
        for(ManagerAppointment run : Appointmentusers){
            if(run.getAppointed().getId()==id)
                return true;
        }
        return false;
    }
    /***
     *
     * @param user - The Manager's unique id
     */
    public void changeManagerPermissions(User user)
    {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param shop
     */
    @Override
    public void closeShop(Shop shop,int id) {
        shop.closeShop();
        if(!shop.isOpen())
            eventLogger.logMsg(Level.INFO,String.format("close shop protocol shop id: %d",shop.getShopID()));
        else eventLogger.logMsg(Level.WARNING,String.format("attempt to close shop filed shop id: %d , user id:%d",shop.getShopID(),id));
    }

    @Override
    public void searchProductByName(String name, Filter f) {

    }

    @Override
    public void searchProductByCategory(String category, Filter f) {

    }

    @Override
    public void searchProductByKeyword(String keyword, Filter f) {

    }


    /***
     * The function will display information on the shop, and shop's officials.
     * The function can be called only by Shop Owner
     * @param f
     */
    public void requestShopInfo(Filter f)
    {
        throw new UnsupportedOperationException();
    }



}
