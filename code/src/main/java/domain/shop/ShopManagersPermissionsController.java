package domain.shop;

import domain.DAL.ControllerDAL;
import domain.ErrorLoggerSingleton;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class ShopManagersPermissionsController {
    private int shopId;
    private List<ShopManagersPermissions> managerinit = List.of(new ShopManagersPermissions[]{ShopManagersPermissions.AddProductToInventory, ShopManagersPermissions.RemoveProductFromInventory});
    private Map<String, List<ShopManagersPermissions>> shopManagersPermissionsMap;
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();
    private ControllerDAL controllerDAL = ControllerDAL.getInstance();


    public ShopManagersPermissionsController(int shopId){
        this.shopId = shopId;
        shopManagersPermissionsMap = new HashMap<>();
    }

    public boolean canAddProductToInventory(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AddProductToInventory);
    }

    public boolean canRemoveProductFromInventory(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RemoveProductFromInventory);
    }

    public boolean canChangeProductsDetail(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsDetail);
    }
    public boolean canChangeBuyingShopPolicy(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeBuyingShopPolicy);
    }
    public boolean canChangeDiscountShopPolicy(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeDiscountShopPolicy);
    }
    public boolean canChangeProductsBuyingShopPolicy(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsBuyingShopPolicy);
    }
    public boolean canChangeProductsDiscountShopPolicy(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeProductsDiscountShopPolicy);
    }
    public boolean canAppointNewShopOwner(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AppointNewShopOwner);
    }
    public boolean canAppointNewShopManager(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.AppointNewShopManager);
    }
    public boolean canChangeManagerPrivileges(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeManagerPrivileges);
    }
    public boolean canChangeShopManagersPermissions(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.ChangeShopManagersPermissions);
    }
    public boolean canCloseShop(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.CloseShop);
    }
    public boolean canOpenShop(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.OpenShop);
    }
    public boolean canDismissalOfStoreOwner(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.OpenShop);
    }
    public boolean canRequestInformationOnShopsOfficials(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RequestInformationOnShopsOfficials);
    }
    public boolean canRequestInformationOfShopsSalesHistory(String userId){
        return shopManagersPermissionsMap.get(userId).contains(ShopManagersPermissions.RequestInformationOfShopsSalesHistory);
    }
    public boolean removePermissions(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser) {
        if (shopManagersPermissionsList != null || shopManagersPermissionsList.size() > 0) {
            List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);

            if (userShopManagersPermissionsList != null) {
                for (ShopManagersPermissions run : shopManagersPermissionsList) {
                    if (userShopManagersPermissionsList.contains(run))
                        userShopManagersPermissionsList.remove(run);
                }
            } else synchronized (shopManagersPermissionsMap) {
                shopManagersPermissionsMap.put(targetUser, shopManagersPermissionsList);
            }
            if(PermissionNotExist(shopManagersPermissionsList, targetUser))
                controllerDAL.saveShopManagersPermissionsController(this);
            else return false;
            return true;
        } else return false;
    }

    private boolean PermissionNotExist(List<ShopManagersPermissions> shopManagersPermissionsList, String targetUser){
        boolean output = true;
        List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
        for (ShopManagersPermissions run : shopManagersPermissionsList)
            if(userShopManagersPermissionsList.contains(run)){
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
            } else synchronized (shopManagersPermissionsMap){shopManagersPermissionsMap.put(targetUser, shopManagersPermissionsList);}
            if(PermissionExist(shopManagersPermissionsList, targetUser))
                controllerDAL.saveShopManagersPermissionsController(this);
            else return false;
            return true;
        } else return false;
    }
    public boolean addPermissions(ShopManagersPermissions shopManagersPermissionsList, String targetUser) {
        List<ShopManagersPermissions> shopManagersPermissions = new LinkedList<>();
        if (shopManagersPermissionsList != null) {
            List<ShopManagersPermissions> userShopManagersPermissionsList = shopManagersPermissionsMap.get(targetUser);
            if (userShopManagersPermissionsList != null) {
                        userShopManagersPermissionsList.add(shopManagersPermissionsList);
            } else synchronized (shopManagersPermissionsMap){
                shopManagersPermissions.add(shopManagersPermissionsList);
                shopManagersPermissionsMap.put(targetUser, shopManagersPermissions);
            }
            if(PermissionExist(shopManagersPermissions, targetUser))
                controllerDAL.saveShopManagersPermissionsController(this);
            else return false;
            return true;
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

    public synchronized void testRestart(){
        shopManagersPermissionsMap.put("SystemManager",ArrayToListConversion(ShopManagersPermissions.values()));
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
        for(ShopManagersPermissions shopManagersPermissions : managerinit)
            init.add(shopManagersPermissions);
        shopManagersPermissionsMap.put(userName,init);
    }
}
