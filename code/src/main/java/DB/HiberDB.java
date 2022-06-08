package DB;
import domain.shop.Product;
import domain.shop.Shop;
import domain.user.Cart;
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

    }

    public User getUser(String username)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager.find(User.class, username);
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

    public Cart getCart(String username)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager.find(Cart.class, username);
    }

    public void saveShop(Shop shop)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(shop);
    }

    public Shop getShop(int shopID)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager.find(Shop.class, shopID);
    }

    public void saveProduct(Product p)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(p);
    }

    public Product getProduct(int pID)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        return entityManager.find(Product.class, pID);
    }

}
