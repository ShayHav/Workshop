package domain.DAL;

import DB.HiberDB;
import domain.market.MarketSystem;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.Cart;
import domain.user.SecurePasswordStorage;
import domain.user.User;
import domain.user.UserController;
import org.hibernate.cfg.NotYetImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ControllerDAL {
    private static ControllerDAL instance = null;
    private HiberDB db;

    public ControllerDAL()
    {
        db = new HiberDB();
    }

    public void updateUser(User user) {
        throw new NotYetImplementedException();
    }

    public void deleteUser(String useID) {
        throw new NotYetImplementedException();
    }

    public void deleteAllUser() {
        throw new NotYetImplementedException();
    }

    private static class DALHolder {
        private static final ControllerDAL dal = new ControllerDAL();
    }

    public static ControllerDAL getInstance() {
        return ControllerDAL.DALHolder.dal;
    }


    public void saveUser(User u) {
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

    public void updateCart(Cart c)
    {
        throw new NotYetImplementedException();
    }

    public Cart getCart(String username)
    {
       return db.getCart(username);
    }

    public void saveShop(Shop shop)
    {
        db.saveShop(shop);
    }

    public void upDateShop(Shop shop){throw new NotYetImplementedException();}

    public Shop getShop(int shopID)
    {
        return db.getShop(shopID);
    }

    public void saveProduct(Product p)
    {
        saveProduct(p);
    }

    public ProductImp getProduct(int pID)
    {
        return getProduct(pID);
    }

    public Order getOrderByUser(String username)
    {
        throw new NotYetImplementedException();
    }

    public void saveShopManagersPermissionsController(ShopManagersPermissionsController shopManagersPermissionsController){throw new NotYetImplementedException();}

    public void saveDiscountPolicy(DiscountPolicy discountPolicy){
        throw new NotYetImplementedException();
    }

    public void upDateDiscountPolicy(DiscountPolicy discountPolicy){
        throw new NotYetImplementedException();
    }

    public void savePurchasePolicy(PurchasePolicy purchasePolicy){
        throw new NotYetImplementedException();
    }

    public void upDatePurchasePolicy(PurchasePolicy purchasePolicy){
        throw new NotYetImplementedException();
    }
    public void saveInventory(Inventory inventory){
        throw new NotYetImplementedException();
    }

    public void upDateInventory(Inventory inventory){
        throw new NotYetImplementedException();
    }

    public void saveOrderHistory(OrderHistory order){
        throw new NotYetImplementedException();
    }

    public void upDateOrderHistory(OrderHistory order){
        throw new NotYetImplementedException();
    }
    public void saveOrder(Order order){
        throw new NotYetImplementedException();
    }

    public void upDateOrder(Order order){
        throw new NotYetImplementedException();
    }

    public void saveSecurePasswordStorage(SecurePasswordStorage securePasswordStorage) {
        throw new NotYetImplementedException();
    }
    public void updateSecurePasswordStorage(SecurePasswordStorage securePasswordStorage){
        throw new NotYetImplementedException();
    }

    public SecurePasswordStorage getSecurePasswordStorage(){
        throw new NotYetImplementedException();
    }


}
