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



    @Override
    public List<ShopInfo> getInfoOfShops() {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfShops();
    }

    @Override
    public List<ProductInfo> getInfoOfProductInShop(int shopID) {
        MarketSystem market = MarketSystem.getInstance();
        return market.getInfoOfProductInShop(shopID);
    }


    @Override
    public List<ProductInfo> searchProductByName(String name, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByName(name, f);
    }

    @Override
    public List<ProductInfo>  searchProductByCategory(String category, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByCategory(category, f);
    }

    @Override
    public List<ProductInfo>  searchProductByKeyword(String keyword, SearchProductFilter f) {
        MarketSystem market = MarketSystem.getInstance();
        return market.searchProductByKeyword(keyword, f);
    }


    @Override
    public List<ResponseT<Order>> checkout(String id, Cart c, String fullName, String address, String phoneNumber, String cardNumber, String expirationDate) {
        return c.checkout(id, fullName, address, phoneNumber, cardNumber, expirationDate);
    }

    @Override
    public void leaveMarket(Cart cart) {
        //TODO: impl,shahar need to add method save cart when we manage DB
    }

    /***
     *
     * @param name
     * @param discountPolicy
     * @param purchasePolicy
     * @param id
     */
    @Override
    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy,String id)
    {
        MarketSystem.getInstance().createShop(name,discountPolicy,purchasePolicy, id);
    } //TODO: should be at upper level

    /***
     *  @param user - appointed for shopOwner
     * @param shop - relevant shop
     * @param id - appointee id
     * @param ownerAppointmentList
     */
    @Override
    public void appointOwner(User user, Shop shop,String id, List<OwnerAppointment> ownerAppointmentList) {
        if (shop.isFounder(id) || shop.isOwner(id)) {
            user.addRole(Role.ShopOwner);
            shop.setOwner(user.getId());
            OwnerAppointment newAppointment = new OwnerAppointment(shop,id,user);
            ownerAppointmentList.add(newAppointment);
            eventLogger.logMsg(Level.INFO,String.format("appointOwner = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointOwner without permissions = {appointeeId: %d , appointedId: %d , ShopId %d}",id,user.getId(),shop.getShopID()));
    } //TODO: should be at upper level

    /***
     *
     * @param user - The appointed user's object
     *               TODO: decide if need to add param Permissions or to receive permission in the
     */
    @Override
    public void appointManager(User user, Shop shop, String id, List<ManagerAppointment> managerAppointmentList) {
        if(shop.isOwner(id)){
            user.addRole(Role.ShopManager);
            shop.setManager(user.getId());
            ManagerAppointment newAppointment = new ManagerAppointment(shop,id,user);
            managerAppointmentList.add(newAppointment);
            eventLogger.logMsg(Level.INFO,String.format("appointManager = {appointeeId: %s , appointedId: %s , ShopId %d}",id,user.getId(),shop.getShopID()));
        }
        else errorLogger.logMsg(Level.WARNING,String.format("attempt to appointManager without permissions = {appointeeId: %s , appointedId: %s , ShopId %d}",id,user.getId(),shop.getShopID()));
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
    public void closeShop(Shop shop,String id) {
        if(shop.closeShop())
            eventLogger.logMsg(Level.INFO,String.format("close shop protocol shop id: %s",shop.getShopID()));
        else eventLogger.logMsg(Level.WARNING,String.format("attempt to close shop filed shop id: %s , user id:%s",shop.getShopID(),id));
    }






}
