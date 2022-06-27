package domain.DAL;

import DB.HiberDB;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.shop.user.Cart;
import domain.shop.user.Role;
import domain.shop.user.SecurePasswordStorage;
import domain.shop.user.User;
import org.hibernate.cfg.NotYetImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerDAL {
    private static ControllerDAL instance = null;
    private HiberDB db;

    public ControllerDAL()
    {
        db = new HiberDB();
    }

    public void updateUser(User user) {
        db.updateUser(user);
    }

    public void deleteUser(String useID) {
        User u = getUser(useID);
        Map<Integer,List<Role>> map = u.getRoleList();
        db.deleteUser(u);
        db.deleteCart(u.getUserCart());
        for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
            for(Role r : entry.getValue()) {
                if (r == Role.ShopFounder){
                    List<Role> ls = new ArrayList<>();
                    ls.add(Role.ShopManager);
                    ls.add(Role.ShopFounder);
                    ls.add(Role.ShopOwner);
                    ls.add(Role.Shopper);
                    Shop shop = db.getShop(entry.getKey());
                    removeShopRoles(shop,ls);
                    db.deleteShop(shop);
            }

            }
        }
    }

    private void removeShopRoles(Shop shop, List<Role> ls) {
        db.removeShopRoles(shop,ls);
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
        db.updateCart(c);
    }

    public void deleteCart(Cart c){
        db.deleteCart(c);
    }
    public Cart getCart(String username)
    {
       return db.getCart(username);
    }

    public void saveShop(Shop shop)
    {
        db.saveShop(shop);
    }

    public void upDateShop(Shop shop){db.updateShop(shop);}

    public Shop getShop(int shopID)
    {
        return db.getShop(shopID);
    }

    public void deleteShop(Shop shop)
    {
        db.deleteShop(shop);
    }

    public void saveProduct(Product p)
    {
        db.saveProduct(p);
    }

    public ProductImp getProduct(int pID)
    {
        return db.getProduct(pID);
    }

    public void updateProduct(Product pi)
    {
        db.updateProduct(pi);
    }

    public void deleteProduct(ProductImp pi)
    {
        db.deleteProduct(pi);
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
