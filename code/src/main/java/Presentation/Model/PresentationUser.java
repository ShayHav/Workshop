package Presentation.Model;

import domain.shop.ShopManagersPermissions;
import domain.user.ManagerAppointment;
import domain.user.Role;
import domain.user.User;
import domain.user.UserState2;

import java.util.*;


public class PresentationUser {

    private String username;
    private String password;
    private boolean loggedIn;
    Map<Integer, List<Role>> roleList;
    Map<Integer, List<ShopManagersPermissions>> permissions;
    private List<ManagerAppointment> myAppointments;
    private UserState2 state;

    public PresentationUser(String username, boolean loggedIn) {
        this.username = username;
        this.loggedIn = loggedIn;
        permissions = new HashMap<>();
        roleList = new HashMap<>();
    }

    public PresentationUser(User user) {
        username = user.getUserName();
        loggedIn = user.isLoggedIn();
        permissions = new HashMap<>();
        roleList = user.getRoleList() == null ? new HashMap<>() : user.getRoleList();
        myAppointments = Collections.unmodifiableList(user.getManagerAppointeeList());
        state = user.getUs();
    }

    public PresentationUser() {
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

    public boolean hasOrdersPermissions(int shopID) {
        if (permissions == null)
            return false;
        if (roleList.containsKey(shopID)) {
            return isOwnerOrManagerAtShop(shopID);
        }

        if (permissions.containsKey(shopID)) {
            for (ShopManagersPermissions permissions : permissions.get(shopID)) {
                if (permissions == ShopManagersPermissions.RequestInformationOfShopsSalesHistory)
                    return true;
            }
        }
        return false;
    }

    public boolean hasInventoryPermission(int shopID) {
        if (permissions == null)
            return false;

        if (roleList.containsKey(shopID)) {
            return isOwnerOrManagerAtShop(shopID);
        }
        if (permissions.containsKey(shopID)) {
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

    public boolean isOwnerOrManagerAtShop(int shopID) {
        if (roleList.containsKey(shopID)) {
            List<Role> shopRoles = roleList.get(shopID);
            return shopRoles.contains(Role.ShopOwner) || shopRoles.contains(Role.ShopFounder);
        }
        return false;
    }

    public boolean isMyApointed(int shopID, PresentationUser user){
        for(ManagerAppointment appointment: myAppointments){
            if(appointment.getShop().getShopID() == shopID && user.equals(appointment.getAppointed()))
                return true;
        }
        return false;
    }

    public boolean hasRoleInShop(int shopID){
        if(roleList.containsKey(shopID)){
            return roleList.get(shopID).stream().anyMatch(role -> role.equals(Role.ShopFounder) || role.equals(Role.ShopOwner) || role.equals(Role.ShopManager));
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if(o.getClass() == PresentationUser.class) {
            PresentationUser that = (PresentationUser) o;
            return username.equals(that.username);
        }
        if(o.getClass() == User.class){
            return username.equals(((User) o).getUserName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    public boolean isAdmin(){
        return state == UserState2.systemManager;
    }


}
