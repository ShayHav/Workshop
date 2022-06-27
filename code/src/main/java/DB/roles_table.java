package DB;

import domain.shop.user.Role;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class roles_table {
    @Id
    private String username;
    @Id
    private int shopid;
    @Enumerated(EnumType.STRING)
    private Role role;

    public roles_table(String username,int shopid, Role role)
    {
        this.username = username;
        this.shopid = shopid;
        this.role = role;
    }

    public roles_table()
    {}

    public roles_table merge(roles_table rt)
    {
        this.setR(rt.getR());
        return this;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public Role getR() {
        return role;
    }

    public void setR(Role r) {
        this.role = r;
    }
}
