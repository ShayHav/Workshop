package SystemManager;

import domain.Exceptions.*;
import domain.market.MarketSystem;

import domain.user.User;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class SystemManagerPageTest {

    private final MarketSystem market = MarketSystem.getInstance();

    public SystemManagerPageTest() {
        try {
            market.start(null, null);
            User adminGuest = market.EnterMarket();
            market.login(adminGuest.getUserName(), "Admin", "Admin");
        } catch (InvalidSequenceOperationsExc | IncorrectIdentification | BlankDataExc | InvalidAuthorizationException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUsers() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        User guest = market.EnterMarket();
        market.register(guest.getUserName(), "test", "test");
        Map<Integer, User> users = market.getAllUsers("Admin");
        List<String> username = users.values().stream().map(User::getUserName).collect(Collectors.toList());
        assertTrue(username.contains("test"));

        market.deleteUser("Admin", "test");
        market.LeaveMarket(guest.getUserName());
    }

    @Test
    public void testActiveUsers() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        int activeUsers = market.getCurrentActiveUsers("Admin");
        User guest = market.EnterMarket();
        assertEquals(activeUsers + 1, market.getCurrentActiveUsers("Admin"));

        activeUsers += 1;
        market.register(guest.getUserName(), "test", "test");
        market.login(guest.getUserName(), "test", "test");

        //since active users deletes former instance of guest, the number should remain the same
        assertEquals(activeUsers, market.getCurrentActiveUsers("Admin"));


        guest = market.logout("test");
        market.LeaveMarket(guest.getUserName());
        market.deleteUser("Admin", "test");
    }

    @Test
    public void testActiveMembers() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        int activeMembers = market.getCurrentActiveMembers("Admin");
        User guest = market.EnterMarket();

        market.register(guest.getUserName(), "test", "test");
        market.login(guest.getUserName(), "test", "test");

        assertEquals(activeMembers + 1, market.getCurrentActiveMembers("Admin"));

        guest = market.logout("test");
        market.LeaveMarket(guest.getUserName());
        market.deleteUser("Admin", "test");
    }

    @Test
    public void testActiveGuests() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc {
        int activeGuests = market.getCurrentActiveGuests("Admin");
        User guest = market.EnterMarket();
        assertEquals(activeGuests + 1, market.getCurrentActiveGuests("Admin"));

        market.LeaveMarket(guest.getUserName());
    }

    @Test
    public void testTotalRegistered() throws IncorrectIdentification, InvalidSequenceOperationsExc, InvalidAuthorizationException, BlankDataExc, ShopNotFoundException {
        int totalRegistered = market.getTotalMembers("Admin");
        User guest = market.EnterMarket();
        market.register(guest.getUserName(), "test1", "test");
        assertEquals(totalRegistered + 1, market.getTotalMembers("Admin"));

        market.deleteUser("Admin", "test1");
        market.LeaveMarket(guest.getUserName());
    }
}
