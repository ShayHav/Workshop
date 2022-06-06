package domain.shop;

import domain.ControllersBridge;
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

    public Shop createShop(String description,String name, DiscountPolicy discountPolicy, PurchasePolicy purchasePolicy, User shopFounder) throws InvalidSequenceOperationsExc {
        Shop newShop;
        synchronized(this) {
            shopCounter++;
            newShop = new Shop(name, description, discountPolicy, purchasePolicy, shopFounder, shopCounter);
            shopList.put(shopCounter, newShop);
        }
        shopFounder.addRole(shopCounter,Role.ShopFounder);
        eventLogger.logMsg(Level.INFO, String.format("create new shop. FounderId: %s , ShopName: %s", shopFounder.getUserName(), name));
        return newShop;

    }

    public List<Shop> getInfoOfShops(Filter<Shop> f) {
        List<Shop> allShops = new ArrayList<>();
        synchronized (this) {
            for (Shop s : shopList.values()) {
                if(s.isOpen())
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

    public Map<Integer,List<Product>> searchProductByName(String name, Filter<Product> f) {
        String lowerName = name.toLowerCase();
        Map<Integer,List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getName().toLowerCase().equals(lowerName)).collect(Collectors.toList());
                products.put(s.getShopID(),f.applyFilter(shopProducts));
            }
        }
        eventLogger.logMsg(Level.INFO, "searchProductByName succeeded");
        return products;
    }

    public Map<Integer,List<Product>> searchProductByKeyword(String keyword, Filter<Product> f) {
        String lowerKeyword = keyword.toLowerCase();
        Map<Integer,List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getName().toLowerCase().contains(lowerKeyword)).collect(Collectors.toList());
                products.put(s.getShopID(),f.applyFilter(shopProducts));
            }
        }
        eventLogger.logMsg(Level.INFO, "searchProductByKeyword succeeded");
        return products;
    }

    public Map<Integer,List<Product>> searchProductByCategory(String category, Filter<Product> f) {
        String category_lower = category.toLowerCase();
        Map<Integer,List<Product>> products = new HashMap<>();
        synchronized (shopList) {
            for (Shop s : shopList.values()) {
                List<Product> shopProducts = s.getProductInfoOfShop().stream().filter(p -> p.getCategory().toLowerCase().equals(category_lower)).collect(Collectors.toList());
                products.put(s.getShopID(),f.applyFilter(shopProducts));
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

    public Shop shopExist(String shopName) throws ShopNotFoundException {
        for (Shop s : shopList.values()) {
            if (s.getName().equals(shopName))
                eventLogger.logMsg(Level.INFO, "getShop succeeded");
            return s;
        }

        errorLogger.logMsg(Level.WARNING, String.format("shopName %s isn't a valid shop in market", shopName));
        throw new ShopNotFoundException("shop does not exist in market");
    }

    public String closeShop(int key, String user) throws InvalidSequenceOperationsExc {
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

    public String openShop(int key, String user) throws InvalidSequenceOperationsExc {
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
    public String AddShopMangerPermissions(int key, ShopManagersPermissions shopManagersPermissionsList, String tragetUser, String userName) {
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

    public String AppointNewShopManager(int key, String targetUser, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        eventLogger.logMsg(Level.INFO, "AppointNewShopManager succeeded");
        return s.AppointNewShopManager(targetUser, userId);
    }
    public String AppointNewShopOwner(int key, String targetUser, String userId) throws IncorrectIdentification, BlankDataExc, InvalidSequenceOperationsExc {
        Shop s;
        try {
            s = getShop(key);
        }catch (ShopNotFoundException snfe){
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            return null;
        }
        return s.AppointNewShopOwner(targetUser, userId);
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
    public List<Order> getOrderHistoryForShops(List<Integer> shopId) throws ShopNotFoundException {
        List<Order> orders = new ArrayList<>();
        if (shopId == null) {
            for (Shop s : shopList.values()) {
                orders.addAll(s.getOrders());
            }
        } else {
            for (Integer id : shopId) {
                Shop s = shopList.get(id);
                if (s == null) {
                    errorLogger.logMsg(Level.WARNING,String.format("Shop not exist: %d",id));
                    throw new ShopNotFoundException();
                }
                orders.addAll(s.getOrders());
            }
        }
        eventLogger.logMsg(Level.INFO, "getOrderHistoryForShops succeeded");
        return orders;


    }

    public List<ShopManagersPermissions> checkPermissionsForManager(String managerUsername, int shopID) throws ShopNotFoundException, IllegalArgumentException {
        Shop shop = shopList.get(shopID);
        if(shop == null)
            throw new ShopNotFoundException(String.format("Shop id %d doesn't exist", shopID));

        return shop.requestInfoOnManagerPermissions(managerUsername);
    }


    /**
     * check for each shop if the user is Founder | Owner Or manager
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

    public boolean DismissalOwner(String userName, String targetUser, int shop) throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        return getShop(shop).DismissalOwner(userName,targetUser);
    }

    public boolean isShopClose(int i) {
        return !shopList.get(i).isOpen();
    }
}