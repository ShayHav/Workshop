package DB;
import domain.user.User;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;



public class HiberDB {

    public HiberDB()
    {}

    public void add()
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        /*
        persist === save to DB
        find === load from DB
         */
        User u = new User();

    }
}
