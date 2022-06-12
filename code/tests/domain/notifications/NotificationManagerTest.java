package domain.notifications;


import domain.Exceptions.BlankDataExc;
import domain.Exceptions.IncorrectIdentification;
import domain.Exceptions.InvalidAuthorizationException;
import domain.Exceptions.InvalidSequenceOperationsExc;
import domain.market.MarketSystem;
import domain.user.User;
import domain.user.UserController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class NotificationManagerTest {

    private final NotificationManager notificationManager = NotificationManager.getInstance();

    @BeforeEach
    void cleanUp(){
        for(String username : notificationManager.getObservers().keySet()){
            notificationManager.removeFromObserversList(username);
        }
    }

    @Test
    void removeFromObserversList() {
        Map<String, List<UserObserver>> observerMap = notificationManager.getObservers();
        String username = "user3";
        // case 1 no such user registered
        assertNull(observerMap.get(username));
        notificationManager.removeFromObserversList(username);
        assertNull(observerMap.get(username));

        // case 2 user is registered to the observer
        User user = new User(username);
        notificationManager.registerObserver(user, msg -> {});
        assertNotNull(observerMap.get(username));

        notificationManager.removeFromObserversList(username);
        assertNull(observerMap.get(username));

    }

    @Test
    void sendMessage() {
        User user = new User("user2");
        Map<User, List<Message>> userMessages = notificationManager.getUserMessages();
        assertNull(userMessages.get(user));
        notificationManager.sendMessage(user, "test message", user);
        assertNotNull(userMessages.get(user));
        assertEquals(1, userMessages.get(user).size());
        Message m = userMessages.get(user).get(0);
        assertEquals(user, m.getAddressee());
        assertEquals(user.getUserName(), m.getSender());
        assertEquals("test message", m.getContent());
        assertFalse(m.isRead());
    }

    @Test
    void registerObserver() {
        assertNull(notificationManager.getObservers().get("user1"));
        User u1 = new User("user1");
        UserObserver observer = msg -> {};
        notificationManager.registerObserver(u1, observer);
        assertEquals(observer, notificationManager.getObservers().get("user1").get(0));
    }

    @Test
    void registerAdminObserver() {
        var adminObservers = notificationManager.getAdminObservers();
        User admin1 = new User("admin1");
        AdminObserver observer = () -> {};
        assertNull(adminObservers.get(admin1));
        notificationManager.registerAdminObserver(admin1, observer);
        assertNotNull(adminObservers.get(admin1));
        assertEquals(1, adminObservers.get(admin1).size());
        assertEquals(observer, adminObservers.get(admin1).get(0));
    }

    @Test
    void notifyAdmin() throws InvalidSequenceOperationsExc, IncorrectIdentification, InvalidAuthorizationException, BlankDataExc {
        boolean[] notified = new boolean[1];
        User admin = new User("admin2");
        notificationManager.registerAdminObserver(admin, () -> notified[0] = true);
        notificationManager.notifyAdmin();
        User guest = MarketSystem.getInstance().EnterMarket();
        MarketSystem.getInstance().register(guest.getUserName(), "testUser", "pass");
        assertFalse(notified[0]);
        admin.login();
        MarketSystem.getInstance().login(guest.getUserName(), "testUser", "pass");
        assertTrue(notified[0]);
    }

    @Test
    void getNumberOfUnreadMessage() {
        User user = new User("user3");
        List<Message> messages = new ArrayList<>();

        // case 1 unregistered user
        assertEquals(0, notificationManager.getNumberOfUnreadMessage(user));

        // case 2 registered without messages
        notificationManager.registerObserver(user, messages::add);
        assertEquals(0, notificationManager.getNumberOfUnreadMessage(user));

        // case 3 registered with messages unread sent
        notificationManager.sendMessage(user, "testing messages", user);
        assertEquals(1, notificationManager.getNumberOfUnreadMessage(user));

        // case 4 after reading the message
        messages.get(0).markAsRead();
        assertEquals(0, notificationManager.getNumberOfUnreadMessage(user));
        messages.get(0).markAsRead();
        // case 5 reading message twice don't change the counter
        assertEquals(0, notificationManager.getNumberOfUnreadMessage(user));
    }
}