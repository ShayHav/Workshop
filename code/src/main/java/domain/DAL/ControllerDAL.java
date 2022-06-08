package domain.DAL;

import DB.HiberDB;
import domain.shop.Product;
import domain.shop.Shop;
import domain.user.Cart;
import domain.user.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ControllerDAL {
    private HiberDB db;

    public ControllerDAL()
    {
        db = new HiberDB();
    }

    public void saveUser(User u)
    {
        db.saveUser(u);

    }

    public User getUser(String username)
    {
        return db.getUser(username);
    }

    public void saveCart(Cart c)
    {
        db.saveCart(c);
    }

    public Cart getCart(String username)
    {
       return db.getCart(username);
    }

    public void saveShop(Shop shop)
    {
        db.saveShop(shop);
    }

    public Shop getShop(int shopID)
    {
        return db.getShop(shopID);
    }

    public void saveProduct(Product p)
    {
        saveProduct(p);
    }

    public Product getProduct(int pID)
    {
        return getProduct(pID);
    }

}
