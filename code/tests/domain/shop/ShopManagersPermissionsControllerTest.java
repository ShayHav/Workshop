package domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopManagersPermissionsControllerTest {
    private ShopManagersPermissionsController shopManagersPermissionsController;
    /*private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();*/
    private String systemManager = "SystemManager";
    private List<ShopManagersPermissions> shopManagersPermissionsList;


    @BeforeEach
    void setUp(){
        shopManagersPermissionsController = new ShopManagersPermissionsController(0);
        shopManagersPermissionsController.testRestart();
        shopManagersPermissionsList = new LinkedList<>();
    }


    @Test
    void removePermissions() {
        shopManagersPermissionsList.add(ShopManagersPermissions.AppointNewShopManager);
        shopManagersPermissionsController.removePermissions(shopManagersPermissionsList,systemManager);
        assertFalse(shopManagersPermissionsController.canAppointNewShopManager(systemManager));
    }

   /* @Test
    void addPermissions() {
        shopManagersPermissionsList.add(ShopManagersPermissions.AddProductToInventory);
        shopManagersPermissionsController.addPermissions(shopManagersPermissionsList, userName[0]);
        assertTrue(shopManagersPermissionsController.canAddProductToInventory(userName[0]));
        assertFalse(shopManagersPermissionsController.canAppointNewShopManager(userName[0]));
    }*/
}