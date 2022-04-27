package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.ResponseT;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;

import java.util.List;
import java.util.logging.Level;


public class Member implements UserState{
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    public boolean saveCart(Cart cart) {
        throw new UnsupportedOperationException("guest is not allowed to perform this action");
    }


    /**
     *
     * @param name
     * @param discountPolicy
     * @param purchasePolicy
     * @param id
     */
    @Override
    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy,String id) {
        return MarketSystem.getInstance().createShop(name, discountPolicy, purchasePolicy, id);
    }

    /**
     *
     * @param targetUser
     * @param shop
     * @param id
     * @param ownerAppointmentList
     */
    @Override
    public void appointOwner(String targetUser, int shop , String id, List<OwnerAppointment> ownerAppointmentList) {
        Shop shop1 = MarketSystem.getInstance().getShop(shop);
        User user = MarketSystem.getInstance().getUser(targetUser);
        if (shop1.isFounder(id) || shop1.isOwner(id)) {
            user.addRole(shop1.getShopID(),Role.ShopOwner);
            shop1.AppointNewShopOwner(targetUser,id);
            if(isAppointedMeOwner(user,id)) {
                OwnerAppointment newAppointment = new OwnerAppointment(shop1,id,user);
                ownerAppointmentList.add(newAppointment);
                eventLogger.logMsg(Level.INFO, String.format("appointOwner = {appointeeId: %s , appointedId: %s , ShopId %s}", id, user.getId(), shop));
            }
            else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %s , appointedId: %s , ShopId %s}",id,targetUser,shop));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %s , appointedId: %s , ShopId %s}",id,targetUser,shop));
    } //TODO: should be at upper level

    private boolean isAppointedMeOwner(User user,String id){
        List<OwnerAppointment> Appointmentusers = user.getOwnerAppointmentList();
        for(OwnerAppointment run : Appointmentusers){
            if(run.getAppointed().getId().equals(id))
                return true;
        }
        return false;
    }

    /**
     *
     * @param targetUser
     * @param shop
     * @param id
     * @param managerAppointmentList
     */
    @Override
    public void appointManager(String targetUser, int shop, String id, List<ManagerAppointment> managerAppointmentList) {
        Shop shop1 = MarketSystem.getInstance().getShop(shop);
        User user1 = MarketSystem.getInstance().getUser(targetUser);
        synchronized (this) {
            if (shop1.isOwner(id)) {
                user1.addRole(shop, Role.ShopManager);
                shop1.AppointNewShopManager(targetUser, id);
                if (isAppointedMeManager(user1, id)) {
                    ManagerAppointment newAppointment = new ManagerAppointment(shop1, id, user1);
                    managerAppointmentList.add(newAppointment);
                    eventLogger.logMsg(Level.INFO, String.format("appointManager = {appointeeId: %s , appointedId: %s , ShopId %s}", id, targetUser, shop));
                } else
                    errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}", id, targetUser, shop));
            } else
                errorLogger.logMsg(Level.WARNING, String.format("attempt to appointManager without permissions = {appointeeId: %s , appointedId: %s , ShopId %d}", id,targetUser, shop));
        }
    }

    private boolean isAppointedMeManager(User user,String id){
        List<ManagerAppointment> Appointmentusers = user.getManagerAppointeeList();
        for(ManagerAppointment run : Appointmentusers){
            if(run.getAppointed().getId().equals(id))
                return true;
        }
        return false;
    }


    /**
     *
     * @param targetUser
     * @param shop
     * @param userId
     * @param shopManagersPermissionsList
     * @return
     */
    public boolean addManagerPermissions(String targetUser,int shop,String userId,List<ShopManagersPermissions> shopManagersPermissionsList) {
        synchronized (this) {
            Shop shop1 = MarketSystem.getInstance().getShop(shop);
            User user = MarketSystem.getInstance().getUser(targetUser);
            return shop1.addPermissions(shopManagersPermissionsList, targetUser, userId);
        }
    }

    public boolean removeManagerPermissions(String targetUser,int shop,String userId,List<ShopManagersPermissions> shopManagersPermissionsList){
        synchronized (this) {
            Shop shop1 = MarketSystem.getInstance().getShop(shop);
            User user = MarketSystem.getInstance().getUser(targetUser);
            return shop1.removePermissions(shopManagersPermissionsList, targetUser, userId);
        }
    }
    /***
     *
     * @param shop
     */
    @Override
    public void closeShop(int shop,String id) {
        Shop shop1 = MarketSystem.getInstance().getShop(shop);
        shop1.closeShop(id);
        if(!shop1.isOpen())
            eventLogger.logMsg(Level.INFO,String.format("close shop protocol shop id: %s",shop));
        else eventLogger.logMsg(Level.WARNING,String.format("attempt to close shop filed shop id: %s , user id:%s",shop,id));
    }

    @Override
    public List<Order> getOrderHistoryForUser(Filter<Order> f, List<String> userID) {
        throw new UnsupportedOperationException("member is not allowed to perform this action");
    }

    @Override
    public List<Order> getOrderHistoryForShops(Filter<Order> f, List<Integer> shopID) {
        throw new UnsupportedOperationException("member is not allowed to perform this action");
    }

    /***
     * The function will display information on the shop, and shop's officials.
     * The function can be called only by Shop Owner
     * @param f
     */
    public void requestInfoOnOfficials(Filter f)
    {
        throw new UnsupportedOperationException();
    }







}
