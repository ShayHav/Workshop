package domain.user;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.*;

import java.util.List;
import java.util.logging.Level;


public class Member implements UserState{
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();


    @Override
    public List<ShopInfo> getInfoOfShops() {
        return null;
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        return null;
    }

    @Override
    public void checkout(int id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {

    }


    @Override
    public void leaveMarket(Cart cart) {
        //TODO: how to accesses to the user cart?
    }


    /***
     *
     * @param shop - shop's name or ID
     */
    @Override
    public void createShop(Shop shop,int id)
    {
        shop.setFounder(id);
    } //TODO: should be at upper level

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
            OwnerAppointment newAppointment = new OwnerAppointment(shop,id,user);
            ownerAppointmentList.add(newAppointment);
            eventLogger.logMsg(Level.INFO,String.format("appointOwner = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getId()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getId()));
    } //TODO: should be at upper level

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
            ManagerAppointment newAppointment = new ManagerAppointment(shop,id,user);
            managerAppointmentList.add(newAppointment);
            eventLogger.logMsg(Level.INFO,String.format("appointManager = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getId()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointManager without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getId()));
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
        if(shop.close(id))
            eventLogger.logMsg(Level.INFO,String.format("close shop protocol shop id: %d",shop.getId()));
        else eventLogger.logMsg(Level.WARNING,String.format("attempt to close shop filed shop id: %d , user id:%d",shop.getId(),id));
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
