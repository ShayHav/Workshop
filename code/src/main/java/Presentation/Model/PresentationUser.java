package Presentation.Model;

import domain.shop.ShopManagersPermissions;
import domain.user.Role;
import domain.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentationUser {

    private String username;
    private String password;
    private boolean loggedIn;
    Map<Integer, List<Role>> roleList;
    Map<Integer, List<ShopManagersPermissions>> permissions;

    public PresentationUser(String username, boolean loggedIn){
        this.username = username;
        this.loggedIn = loggedIn;
        permissions = new HashMap<>();
        roleList = new HashMap<>();
    }

    public PresentationUser(User user){
        username = user.getUserName();
        loggedIn = user.isLoggedIn();
        permissions = new HashMap<>();
        roleList = user.getRoleList();
    }

    public PresentationUser(){
        permissions = new HashMap<>();
        roleList = new HashMap<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean hasInventoryPermission(int shopID){
        if(permissions == null){
            return false;
        }
        if(roleList.containsKey(shopID)){
            List<Role> shopRoles = roleList.get(shopID);
            if(shopRoles.contains(Role.ShopOwner) || shopRoles.contains(Role.ShopFounder))
                return true;
        }
        if(permissions.containsKey(shopID)) {
            for (ShopManagersPermissions permissions : permissions.get(shopID)) {
                if (permissions == ShopManagersPermissions.AddProductToInventory ||
                        permissions == ShopManagersPermissions.RemoveProductFromInventory ||
                        permissions == ShopManagersPermissions.ChangeProductsDetail)
                    return true;
            }
        }
        return false;
    }

    public void setPermission(int shopID, List<ShopManagersPermissions> value) {
        permissions.put(shopID, value);
    }

}
