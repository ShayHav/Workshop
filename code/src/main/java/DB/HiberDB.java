package DB;
import domain.shop.*;
import domain.user.*;
import domain.user.OwnerAppointment;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiberDB {

    public HiberDB()
    {}

    public void saveUser(User u)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();

        try
        {
            et.begin();
            em.persist(u);
            Map<Integer,List<Role>> map = u.getRoleList();
            for (Map.Entry<Integer,List<Role>> entry : map.entrySet()) {
                for(Role role : entry.getValue())
                {
                    roles_table r = new roles_table(u.getUserName(),entry.getKey(),role);
                    em.persist(r);
                }
            }
            for(ManagerAppointment ma : u.getManagerAppointeeList())
            {
                Appointments a = new Appointments(ma.getAppointed().getUserName(),ma.getShop().getShopID(),u.getUserName(),Role.ShopManager);
                em.persist(a);
            }
            for(OwnerAppointment oa : u.getOwnerAppointmentList())
            {
                Appointments a = new Appointments(oa.getAppointed().getUserName(),oa.getShop().getShopID(),u.getUserName(),Role.ShopOwner);
                em.persist(a);
            }
            et.commit();
        }finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public User getUser(String username)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        User u;
        try
        {
            et.begin();
            u = em.find(User.class, username);
            List<roles_table> rt = em.createQuery("select e from roles_table e where e.username = :status",
                    roles_table.class).setParameter("status", u.getUserName()).getResultList();
            Map<Integer,List<Role>> roles = new HashMap<>();
            if(rt!=null)
            {
                for(roles_table temp : rt)
                {
                    if(roles.containsKey(temp.getShopid()))
                        roles.get(temp.getShopid()).add(temp.getR());
                    else
                    {
                        List<Role> ls = new ArrayList<>();
                        ls.add(temp.getR());
                        roles.putIfAbsent(temp.getShopid(),ls);
                    }
                }
            }
            u.setRoleList(roles);
            //appointee lists
            List<Appointments> aLs = em.createQuery("select e from Appointments e where e.myappointee = :status",
                    Appointments.class).setParameter("status", username).getResultList();
            List<OwnerAppointment> oaLs = new ArrayList<>();
            List<ManagerAppointment> maLs = new ArrayList<>();
            for(Appointments a : aLs)
            {
                if(a.getMyrole() == Role.ShopManager)
                {
                    Shop shop = em.find(Shop.class,a.getShopID());
                    User temp = em.find(User.class,a.getUsername());
                    ManagerAppointment ma = new ManagerAppointment(shop,u,temp);
                    maLs.add(ma);
                }
                else if(a.getMyrole() == Role.ShopOwner)
                {
                    Shop shop = em.find(Shop.class,a.getShopID());
                    User temp = em.find(User.class,a.getUsername());
                    OwnerAppointment oa = new OwnerAppointment(shop,u.getUserName(),temp);
                    oaLs.add(oa);
                }
            }
            u.setManagerAppointeeList(maLs);
            u.setOwnerAppointmentList(oaLs);
            et.commit();
        }finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }

        return u;
    }

    public void updateUser(@NotNull User u)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            User ut = em.find(User.class,u.getUserInfo().getUserId());
            ut.merge(u);
            Map<Integer,List<Role>> map = ut.getRoleList();
            for (Map.Entry<Integer,List<Role>> entry : map.entrySet()) {
                for(Role r : entry.getValue()) {
                    roles_table temp = new roles_table(u.getUserName(), entry.getKey(),r);
                    roles_table rt = em.find(roles_table.class,temp);
                    rt.merge(temp);
                }
            }

            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteUser(User u)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            Map<Integer,List<Role>> map = u.getRoleList();
            for (Map.Entry<Integer,List<Role>> entry : map.entrySet()) {
                for(Role r : entry.getValue()) {
                    roles_table temp = new roles_table(u.getUserName(), entry.getKey(),r);
                    em.remove(temp);
                }
            }
            em.remove(u);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveCart(Cart c)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.persist(c);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public Cart getCart(String username)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Cart c;
        try
        {
            et.begin();
            c = em.find(Cart.class, username);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return c;
    }

    public void updateCart(@NotNull Cart c)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            Cart toupdate = em.find(Cart.class, c.getUsername());
            toupdate.merge(c);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteCart(Cart c)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.remove(c);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }
    public void saveShop(Shop shop)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.persist(shop);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public Shop getShop(int shopID)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        Shop shop;
        try
        {
            et.begin();
            shop = em.find(Shop.class,shopID);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return shop;
    }

    public void updateShop(@NotNull Shop shop)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            Shop st = em.find(Shop.class,shop.getShopID());
            st.merge(shop);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteShop(Shop shop)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.remove(shop);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void saveProduct(Product p)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.persist(p);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public ProductImp getProduct(int pID)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        ProductImp p;
        try
        {
            et.begin();
            p = em.find(ProductImp.class,pID);
//            if(p == null)
//                p = em.find(ProductHistory.class,pID);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
        return p;
    }

    public void updateProduct(@NotNull Product pi)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            ProductImp st = em.find(ProductImp.class,pi.getId());
            st.merge(pi);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void deleteProduct(ProductImp pi)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            em.remove(pi);
            et.commit();
        }
        finally
        {
            if(et.isActive())
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
        try
        {
            et.begin();
            for(UserInfo ui : securePasswordStorage.getUserDatabase())
                em.persist(ui);
            et.commit();
        }
        finally
        {
            if(et.isActive())
                et.rollback();
            em.close();
            emf.close();
        }
    }

    public void updateSecurePasswordStorage(SecurePasswordStorage securePasswordStorage) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        EntityTransaction et = em.getTransaction();
        try
        {
            et.begin();
            for(UserInfo ui : securePasswordStorage.getUserDatabase()) {
                UserInfo toUpdate = em.find(UserInfo.class,ui.getUserid());
                toUpdate.merge(ui);
            }
                et.commit();
        }
        finally
        {
            if(et.isActive())
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
        try
        {
            et.begin();
            List<UserInfo> uiLs = em.createQuery("select e from UserInfo e").getResultList();
            Map<String,UserInfo> uiMap = new HashMap<>();
            for(UserInfo ui : uiLs)
                uiMap.putIfAbsent(ui.getUserid(),ui);
            sps.setMap(uiMap);
            et.commit();
        }
        finally
        {
            if(et.isActive())
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
        try
        {
            et.begin();
            for(Order o : orderHistory) {
                o.initLs();
                em.persist(o);
            }
            et.commit();
        }
        finally
        {
            if(et.isActive())
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
            for(Order o : ls)
            {
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
}
