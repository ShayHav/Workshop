package DB;
import domain.user.User;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.EntityManagerFactory;



public class HiberDB {

    public HiberDB()
    {}

    public void saveUser(User u)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(u);
        /*
        persist === save to DB
        find === load from DB
         */
        User u = new User();

    }

    public User getUser(String username)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.find(User.class, username);
        /*
        persist === save to DB
        find === load from DB
         */
    }

    public void saveCart(Cart c)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(c);
        /*
        persist === save to DB
        find === load from DB
         */
    }
}
