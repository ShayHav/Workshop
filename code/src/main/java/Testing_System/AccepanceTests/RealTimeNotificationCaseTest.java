package Testing_System.AccepanceTests;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertFalse;

/* https://github.com/ShayHav/Workshop/wiki/Use-Cases */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RealTimeNotificationCaseTest {

    @Test
    public void NotInV_1() {
        assertFalse(false);
    }
}