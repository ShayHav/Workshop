package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.ProductInfo;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.Shop;
import domain.shop.ShopController;
import domain.shop.ShopInfo;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketSystem {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private UserController uc;
    private ShopController sc;
    private static MarketSystem instance = null;
    private ExternalConnector externalConnector;

    private MarketSystem() {
        eventLogger.logMsg(Level.INFO,"System start");
        externalConnector = new ExternalConnector();
        this.start();
    }

    public static MarketSystem getInstance() {
        if (instance == null) {
            instance = new MarketSystem();
        }
        return instance;
    }


    /***
     * init the Market system:
     * Connect to payment service
     * Connect to supply service
     * Ensures there is at least 1 System manager
     */
    private void start() {
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param ti - contains amount, method of payment etc
     * @return true if approve, false if otherwise
     */
    public boolean pay(TransactionInfo ti) {
        return externalConnector.pay(ti);
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti, Map<Integer,Integer> products) {
        return externalConnector.supply(ti,products);
    }

    public void setSupplierConnection(boolean b)
    {

    }

    public void setPaymentConnection(boolean b)
    {

    }

    public void createSystemManger(String username, String pw)
    {

    }


    public List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f) {
        return sc.getInfoOfShops(f);
    }

    public List<ProductInfo> getInfoOfProductInShop(int shopID, Filter<ProductInfo> f){
       return sc.getInfoOfProductInShop(shopID, f);
    }

    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        return sc.searchProductByName(name,f);
    }


    public List<ProductInfo> searchProductByCategory(String category, Filter<ProductInfo> f) {
        return sc.searchProductByCategory(category,f);
    }


    public List<ProductInfo> searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        return sc.searchProductByKeyword(keyword, f);
    }

    public User getUser(String id) {
        return uc.getUser(id);
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        return sc.createShop(name,discountPolicy, purchasePolicy,id);
    }

    public boolean register(String userId,String pass){
       return uc.register(userId,pass);
    }

    public void deleteUserTest(String[] username){
        uc.deleteUserTest(username);
    }

    public Shop getShop(int shopID) {
        return sc.getShop(shopID);
    }
}
