package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.filter.*;

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

    public synchronized Shop createShop(String description, String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, User shopFounder) {
        Shop newShop;
        shopCounter++;
        newShop = new Shop(name, description, discountPolicy, purchasePolicy, shopFounder, shopCounter);
        shopList.put(shopCounter, newShop);
        eventLogger.logMsg(Level.INFO, String.format("create new shop. FounderId: %s , ShopName: %s", shopFounder.getUserName(), name));
        return newShop;

    }

    public List<Shop> getInfoOfShops(Filter<Shop> f) {
        List<Shop> allShops = new ArrayList<>();
        synchronized (this) {
            for (Shop s : shopList.values()) {
                if (s.isOpen())
                    allShops.add(s);
            }
        }
        return f.applyFilter(allShops);
    }

    public List<Product> getInfoOfProductInShop(int shopID, Filter<Product> f) {
        Shop s;
        try {
            s = getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        List<Product> info = s.getProductInfoOfShop();
        return f.applyFilter(info);
    }

    public Map<Integer, List<Product>> searchProductByName(String name, Filter<Product> f) {
        String lowerName = name.toLowerCase();
        Map<Integer, List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getName().toLowerCase().equals(lowerName)).collect(Collectors.toList());
                products.put(s.getShopID(), f.applyFilter(shopProducts));
            }
        }
        eventLogger.logMsg(Level.INFO, "searchProductByName succeeded");
        return products;
    }

    public Map<Integer, List<Product>> searchProductByKeyword(String keyword, Filter<Product> f) {
        String lowerKeyword = keyword.toLowerCase();
        Map<Integer, List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getName().toLowerCase().contains(lowerKeyword)).collect(Collectors.toList());
                products.put(s.getShopID(), f.applyFilter(shopProducts));
            }
        }
        eventLogger.logMsg(Level.INFO, "searchProductByKeyword succeeded");
        return products;
    }

    public Map<Integer, List<Product>> searchProductByCategory(String category, Filter<Product> f) {
        String category_lower = category.toLowerCase();
        Map<Integer, List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getCategory().toLowerCase().equals(category_lower)).collect(Collectors.toList());
                products.put(s.getShopID(), f.applyFilter(shopProducts));
            }
        }
        eventLogger.logMsg(Level.INFO, "searchProductByKeyword succeeded");
        return products;
    }

    public Shop getShop(int shopID) throws ShopNotFoundException {
        if (!shopList.containsKey(shopID)) {
            errorLogger.logMsg(Level.WARNING, String.format("shopId %d isn't a valid shop in market", shopID));
            throw new ShopNotFoundException("shop does not exist in market");
        }
        eventLogger.logMsg(Level.INFO, "getShop succeeded");
        return shopList.get(shopID);
    }

    public String closeShop(int key, String user) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        s.closeShop(user);
        eventLogger.logMsg(Level.INFO, "close shop succeeded");
        return s.getName();
    }

    public String openShop(int key, String user) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        s.openShop(user);
        eventLogger.logMsg(Level.INFO, "close shop succeeded");
        return s.getName();
    }

    //TEST METHOD
    public void DeleteShops() {
        shopList = new HashMap<>();
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopID) throws InvalidAuthorizationException {
        Shop s;
        try {
            s = getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return -1;
        }
        s.removeListing(productId, username);
        eventLogger.logMsg(Level.INFO, "RemoveProductFromShopInventory succeeded");
        return productId;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String id) {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        if (s.removePermissions(shopManagersPermissionsList, tragetUser, id)) {
            eventLogger.logMsg(Level.INFO, "RemoveShopManagerPermissions succeeded");
            return "Shop Manager Permissions Removed";
        } else
            return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String userName) {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        if (s.addPermissions(shopManagersPermissionsList, tragetUser, userName)) {
            eventLogger.logMsg(Level.INFO, "AddShopMangerPermissions succeeded");
            return "ShopManagerPermissionsAdd";
        } else
            return null;
    }

    public void AppointNewShopManager(int key, String targetUser, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, ShopNotFoundException {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            throw snfe;
        }
        s.AppointNewShopManager(targetUser, userId);
    }

    public void AppointNewShopOwner(int key, String targetUser, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc, ShopNotFoundException {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            throw snfe;
        }
        s.AppointNewShopOwner(targetUser, userId);
    }

    /* public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, User tragetUser, String id) {
         Shop s;
         try {
             s = getShop(key);
         } catch (ShopNotFoundException snfe) {
             errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
             return null;
         }
         synchronized (this) {
             if (s.removePermissions(shopManagersPermissionsList, tragetUser.getUserName(), id)) {
                 eventLogger.logMsg(Level.INFO, "RemoveShopManagerPermissions succeeded");
                 return "ShopManagerPermissionsRemove";
             } else return null;
         }
     }

     public String AppointNewShopOwner(int key, String targetUser, String userId) throws IncorrectIdentification, BlankDataExc {
         Shop s;
         try {
             s = getShop(key);
         } catch (ShopNotFoundException snfe) {
             errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
             return null;
         }
         eventLogger.logMsg(Level.INFO, "AppointNewShopOwner succeeded");
         return s.AppointNewShopOwner(targetUser, userId);
     }

     /**
      *
      * @param shopId
      * @return
      */
    public List<Order> getOrderHistoryForShops() throws ShopNotFoundException {
        List<Order> orders = new ArrayList<>();
        for (Shop s : shopList.values()) {
            orders.addAll(s.getOrders());
        }
        return orders;
    }

    public List<ShopManagersPermissions> checkPermissionsForManager(String managerUsername, int shopID) throws
            ShopNotFoundException, IllegalArgumentException {
        Shop shop = shopList.get(shopID);
        if (shop == null)
            throw new ShopNotFoundException(String.format("Shop id %d doesn't exist", shopID));

        return shop.requestInfoOnManagerPermissions(managerUsername);
    }


    /**
     * check for each shop if the user is Founder | Owner Or manager
     *
     * @param targetUser - wanted Delete user identifier
     * @return
     */
    public boolean canBeDismiss(String targetUser) {
        for (Map.Entry<Integer, Shop> entry : shopList.entrySet()) {
            if (!entry.getValue().canBeDismiss(targetUser))
                return false;
        }
        return true;
    }

    public boolean DismissalOwner(String userName, String targetUser, int shop) throws
            ShopNotFoundException, InvalidSequenceOperationsExc {
        return getShop(shop).DismissalOwner(userName, targetUser);
    }

    public List<Shop> getAllUserShops(String username) {
        List<Shop> shops = new ArrayList<>();
        for(Shop shop: shopList.values()){
            if(shop.getShopFounder().getUserName().equals(username))
                shops.add(shop);
        }
        return shops;
    }
}