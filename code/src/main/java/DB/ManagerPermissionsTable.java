package DB;

import domain.shop.ShopManagersPermissions;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.lang.management.ManagementPermission;

@Entity
public class ManagerPermissionsTable {
    @Id
    private int shopID;
    @Id
    private String username;
    @Enumerated(EnumType.STRING)
    private ShopManagersPermissions permission;

    public ManagerPermissionsTable(int shop, String name, ShopManagersPermissions per)
    {
        shopID = shop;
        username = name;
        permission = per;
    }

    public ManagerPermissionsTable() {

    }

    public ManagerPermissionsTable merge(ManagerPermissionsTable mpt)
    {
        setPermission(mpt.getPermission());
        setShopID(mpt.getShopID());
        setUsername(mpt.getUsername());
        return this;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ShopManagersPermissions getPermission() {
        return permission;
    }

    public void setPermission(ShopManagersPermissions permission) {
        this.permission = permission;
    }
}
