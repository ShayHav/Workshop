package domain.shop;

import Service.Services;
import Testing_System.UserGenerator;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.Exceptions.ShopNotFoundException;
import domain.user.User;
import domain.user.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class AppointManagerNotifications {
    private UserController userController = UserController.getInstance();;
    private ShopController shopController = ShopController.getInstance();
    private final String [] nitayNames = {"Fashionista","StyleQueen","GlamourGal","DesignerDiva","SassySuitcase","HauteHippie","ModelMoment","CoutureCutie","FashionForward"};
    private final String [] nitayPassword = {"123456","123456789","12345","qwerty","password","12345678","111111","123123","1234567890"};
    private Services services = Services.getInstance();

    @BeforeEach
    public void SetUp()
    {
        List<Shop> shopList = shopController.getAllShops();
        for(Shop run : shopList)
            shopController.deleteShopTest(run.getShopID());
        try {
            userController.deleteUserTest(nitayNames);
        } catch (Exception e){
            fail();
        }
        for(int i = 0; i<nitayNames.length; i++) {
            String g = services.EnterMarket().getValue().getUserName();
            services.Register(g,nitayNames[i], nitayPassword[i]);
            services.Login(g,nitayNames[i], nitayPassword[i]);
        }
    }


    @Test
    void OneOwnerAppoint() {
        try{
            int shppID;
            shppID = services.CreateShop("NBAstore",nitayNames[0],"Nitay's").getValue().getShopID();
            services.AppointNewShopOwner(shppID,nitayNames[1],nitayNames[0]);
            assertTrue(shopController.getShop(shppID).isOwner(nitayNames[1]));
        }
        catch (Exception e){
            fail();
        }
    }

    @Test
    void MultiOwnerAppoint(){
        try{
            int shppID;
            shppID = services.CreateShop("NBAstore",nitayNames[0],"Nitay's").getValue().getShopID();
            services.AppointNewShopManager(shppID,nitayNames[1],nitayNames[0]);
            services.AppointNewShopManager(shppID,nitayNames[2],nitayNames[0]);
            services.acceptAppoint(shppID,2,nitayNames[1]);
            assertTrue(shopController.getShop(shppID).isOwner(nitayNames[2]));
        } catch (Exception e){
            fail();
        }
    }
    @Test
    void MultiOwnerAppointDecline(){
        try{
            int shppID;
            shppID = services.CreateShop("NBAstore",nitayNames[0],"Nitay's").getValue().getShopID();
            services.AppointNewShopManager(shppID,nitayNames[1],nitayNames[0]);
            services.AppointNewShopManager(shppID,nitayNames[3],nitayNames[0]);
            services.declineAppoint(shppID,2,nitayNames[1]);
            assertFalse(shopController.getShop(shppID).isOwner(nitayNames[3]));
        } catch (Exception e){
            fail();
        }
    }
}