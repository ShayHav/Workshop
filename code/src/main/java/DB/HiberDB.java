package DB;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;



public class HiberDB {

    public void add()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("");
        EntityManager entityManager = emf.createEntityManager();
    }
}
