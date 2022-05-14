package Presentation.Model;

import domain.shop.ShopManagersPermissions;
import domain.user.User;

import java.util.List;

public class PresentationUser {

    private String username;
    private String password;
    private boolean loggedIn;
    List<ShopManagersPermissions> permissions;

    public PresentationUser(String username, boolean loggedIn){
        this.username = username;
        this.loggedIn = loggedIn;
        permissions = null;
    }

    public PresentationUser(User user){
        username = user.getUserName();
        loggedIn = user.isLoggedIn();
        permissions = null;
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

    public boolean hasInventoryPermission(){
        if(permissions == null){
            return false;
        }
        for(ShopManagersPermissions permissions : permissions){
            if(permissions == ShopManagersPermissions.AddProductToInventory ||
                permissions == ShopManagersPermissions.RemoveProductFromInventory ||
                permissions == ShopManagersPermissions.ChangeProductsDetail)
                return true;
        }
        return false;
    }

    public void setPermission(List<ShopManagersPermissions> value) {
        this.permissions = value;
    }
}
