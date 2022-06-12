package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.EventLoggerSingleton;
import domain.Exceptions.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.shop.predicate.ToBuildDiscountPredicate;
import domain.shop.predicate.ToBuildPRPredicateFrom;
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
            newShop = new Shop(name, description, shopFounder, shopCounter);
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

    public Shop shopExist(String shopName) throws ShopNotFoundException {
        for (Shop s : shopList.values()) {
            if (s.getName().equals(shopName))
                eventLogger.logMsg(Level.INFO, "getShop succeeded");
            return s;
        }

        errorLogger.logMsg(Level.WARNING, String.format("shopName %s isn't a valid shop in market", shopName));
        throw new ShopNotFoundException("shop does not exist in market");
    }

    public String closeShop(int key, String user) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc,ShopNotFoundException {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            throw new ShopNotFoundException("this shop does not exist, thus cannot be closed");
        }
        s.closeShop(user);
        eventLogger.logMsg(Level.INFO, "close shop succeeded");
        return s.getName();
    }

    public String openShop(int key, String user) throws InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc, ShopNotFoundException {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            throw new ShopNotFoundException("this shop does not exist, thus cannot be closed");
        }
        s.openShop(user);
        eventLogger.logMsg(Level.INFO, "close shop succeeded");
        return s.getName();
    }

    //TEST METHOD
    public void DeleteShops() {
        shopList = new HashMap<>();
    }

    public int RemoveProductFromShopInventory(int productId, String username, int shopID) throws InvalidAuthorizationException, InvalidProductInfoException {
        Shop s;
        try {
            s = getShop(shopID);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist");
            return -1;
        }
        s.removeListing(productId, username);
        eventLogger.logMsg(Level.INFO, "RemoveProductFromShopInventory succeeded");
        return productId;
    }

    public String RemoveShopManagerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String id) throws InvalidSequenceOperationsExc, ShopNotFoundException {
        Shop s;
        s = getShop(key);
        if (s.removePermissions(shopManagersPermissionsList, tragetUser, id)) {
            eventLogger.logMsg(Level.INFO, "RemoveShopManagerPermissions succeeded");
            return "Shop Manager Permissions Removed";
        } else
            return null;
    }

    public String AddShopMangerPermissions(int key, List<ShopManagersPermissions> shopManagersPermissionsList, String tragetUser, String userName) throws InvalidSequenceOperationsExc {
        Shop s;
        try {
            s = getShop(key);
        } catch (ShopNotFoundException snfe) {
            errorLogger.logMsg(Level.SEVERE, "this shop does not exist, thus cannot be closed");
            throw new InvalidSequenceOperationsExc("this shop does not exist, thus cannot be closed");
        }
        if (s.addPermissions(shopManagersPermissionsList, tragetUser, userName)) {
            eventLogger.logMsg(Level.INFO, "AddShopMangerPermissions succeeded");
            return "ShopManagerPermissionsAdd";
        } else
            return "";
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
    public Map<Shop,List<Order>> getOrderHistoryForShops(Filter<Order> filter) throws ShopNotFoundException {
        Map<Shop,List<Order>> orders = new HashMap<>();
        for (Shop s : shopList.values()) {
            List<Order> shopOrders = s.getOrders();
            if(shopOrders.size()> 0)
                orders.put(s, filter.applyFilter(shopOrders));
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

    public boolean DismissalOwner(String userName, String targetUser, int shop) throws ShopNotFoundException, InvalidSequenceOperationsExc, IncorrectIdentification, BlankDataExc {
        return getShop(shop).DismissalOwner(userName,targetUser);
    }

    public List<Shop> getAllUserShops(String username, Filter<Shop> filter) throws IncorrectIdentification {
        List<Shop> shops = new ArrayList<>();
        for(Shop shop: shopList.values()){
            User user = UserController.getInstance().getUser(username);
            if(shop.getShopFounder().getUserName().equals(username) || shop.getShopsManagers().contains(user) || shop.getShopOwners().contains(user))
                shops.add(shop);
        }
        return filter.applyFilter(shops);
    }

    public boolean isShopClose(int i) {
        return !shopList.get(i).isOpen();
    }



    public int addSimpleProductDiscount(String userName, int shopID, int prodID, double percentage) throws InvalidParamException, ShopNotFoundException, ProductNotFoundException {
        Shop shop;
        shop = getShop(shopID);
        return shop.addSimpleProductDiscount(userName,prodID, percentage);
    }

    public int addSimpleCategoryDiscount(String userName, int shopID, String category, double percentage) throws InvalidParamException, ShopNotFoundException {
        Shop shop;
        shop = getShop(shopID);
        return shop.addSimpleCategoryDiscount(userName,category, percentage);
    }

    public int addSimpleShopAllProductsDiscount(String userName, int shopID, double percentage) throws InvalidParamException, ShopNotFoundException {
        Shop shop;
        shop = getShop(shopID);
        return shop.addSimpleShopAllProductsDiscount(userName,percentage);
    }

    public int addConditionalProductDiscount(String userName, int shopID, int prodID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws ShopNotFoundException, InvalidParamException, CriticalInvariantException, AccessDeniedException, ProductNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addConditionalProductDiscount(userName,prodID, percentage, toBuildPredicatesFrom);
    }

    public int addConditionalCategoryDiscount(String userName, int shopID, String category, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws ShopNotFoundException, InvalidParamException, CriticalInvariantException, AccessDeniedException {
        Shop shop = getShop(shopID);
        return shop.addConditionalCategoryDiscount(userName,category, percentage, toBuildPredicatesFrom);
    }

    public int addConditionalShopAllProductsDiscount(String userName, int shopID, double percentage, ToBuildDiscountPredicate toBuildPredicatesFrom) throws ShopNotFoundException, InvalidParamException, CriticalInvariantException, AccessDeniedException {
        Shop shop = getShop(shopID);
        return shop.addConditionalShopAllProductsDiscount(userName,percentage, toBuildPredicatesFrom);
    }

    public int addProductPurchasePolicy(String userName, int shopID, int prodID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException, ProductNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addProductPurchasePolicy(userName,prodID, toBuildPredicatesFrom);
    }

    public int addCategoryPurchasePolicy(String userName, int shopID, String category, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException {
        Shop shop = getShop(shopID);
        return shop.addCategoryPurchasePolicy(userName,category, toBuildPredicatesFrom);
    }


    public int addShopAllProductsPurchasePolicy(String userName, int shopID, ToBuildPRPredicateFrom toBuildPredicatesFrom) throws CriticalInvariantException, ShopNotFoundException, AccessDeniedException {
        Shop shop = getShop(shopID);
        return shop.addShopAllProductsPurchasePolicy(userName,toBuildPredicatesFrom);
    }


    public int addOrDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws DiscountNotFoundException, CriticalInvariantException, ShopNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addOrDiscount(userName,dis1ID, dis2ID);
    }

    public int addAndDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws ShopNotFoundException, DiscountNotFoundException, CriticalInvariantException {
        Shop shop = getShop(shopID);
        return shop.addAndDiscount(userName,dis1ID, dis2ID);
    }

    public int addXorDiscount(String userName, int dis1ID, int dis2ID, int shopID) throws DiscountNotFoundException, CriticalInvariantException, ShopNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addXorDiscount(userName,dis1ID, dis2ID);
    }

    public int addOrPurchaseRule(String userName, int pr1ID, int pr2ID, int shopID) throws PurchaseRuleNotFoundException, CriticalInvariantException, ShopNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addOrPurchaseRule(userName,pr1ID, pr2ID);
    }

    public int addAndPurchaseRule(String userName, int pr1ID, int pr2ID, int shopID) throws PurchaseRuleNotFoundException, CriticalInvariantException, ShopNotFoundException {
        Shop shop = getShop(shopID);
        return shop.addAndPurchaseRule(userName,pr1ID, pr2ID);
    }

    public boolean removeDiscount(String userName, int discountID, int shopID) throws ShopNotFoundException {
        Shop shop = getShop(shopID);
        return shop.removeDiscount(userName,discountID);
    }


    public void removePurchaseRule(String userName, int purchaseRuleID, int shopID) throws ShopNotFoundException {
        Shop shop = getShop(shopID);
        shop.removePurchaseRule(userName,purchaseRuleID);
    }

    public void deleteShopTest(Integer key) {
        shopList.remove(key);
    }

    public void RemoveShopOwnerTest(Integer key, String useID) {
        try {
            Shop shop = getShop(key);
            shop.RemoveShopOwnerTest(useID);
        }
        catch (ShopNotFoundException shopNotFoundException){

        }
    }

    public void RemoveShopManagerTest(Integer key, String useID) {
        try {
            Shop shop = getShop(key);
            shop.RemoveShopManagerTest(useID);
        }
        catch (ShopNotFoundException shopNotFoundException){

        }
    }
}