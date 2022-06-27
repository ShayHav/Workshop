package domain.user.EntranceLogger;

import domain.market.MarketSystem;
import domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntranceLoggerTest {

    private static final MarketSystem market = MarketSystem.getInstance();
    private static final EntranceLogger logger = EntranceLogger.getInstance();

    @BeforeEach
    void setUp(){
        EntranceLogger.getInstance().getEntrances().clear();
    }

    @Test
    void logEntrance() {
        int entrancesNumber = logger.getEntrances().size();
        User user = new User("test");
        Entrance e = new Entrance(user, LocalDate.now());
        logger.logEntrance(e);
        assertEquals(entrancesNumber +1, logger.getEntrances().size());
    }

    @Test
    void getEntrances() {
        User user = new User("test");
        Entrance e = new Entrance(user, LocalDate.now());
        logger.logEntrance(e);
        List<Entrance> entranceList = logger.getEntrances(LocalDate.now(), LocalDate.now());
        assertEquals("test", entranceList.get(0).getEnteredUser().getUserName());
        assertTrue(entranceList.get(0).getDateOfEntrance().isEqual(LocalDate.now()));
    }
}