package DB;

import domain.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HiberDBTest {

    private HiberDB db;
    private User u;
    private User founder;

    @BeforeAll
    public void Start()
    {
        founder = new User("FounderT");

    }

    @BeforeEach
    public void InitTest()
    {
        u = new User("Arielaf");
    }

    @AfterEach
    public void CleanTest()
    {
        db.deleteUser(u);
    }

    @Test
    public void insertUser()
    {
        try{db.saveUser(u);}catch(Exception e){fail(); }
        assertTrue(true);
    }

    @Test
    public void deleteUser()
    {
        db.saveUser(u);
        try{db.deleteUser(u);}catch (Exception e) { fail();}
        assertTrue(true);
    }

    @Test
    public void doubleUser()
    {
        db.saveUser(u);
        try{db.saveUser(u);} catch (Exception e) {assertTrue(true);}
        fail();
    }

    @Test
    public void addToCart()
    {

    }
}
