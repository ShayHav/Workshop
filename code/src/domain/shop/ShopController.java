package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.Filter;
import domain.user.SearchProductFilter;
import domain.user.UserController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ShopController {
    private Map<Integer, Shop> shopList;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private static final EventLoggerSingleton eventLogger = EventLoggerSingleton.getInstance();
    private int shopCounter = 0;
    private static ShopController instance = null;

    private ShopController() {
        shopList = new HashMap<>();
    }

    public static ShopController getInstance() {
        if (instance == null) {
            instance = new ShopController();
        }

        return instance;
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        if(isUniqueName(name)) {
            shopCounter++;
            Shop newShop = new Shop(name, discountPolicy, purchasePolicy, id,shopCounter);
            shopList.put(shopCounter,newShop);
            eventLogger.logMsg(Level.INFO,String.format("create new shop. FounderId: %s , ShopName: %s",id,name));
            return shopCounter;
        }
        errorLogger.logMsg(Level.WARNING,String.format("attempt to create a shop with exist name. id: %s , name: %s",id,name));
        return -1;
    }

    private boolean isUniqueName(String name) {
        for (Map.Entry<Integer, Shop> entry : shopList.entrySet()){
           if(entry.getValue().getName().equals(name))
               return false;
        }
        return true;
    }

    public List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f) {
        List<ShopInfo> allShops = new ArrayList<>();
        for(Shop s: shopList.values()){
            allShops.add(s.getShopInfo());
        }
        return f.applyFilter(allShops);
    }

    public List<ProductInfo> getInfoOfProductInShop(int shopID, Filter<ProductInfo> f) {
        if(!shopList.containsKey(shopID)){
            //log
            return null;
        }

        Shop s = shopList.get(shopID);
        List<ProductInfo> info = s.getProductInfoOfShop();
        return f.applyFilter(info);
    }

    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        List<ProductInfo> products = new ArrayList<>();
        for(Shop s: shopList.values()){
            List<ProductInfo> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getProductName().equals(name)).collect(Collectors.toList());
            products.addAll(shopProducts);
        }
        return f.applyFilter(products);
    }

    public List<ProductInfo> searchProductByCategory(String category, Filter<ProductInfo> f) {
        List<ProductInfo> products = new ArrayList<>();
        for(Shop s: shopList.values()){
            List<ProductInfo> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getCategory().equals(category)).collect(Collectors.toList());
            products.addAll(shopProducts);
        }
        return f.applyFilter(products);
    }

    public List<ProductInfo> searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        List<ProductInfo> products = new ArrayList<>();
        for(Shop s: shopList.values()){
            List<ProductInfo> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getProductName().contains(keyword)).collect(Collectors.toList());
            products.addAll(shopProducts);
        }
        return f.applyFilter(products);
    }

    public Shop getShop(int shopID) {
        if(!shopList.containsKey(shopID)){
            //log
            return null;
        }
        return shopList.get(shopID);
    }
}
