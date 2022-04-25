package domain.market;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.ProductInfo;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.ShopController;
import domain.shop.ShopInfo;
import domain.shop.discount.DiscountPolicy;
import domain.user.SearchProductFilter;
import domain.user.TransactionInfo;
import domain.user.User;
import domain.user.UserController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MarketSystem {
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private UserController uc;
    private ShopController sc;
    private static MarketSystem instance = null;

    private MarketSystem() {
        eventLogger.logMsg(Level.INFO,"System start");
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
        throw new UnsupportedOperationException();
    }

    /***
     *
     * @param ti - should be address and maybe also date
     * @return - true if supply is approved, false otherwise
     */
    public boolean supply(TransactionInfo ti) {
        throw new UnsupportedOperationException();
    }


    public List<ShopInfo> getInfoOfShops() {
        return sc.getInfoOfShops();
    }

    public List<ProductInfo> getInfoOfProductInShop(int shopID){
       return sc.getInfoOfProductInShop(shopID);
    }

    public List<ProductInfo> searchProductByName(String name, SearchProductFilter f) {
        return sc.searchProductByName(name,f);
    }


    public List<ProductInfo> searchProductByCategory(String category, SearchProductFilter f) {
        return sc.searchProductByCategory(category,f);
    }


    public List<ProductInfo> searchProductByKeyword(String keyword, SearchProductFilter f) {
        return sc.searchProductByKeyword(keyword, f);
    }

    public User getUser(String id) {
        return uc.getUser(id);
    }

    public void createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        sc.createShop(name,discountPolicy, purchasePolicy,id);
    }
}
