package domain.shop;

import domain.ErrorLoggerSingleton;
import domain.Exceptions.InvalidSequenceOperationsExc;

import javax.persistence.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShopManagersPermissionsController {

    private List<ShopManagersPermissions> managerinit = List.of(new ShopManagersPermissions[]{ShopManagersPermissions.AddProductToInventory, ShopManagersPermissions.RemoveProductFromInventory});
    private Map<String, List<ShopManagersPermissions>> shopManagersPermissionsMap;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private int shopID;

    public List<ShopManagersPermissions> getManagerinit() {
        return managerinit;
    }

    public void setManagerinit(List<ShopManagersPermissions> managerinit) {
        this.managerinit = managerinit;
    }

    public Map<String, List<ShopManagersPermissions>> getShopManagersPermissionsMap() {
        return shopManagersPermissionsMap;
    }

    public void setShopManagersPermissionsMap(Map<String, List<ShopManagersPermissions>> shopManagersPermissionsMap) {
        this.shopManagersPermissionsMap = shopManagersPermissionsMap;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public ShopManagersPermissionsController() {
        shopManagersPermissionsMap = new HashMap<>();
    }

    public boolean canAddProductToInventory(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AddProductToInventory);
        return false;
    }

    public boolean canRemoveProductFromInventory(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RemoveProductFromInventory);
        return false;
    }

    public boolean canChangeProductsDetail(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsDetail);
        return false;
    }

    public boolean canChangeBuyingShopPolicy(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeBuyingShopPolicy);
        return false;
    }

    public boolean canChangeDiscountShopPolicy(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeDiscountShopPolicy);
        return false;
    }

    public boolean canChangeProductsBuyingShopPolicy(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsBuyingShopPolicy);
        return false;
    }

    public boolean canChangeProductsDiscountShopPolicy(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsDiscountShopPolicy);
        return false;
    }

    public boolean canAppointNewShopOwner(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AppointNewShopOwner);
        return false;
    }

    public boolean canAppointNewShopManager(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AppointNewShopManager);
        return false;
    }

    public boolean canChangeManagerPrivileges(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeManagerPrivileges);
        return false;
    }

    public boolean canChangeShopManagersPermissions(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeShopManagersPermissions);
        return false;
    }

    public boolean canCloseShop(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.CloseShop);
        else return false;
    }

    public boolean canOpenShop(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.OpenShop);
        return false;
    }

    public boolean canDismissalOfStoreOwner(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.OpenShop);
        return false;
    }

    public boolean canRequestInformationOnShopsOfficials(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RequestInformationOnShopsOfficials);
        return false;
    }

    public boolean canRequestInformationOfShopsSalesHistory(String userId) {
        if (shopManagersPermissionsMap.containsKey(userId))
            return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RequestInformationOfShopsSalesHistory);
        return false;
    }

    public boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser) throws InvalidSequenceOperationsExc {
        if (shopManagersPermissionsList != null || shopManagersPermissionsList.size() > 0) {
            List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);

            if (userShopManagersPermissionsList != null) {
                for (ShopManagersPermissions run : shopManagersPermissionsList) {
                    if (userShopManagersPermissionsList.contains(run))
                        userShopManagersPermissionsList.remove(run);
                }
            } else throw new InvalidSequenceOperationsExc(String.format("not a Manager: %s", targetUser));
            return PermissionNotExist(shopManagersPermissionsList, targetUser);
        } else return false;
    }

    private boolean PermissionNotExist(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser) {
        boolean output = true;
        List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
        for (ShopManagersPermissions run : shopManagersPermissionsList)
            if (userShopManagersPermissionsList.contains(run)) {
                output = false;
                errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
            }
        return output;
    }

    public boolean addPermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser) {
        if (shopManagersPermissionsList != null) {
            List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
            if (userShopManagersPermissionsList != null) {
                for (ShopManagersPermissions run : shopManagersPermissionsList) {
                    if (!userShopManagersPermissionsList.contains(run))
                        userShopManagersPermissionsList.add(run);
                }
            } else synchronized (shopManagersPermissionsMap) {
                shopManagersPermissionsMap.put(targetUser, shopManagersPermissionsList);
            }
            return PermissionExist(shopManagersPermissionsList, targetUser);
        } else return false;
    }

    public boolean addPermissions(ShopManagersPermissions shopManagersPermissionsList, String targetUser) {
        List<ShopManagersPermissions> shopManagersPermissions = new LinkedList<>();
        if (shopManagersPermissionsList != null) {
            List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
            if (userShopManagersPermissionsList != null) {
                userShopManagersPermissionsList.add(shopManagersPermissionsList);
            } else synchronized (shopManagersPermissionsMap) {
                shopManagersPermissions.add(shopManagersPermissionsList);
                shopManagersPermissionsMap.put(targetUser, shopManagersPermissions);
            }
            return PermissionExist(shopManagersPermissions, targetUser);
        } else return false;
    }

    private boolean PermissionExist(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser) {
        boolean output = true;
        List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
        for (ShopManagersPermissions run : shopManagersPermissionsList)
            if (!userShopManagersPermissionsList.contains(run)) {
                output = false;
                errorLogger.logMsg(Level.WARNING, String.format(""));//TODO
            }
        return output;
    }

    public synchronized void testRestart() {
        shopManagersPermissionsMap.put("SystemManager", ArrayToListConversion(ShopManagersPermissions.values()));
    }

    private synchronized List<ShopManagersPermissions> ArrayToListConversion(ShopManagersPermissions[] shopManagersPermissions) {
        List<ShopManagersPermissions> output = new LinkedList<>();
        for (ShopManagersPermissions run : shopManagersPermissions)
            output.add(run);
        return output;
    }

    public List<ShopManagersPermissions> getPermissions(String managerUsername) {
        return shopManagersPermissionsMap.get(managerUsername);
    }

    public void initManager(String userName) {
        List<ShopManagersPermissions> init = new LinkedList<>();
        for (ShopManagersPermissions shopManagersPermissions : managerinit)
            init.add(shopManagersPermissions);
        shopManagersPermissionsMap.put(userName, init);
    }
}
