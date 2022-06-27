package DB;


import domain.user.Role;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Appointments {

    @Id
    private String username;
    @Id
    private int shopID;
    private String myappointee;

    @Enumerated(EnumType.STRING)
    private Role myrole;

    public Appointments(String username, int shopID, String myappointee, Role r)
    {
        this.myappointee = myappointee;
        this.username = username;
        this.shopID = shopID;
        myrole = r;
    }

    public Appointments(){}
    public Appointments merge(Appointments a)
    {
        setMyappointee(a.getMyappointee());
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getMyappointee() {
        return myappointee;
    }

    public void setMyappointee(String myappointee) {
        this.myappointee = myappointee;
    }

    public Role getMyrole() {
        return myrole;
    }

    public void setMyrole(Role myrole) {
        this.myrole = myrole;
    }
}
