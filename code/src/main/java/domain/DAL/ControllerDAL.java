package domain.DAL;

import DB.HiberDB;
import domain.notifications.Message;
import domain.shop.*;
import domain.shop.PurchasePolicys.PurchasePolicy;
import domain.shop.discount.DiscountPolicy;
import domain.user.*;
import domain.user.EntranceLogger.Entrance;
import org.hibernate.cfg.NotYetImplementedException;

import java.time.LocalDateTime;
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
        List<Order> ls = user.getOrderHistory();
        for (Order o : ls)
            o.initLs();
        db.updateOrdersForUser(ls);
        for (Order o : ls) {
            o.convertLs();
            o.cleanLs();
        }
    }
    public void deleteUser(String useID) {
        User u = getUser(useID);
        Map<Integer,List<Role>> map = u.getRoleList();
        db.deleteUser(u);
        db.deleteCart(u.getUserCart());
        for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
            for(Role r : entry.getValue()) {
                if (r == Role.ShopFounder){
//                    List<Role> ls = new ArrayList<>();
//                    ls.add(Role.ShopManager);
//                    ls.add(Role.ShopFounder);
//                    ls.add(Role.ShopOwner);
//                    ls.add(Role.Shopper);
                    Shop shop = db.getShop(entry.getKey());
//                    db.deleteShopRoles(shop.getShopID(),Role.Shopper);
//                    db.deleteShopRoles(shop.getShopID(),Role.ShopManager);
//                    db.deleteShopRoles(shop.getShopID(),Role.ShopOwner);
//                    db.deleteShop(shop);
                    deleteShop(shop);
                }

            }
        }
    }

    private void removeShopRoles(Shop shop, List<Role> ls) {
        db.removeShopRoles(shop,ls);
    }

