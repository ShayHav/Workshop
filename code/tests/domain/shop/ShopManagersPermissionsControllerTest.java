package domain.shop;

import Testing_System.UserGenerator;
import domain.user.UserController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShopManagersPermissionsControllerTest {
    private ShopManagersPermissionsController shopManagersPermissionsController;
    private UserController userController;
    private UserGenerator userGenerator = new UserGenerator();
    private String[] userName = userGenerator.GetValidUsers();
    private String[] userPass = userGenerator.GetPW();
    private String[] badPass = userGenerator.GetBadPW();

    @BeforeEach
    void setUp(){
        shopManagersPermissionsController = new ShopManagersPermissionsController();
        userController = UserController.getInstance();
        for(int i=0;i<userName.length;i++) {
            userController.register(userName[i], userPass[i]);
        }
    }

    @Test
    void canAddProductToInventory() {
    }

    @Test
    void canRemoveProductFromInventory() {
    }

    @Test
    void canChangeProductsDetail() {
    }

    @Test
    void canChangeBuyingShopPolicy() {
    }

    @Test
    void canChangeDiscountShopPolicy() {
    }

    @Test
    void canChangeProductsBuyingShopPolicy() {
    }

    @Test
    void canChangeProductsDiscountShopPolicy() {
    }

    @Test
    void canAppointNewShopOwner() {
    }

    @Test
    void canAppointNewShopManager() {
    }

    @Test
    void canChangeManagerPrivileges() {
    }

    @Test
    void canChangeShopManagersPermissions() {
    }

    @Test
    void canCloseShop() {
    }

    @Test
    void canRequestInformationOnShopsOfficials() {
    }

    @Test
    void canRequestInformationOfShopsSalesHistory() {
    }

    @Test
    void removePermissions() {
    }

    @Test
    void addPermissions() {
        shopManagersPermissionsController.
    }
}