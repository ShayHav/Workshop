package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.Filter;
import domain.user.SearchProductFilter;
import domain.user.User;
import domain.user.UserController;

import java.util.*;
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

    public int getShopCounter() {
        return shopCounter;
    }

    public int createShop(String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, String id) {
        if (isUniqueName(name)) {
            shopCounter++;
            Shop newShop = new Shop(name, discountPolicy, purchasePolicy, id, shopCounter);
            shopList.put(shopCounter, newShop);
            eventLogger.logMsg(Level.INFO, String.format("create new shop. FounderId: %s , ShopName: %s", id, name));
            return shopCounter;
        }
        errorLogger.logMsg(Level.WARNING, String.format("attempt to create a shop with exist name. id: %s , name: %s", id, name));
        return -1;
    }

    private boolean isUniqueName(String name) {
        for (Map.Entry<Integer, Shop> entry : shopList.entrySet()) {
            if (entry.getValue().getName().equals(name))
                return false;
        }
        return true;
    }

    public List<ShopInfo> getInfoOfShops(Filter<ShopInfo> f) {
        List<ShopInfo> allShops = new ArrayList<>();
        for (Shop s : shopList.values()) {
            ShopInfo info = s.getShopInfo();
            if (info != null) //means, if the shop is open
                allShops.add(s.getShopInfo());
        }
        return f.applyFilter(allShops);
    }

    public List<ProductInfo> getInfoOfProductInShop(int shopID, Filter<ProductInfo> f) {
        Shop s = getShop(shopID);
        if(s != null) {
            List<ProductInfo> info = s.getProductInfoOfShop();
            return f.applyFilter(info);
        }
        return null;
    }

    public List<ProductInfo> searchProductByName(String name, Filter<ProductInfo> f) {
        String lowerName = name.toLowerCase();
        List<ProductInfo> products = new ArrayList<>();
        for (Shop s : shopList.values()) {
            List<ProductInfo> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getProductName().toLowerCase().equals(lowerName)).collect(Collectors.toList());
            products.addAll(shopProducts);
        }
        return f.applyFilter(products);
    }

    public List<ProductInfo> searchProductByKeyword(String keyword, Filter<ProductInfo> f) {
        String lowerKeyword = keyword.toLowerCase();
        List<ProductInfo> products = new ArrayList<>();
        for (Shop s : shopList.values()) {
            List<ProductInfo> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getProductName().toLowerCase().contains(lowerKeyword)).collect(Collectors.toList());
            products.addAll(shopProducts);
        }
        return f.applyFilter(products);
    }

    public Shop getShop(int shopID) {
        if (!shopList.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING,String.format("shopId %d isn't a valid shop in market", shopID));
            return null;
        }
        return shopList.get(shopID);
    }

    public String closeShop(int key, String user) {
        Shop s = getShop(key);
        if (s != null) {
            s.closeShop(user);
            eventLogger.logMsg(Level.INFO, "close shop succeeded");
            return s.getName();
        }
        return null;
    }

    public void DeleteShops() {
        shopList = new HashMap<>();
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopname) {
        Shop s = getShop(shopname);
        if (s != null) {
            s.removeListing(productId, username);
            return productId;
        }
        return -1;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String id) {
        Shop s = getShop(key);
        if (s.removePermissions(shopManagersPermissionsList, tragetUser, id))
            return "ShopManagerPermissionsRemove";
        else return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String id) {
        Shop s = getShop(key);
        if (s.addPermissions(shopManagersPermissionsList, tragetUser, id))
            return "ShopManagerPermissionsAdd";
        else return null;
    }

    public String AppointNewShopManager(int key, String targetUser, String userId) {
        Shop s = getShop(key);
        return s.AppointNewShopManager(targetUser, userId);
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, User tragetUser, String id) {
        Shop s = getShop(key);
        if (s.removePermissions(shopManagersPermissionsList, tragetUser.getId(), id))
            return "ShopManagerPermissionsRemove";
        else return null;
    }

    public String AppointNewShopOwner(int key, String targetUser, String userId) {
        Shop s = getShop(key);
        return s.AppointNewShopOwner(targetUser, userId);
    }

    public List<Order> getOrderHistoryForShops(List<Integer> shopId) {
        List<Order> orders = new ArrayList<>();
        if (shopId == null) {
            for (Shop s : shopList.values()) {
                orders.addAll(s.getOrders());
            }
        } else {
            for (Integer id : shopId) {
                Shop s = shopList.get(id);
                if (s == null) {
                    //log
                    return null;
                }
                orders.addAll(s.getOrders());
            }
        }
        return orders;
    }

}