//    public void deleteAllUser() {
//        throw new NotYetImplementedException();
//    }

    private static class DALHolder {
        private static final ControllerDAL dal = new ControllerDAL();
    }

    public static ControllerDAL getInstance() {
        return ControllerDAL.DALHolder.dal;
    }


    public void saveUser(User u) {
        db.saveUser(u);
        db.saveOrders(u.getOrderHistory());
        for(Order o : u.getOrderHistory())
            o.cleanLs();

    }

    public User getUser(String username)
    {
        User u = db.getUser(username);
        List<Order> ls = db.getOrderHistoryForUser(u.getUserName());
        for(Order o: ls) {
            o.convertLs();
            o.cleanLs();
        }
        u.setOrderHistory(ls);
        return u;
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
        db.saveShopRoles(shop.getShopID(),shop.getShopOwners(), Role.ShopOwner);
        db.saveShopRoles(shop.getShopID(),shop.getShopManagers(),Role.ShopManager);
        List<User> ls = new ArrayList<>();
        ls.add(shop.getShopFounder());
        db.saveShopRoles(shop.getShopID(),ls,Role.ShopFounder);
        db.saveManagerPermissions(shop.getShopManagersPermissionsController());
        db.saveInventoy(shop.getInventory());
        for(Map.Entry<Integer,ProductImp> entry : shop.getInventory().getKeyToProduct().entrySet() ) {
            entry.getValue().setShopID(shop.getShopID());
            db.saveProduct(entry.getValue());
        }
        for(Order o :shop.getOrders())
            o.initLs();
        db.saveOrders(shop.getOrders());
        for(Order o :shop.getOrders())
            o.cleanLs();

    }

    public void upDateShop(Shop shop){
        db.updateShop(shop);
        db.updateShopRoles(shop.getShopID(),shop.getShopOwners(), Role.ShopOwner);
        db.updateShopRoles(shop.getShopID(),shop.getShopManagers(),Role.ShopManager);
        db.updateManagerPermissions(shop.getShopManagersPermissionsController());
        db.updateInventory(shop.getInventory());
        for(Map.Entry<Integer,ProductImp> entry : shop.getInventory().getKeyToProduct().entrySet() )
            db.updateProduct(entry.getValue());
        for(Order o :shop.getOrders())
          o.initLs();
        db.updateOrdersForUser(shop.getOrders());
        for(Order o :shop.getOrders()) {
            o.convertLs();
            o.cleanLs();
        }
            }

    public Shop getShop(int shopID)
    {
        Shop shop = db.getShop(shopID);
        shop.setShopManagers(db.getShopRoles(shopID,Role.ShopManager));
        shop.setShopOwners(db.getShopRoles(shopID,Role.ShopOwner));
        shop.setShopManagersPermissionsController(db.getShopMangersPermissionsController(shopID));
        shop.setInventory(db.getInventory(shopID));
        OrderHistory oh = db.getOrderForShop(shopID);
        for(Order o : oh.getOrders())
        {
            o.convertLs();
            o.cleanLs();
        }
        shop.setOrders(oh);
        return shop;
    }

    public void deleteShop(Shop shop)
    {
        db.deleteShopRoles(shop.getShopID(),Role.ShopOwner);
        db.deleteShopRoles(shop.getShopID(),Role.ShopManager);
        db.deleteShopRoles(shop.getShopID(),Role.ShopFounder);
        db.deleteShopManagersPermissions(shop.getShopID());
        db.deleteInventory(shop.getShopID());
        db.deleteProductShopId(shop.getShopID());
        db.deleteShop(shop);
    }

    public void deleteProductShopId(int shopID)
    {        db.deleteProductShopId(shopID);
    }


    public void saveProduct(ProductImp p,int shopID)
    {
        p.setShopID(shopID);
        db.saveProduct(p);
    }

    public ProductImp getProduct(int pID,int shopID)
    {

        return db.getProduct(pID,shopID);
    }

    public void updateProduct(ProductImp pi,int shopID)
    {
        pi.setShopID(shopID);
        db.updateProduct(pi);
    }

    public void deleteProduct(ProductImp pi,int shopID)
    {
        pi.setId(shopID);
        db.deleteProduct(pi);
    }

    public List<Order> getOrderByUser(String username)
    {
        return db.getOrderHistoryForUser(username);
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
        db.saveInventoy(inventory);
    }

    public void upDateInventory(Inventory inventory){
        db.updateInventory(inventory);
    }

    public void saveOrderHistory(OrderHistory order){
        for(Order o : order.getOrders())
            o.initLs();
        db.saveOrders(order.getOrders());
        for(Order o : order.getOrders())
            o.cleanLs();
    }

    public void upDateOrderHistory(OrderHistory order) {
        for (Order o : order.getOrders())
            o.initLs();
        db.updateOrdersForUser(order.getOrders());
        for (Order o : order.getOrders()){
            o.convertLs();
            o.cleanLs();
    }
    }
    public void saveOrder(Order order){
        List<Order>ls = new ArrayList<>();
        order.initLs();
        ls.add(order);
        db.updateOrdersForUser(ls);
        order.cleanLs();
    }

    public void upDateOrder(Order order){
        List<Order>ls = new ArrayList<>();
        ls.add(order);
        order.initLs();
        db.updateOrdersForUser(ls);
        order.convertLs();
        order.cleanLs();
    }

    public void saveSecurePasswordStorage(SecurePasswordStorage securePasswordStorage) {
        db.saveSecurePasswordStorage(securePasswordStorage);
    }
    public void updateSecurePasswordStorage(SecurePasswordStorage securePasswordStorage){
        db.updateSecurePasswordStorage(securePasswordStorage);
    }

    public SecurePasswordInt getSecurePasswordStorage(){
        return db.getSecurePasswordStorage();
    }

    public void saveMessage(Message m){db.saveMessage(m);}

    public Message getMessage(String sender, String reciver, LocalDateTime time)
    {return db.getMessage(sender,reciver,time);}

    public List<Message> getAllMessageBySender(String sender){
        return db.getAllMessageBySender(sender);
    }

    public List<Message> getAllMessageByReciver(String reciver)
    {
       return db.getAllMessageByReciver(reciver);
    }

    public void updateMessage(Message m)
    {
        db.updateMessage(m);
    }

    public void saveEntarnce(Entrance e)
    {
        db.saveEntrance(e);
    }

    public List<Entrance> getEntarenceByUser(String username)
    {
        List<Entrance> ls = db.getEntarenceByUser(username);
        for(Entrance e : ls)
        {
            User u = db.getUser(e.getUsername());
            e.setEnteredUser(u);
        }

        return ls;
    }

    public List<Entrance> getEntarenceByDate(LocalDateTime time)
    {
        List<Entrance> ls = db.getEntarenceByDate(time);
        for(Entrance e : ls)
        {
            User u = db.getUser(e.getUsername());
            e.setEnteredUser(u);
        }

        return ls;
    }
}
