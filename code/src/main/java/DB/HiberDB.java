package DB;
import domain.Exceptions.NotFoundInDBException;
import domain.Exceptions.ProductNotFoundException;
import domain.notifications.Message;
import domain.shop.*;
import domain.user.*;
import domain.user.OwnerAppointment;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiberDB {
    private String shopOMT = "shop_owners_managers";

    public HiberDB() {
        initDB();
    }

    private void initDB() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityTransaction txn = null;
        try {
            EntityManager entityManager = emf.createEntityManager();
            txn = entityManager.getTransaction();
            txn.begin();
            entityManager.createNativeQuery("create table shop_owners_managers (id varchar(255) not null, primary key (id)) engine=InnoDB").executeUpdate();

            txn.commit();
        } catch (Throwable e) {
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
            throw e;
        }
    }

    public void saveUser(User u) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            em.persist(u);
            Map<Integer, List<Role>> map = u.getRoleList();
            for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
                for (Role role : entry.getValue()) {
                    roles_table r = new roles_table(u.getUserName(), entry.getKey(), role);
                    em.persist(r);
                }
            }
            for (ManagerAppointment ma : u.getManagerAppointeeList()) {
                Appointments a = new Appointments(ma.getAppointed().getUserName(), ma.getShop().getShopID(), u.getUserName(), Role.ShopManager);
                em.persist(a);
            }
            for (OwnerAppointment oa : u.getOwnerAppointmentList()) {
                Appointments a = new Appointments(oa.getAppointed().getUserName(), oa.getShop().getShopID(), u.getUserName(), Role.ShopOwner);
                em.persist(a);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public User getUser(String username) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        User u;
        try {
            et.begin();
            u = em.find(User.class, username);
            List<roles_table> rt = em.createQuery("select e from roles_table e where e.username = :status",
                    roles_table.class).setParameter("status", u.getUserName()).getResultList();
            Map<Integer, List<Role>> roles = new HashMap<>();
            if (rt != null) {
                for (roles_table temp : rt) {
                    if (roles.containsKey(temp.getShopid()))
                        roles.get(temp.getShopid()).add(temp.getR());
                    else {
                        List<Role> ls = new ArrayList<>();
                        ls.add(temp.getR());
                        roles.putIfAbsent(temp.getShopid(), ls);
                    }
                }
            }
            u.setRoleList(roles);
            //appointee lists
            List<Appointments> aLs = em.createQuery("select e from Appointments e where e.myappointee = :status",
                    Appointments.class).setParameter("status", username).getResultList();
            List<OwnerAppointment> oaLs = new ArrayList<>();
            List<ManagerAppointment> maLs = new ArrayList<>();
            for (Appointments a : aLs) {
                if (a.getMyrole() == Role.ShopManager) {
                    Shop shop = em.find(Shop.class, a.getShopID());
                    User temp = em.find(User.class, a.getUsername());
                    ManagerAppointment ma = new ManagerAppointment(shop, u, temp);
                    maLs.add(ma);
                } else if (a.getMyrole() == Role.ShopOwner) {
                    Shop shop = em.find(Shop.class, a.getShopID());
                    User temp = em.find(User.class, a.getUsername());
                    OwnerAppointment oa = new OwnerAppointment(shop, u.getUserName(), temp);
                    oaLs.add(oa);
                }
            }
            u.setManagerAppointeeList(maLs);
            u.setOwnerAppointmentList(oaLs);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }

        return u;
    }

    public void updateUser(@NotNull User u) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            User ut = em.find(User.class, u.getUserInfo().getUserId());
            ut.merge(u);
            Map<Integer, List<Role>> map = ut.getRoleList();
            for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
                for (Role r : entry.getValue()) {
                    roles_table temp = new roles_table(u.getUserName(), entry.getKey(), r);
                    roles_table rt = em.find(roles_table.class, temp);
                    rt.merge(temp);
                }
            }

            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteUser(User u) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Map<Integer, List<Role>> map = u.getRoleList();
            for (Map.Entry<Integer, List<Role>> entry : map.entrySet()) {
                for (Role r : entry.getValue()) {
                    roles_table temp = new roles_table(u.getUserName(), entry.getKey(), r);
                    em.remove(temp);
                }
            }
            em.remove(u);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveCart(Cart c) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(c);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public Cart getCart(String username) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Cart c;
        try {
            et.begin();
            c = em.find(Cart.class, username);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return c;
    }

    public void updateCart(@NotNull Cart c) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Cart toupdate = em.find(Cart.class, c.getUsername());
            toupdate.merge(c);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteCart(Cart c) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.remove(c);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveShop(Shop shop) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(shop);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public Shop getShop(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Shop shop;
        try {
            et.begin();
            shop = em.find(Shop.class, shopID);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return shop;
    }

    public void updateShop(@NotNull Shop shop) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Shop st = em.find(Shop.class, shop.getShopID());
            st.merge(shop);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteShop(Shop shop) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.remove(shop);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveProduct(Product p) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(p);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public ProductImp getProduct(int pID, int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        ProductImp p = new ProductImp();
        p.setShopID(shopID);
        p.setId(pID);
        try {
            et.begin();
            p = em.find(ProductImp.class, p);
//            if(p == null)
//                p = em.find(ProductHistory.class,pID);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return p;
    }

    public void updateProduct(@NotNull Product pi) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ProductImp st = em.find(ProductImp.class, pi.getId());
            st.merge(pi);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteProduct(ProductImp pi) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.remove(pi);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void removeShopRoles(Shop shop, List<Role> ls) {
    }

    public void saveSecurePasswordStorage(SecurePasswordStorage securePasswordStorage) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (UserInfo ui : securePasswordStorage.getUserDatabase())
                em.persist(ui);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void updateSecurePasswordStorage(SecurePasswordStorage securePasswordStorage) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (UserInfo ui : securePasswordStorage.getUserDatabase()) {
                UserInfo toUpdate = em.find(UserInfo.class, ui.getUserid());
                toUpdate.merge(ui);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public SecurePasswordInt getSecurePasswordStorage() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        SecurePasswordInt sps = new SecurePasswordInt();
        try {
            et.begin();
            List<UserInfo> uiLs = em.createQuery("select e from UserInfo e").getResultList();
            Map<String, UserInfo> uiMap = new HashMap<>();
            for (UserInfo ui : uiLs)
                uiMap.putIfAbsent(ui.getUserid(), ui);
            sps.setMap(uiMap);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return sps;
    }

    public void saveOrders(List<Order> orderHistory) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (Order o : orderHistory) {
                o.initLs();
                em.persist(o);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public List<Order> getOrderHistoryForUser(String userName) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        List<Order> ls = new ArrayList<>();
        try {
            et.begin();
            ls = em.createQuery("select e from Order e where e.userID = :status",
                    Order.class).setParameter("status", userName).getResultList();

            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return ls;
    }

    public void updateOrdersForUser(List<Order> ls) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (Order o : ls) {
                Order toUpdate = em.find(Order.class, o.getOrderId());
                toUpdate.merge(o);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveShopRoles(int shopID, List<User> ls,Role r) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (User u : ls) {
                roles_table rt = new roles_table(u.getUserName(), shopID, r);
                em.persist(rt);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveManagerPermissions(ShopManagersPermissionsController shopManagersPermissionsController) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for(Map.Entry<String,List<ShopManagersPermissions>> entry : shopManagersPermissionsController.getShopManagersPermissionsMap().entrySet() ) {
              for(ShopManagersPermissions smp : entry.getValue()) {
                  ManagerPermissionsTable mpt = new ManagerPermissionsTable(shopManagersPermissionsController.getShopID(),entry.getKey(),smp);
                  em.persist(mpt);
              }
            }
            et.commit();

        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }

    }

    public void saveInventoy(Inventory inventory) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for(Map.Entry<Integer,ProductImp> entry : inventory.getKeyToProduct().entrySet() ) {
                InventoryTable it = new InventoryTable(inventory.getShopID(),entry.getKey(), inventory.getQuantity(entry.getValue().getId()));
                em.persist(it);
            }
            et.commit();

        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void updateShopRoles(int shopID, List<User> ls, Role r) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for (User u : ls) {
                roles_table rt = new roles_table(u.getUserName(), shopID, r);
                roles_table toUpdate = em.find(roles_table.class,rt);
                toUpdate.merge(rt);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void updateManagerPermissions(ShopManagersPermissionsController shopManagersPermissionsController) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for(Map.Entry<String,List<ShopManagersPermissions>> entry : shopManagersPermissionsController.getShopManagersPermissionsMap().entrySet() ) {
                for(ShopManagersPermissions smp : entry.getValue()) {
                    ManagerPermissionsTable mpt = new ManagerPermissionsTable(shopManagersPermissionsController.getShopID(),entry.getKey(),smp);
                    ManagerPermissionsTable toUpdate = em.find(ManagerPermissionsTable.class,mpt);
                    if(toUpdate.getPermission() != mpt.getPermission())
                        toUpdate.merge(mpt);
                }
            }
            et.commit();

        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void updateInventory(Inventory inventory) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            for(Map.Entry<Integer,ProductImp> entry : inventory.getKeyToProduct().entrySet() ) {
                InventoryTable it = new InventoryTable(inventory.getShopID(),entry.getKey(), inventory.getQuantity(entry.getValue().getId()));
                InventoryTable toUpdate = em.find(InventoryTable.class,it);
                toUpdate.merge(it);
            }
            et.commit();

        } catch (ProductNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public List<User> getShopRoles(int shopID, Role r) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        List<User> res = new ArrayList<>();
        try {
            et.begin();
            List<roles_table> rt = em.createQuery("select e from roles_table e where e.shopid = :status AND e.role = :role",
                    roles_table.class).setParameter("status", shopID).setParameter("role", r).getResultList();
            for(roles_table roles : rt)
            {
                User u = getUser(roles.getUsername());
                if(u!=null)
                    res.add(u);
            }
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return res;
    }

    public ShopManagersPermissionsController getShopMangersPermissionsController(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        ShopManagersPermissionsController smpc = new ShopManagersPermissionsController();
        Map<String, List<ShopManagersPermissions>> map = new HashMap<>();
        List<ManagerPermissionsTable> smpLs;
        try {
            et.begin();
            smpLs = em.createQuery("select e from ManagerPermissionsTable e where e.shopID = :status",
                    ManagerPermissionsTable.class).setParameter("status", shopID).getResultList();

            et.commit();

        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        for (ManagerPermissionsTable mpt : smpLs) {
            if (map.containsKey(mpt.getUsername())) {
                map.get(mpt.getUsername()).add(mpt.getPermission());
            } else {
                List<ShopManagersPermissions> ls = new ArrayList<>();
                ls.add(mpt.getPermission());
                map.putIfAbsent(mpt.getUsername(), ls);
            }
        }
        smpc.setShopManagersPermissionsMap(map);
        return smpc;
    }

    public Inventory getInventory(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Inventory inv = new Inventory();
        inv.setShopID(shopID);
        Map<Integer, ProductImp> map= new HashMap<>();
        List<InventoryTable> ls;
        try {
            et.begin();
            ls = em.createQuery("select e from InventoryTable e where e.shopID = :status",
                    InventoryTable.class).setParameter("status", shopID).getResultList();
            for(InventoryTable it : ls)
            {
                ProductImp pi = em.find(ProductImp.class,it.getProductID());
                if(pi !=null)
                {
                    if(!map.containsKey(pi.getId()))
                        map.putIfAbsent(pi.getId(),pi);
                    else
                        throw new NotFoundInDBException("double entries for product in productImp table");
                }
            }
            et.commit();

        } catch (NotFoundInDBException e) {
            inv = null;
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        if(inv!=null)
            inv.setKeyToProduct(map);
        return inv;
    }

    public OrderHistory getOrderForShop(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        OrderHistory res = new OrderHistory();
        List<Order> ls = new ArrayList<>();
        try {
            et.begin();
            ls = em.createQuery("select e from Order e",
                    Order.class).getResultList();

            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        long i =0;
        for(Order o : ls)
        {
            if(o.getShopID() == shopID)
                res.addOrderDB(o);
            i++;
        }
        res.setOrderGen(i);
        return res;
    }

    public void deleteShopRoles(int shopID, Role r) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery(
                    "DELETE FROM roles_table e WHERE e.shopid = :status AND e.role = : r");
            query.setParameter("status", shopID).setParameter("r",r).executeUpdate();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }

    }

    public void deleteShopManagersPermissions(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery(
                    "DELETE FROM ManagerPermissionsTable e WHERE e.shopID = :status");
            query.setParameter("status", shopID).executeUpdate();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteInventory(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery(
                    "DELETE FROM InventoryTable e WHERE e.shopID = :status");
            query.setParameter("status", shopID).executeUpdate();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteProductShopId(int shopID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Query query = em.createQuery(
                    "DELETE FROM ProductImp e WHERE e.shopID = :status");
            query.setParameter("status", shopID).executeUpdate();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveMessage(Message m) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(m);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public Message getMessage(String sender, String reciver, LocalDateTime time) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Message m = new Message();
        m.setSentDate(time);
        m.setReciver(reciver);
        m.setSender(sender);
        try {
            et.begin();
            m = em.find(Message.class,m);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return m;
    }

    public List<Message> getAllMessageBySender(String sender) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        List<Message> ls = new ArrayList<>();
        try {
            et.begin();
            ls = em.createQuery("select e from Message e where e.sender = :status",
                    Message.class).setParameter("status", sender).getResultList();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return ls;
    }

    public List<Message> getAllMessageByReciver(String reciver) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        List<Message> ls = new ArrayList<>();
        try {
            et.begin();
            ls = em.createQuery("select e from Message e where e.reciver = :status",
                    Message.class).setParameter("status", reciver).getResultList();
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return ls;
    }

    public void updateMessage(Message m) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            Message toUpdate = em.find(Message.class,m);
            toUpdate.merge(m);
            et.commit();
        } finally {
            if (et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }
}
