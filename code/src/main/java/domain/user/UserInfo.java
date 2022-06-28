package domain.user;

import javax.persistence.Entity;
import javax.persistence.Id;

// Each user has a unique salt
// This salt must be recomputed during password change!
@Entity
public class UserInfo {
    @Id
    public String userid;
    public String userEncryptedPassword;
    public String userSalt;

    public UserInfo(){}

    public UserInfo(String userid, String enc, String salt)
    {
        this.userid = userid;
        userEncryptedPassword = enc;
        userSalt = salt;
    }

    public UserInfo merge(UserInfo ui){
        setUserEncryptedPassword(ui.getUserEncryptedPassword());
        setUserSalt(ui.getUserSalt());
        setUserid(ui.getUserid());
        return this;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUserEncryptedPassword(String userEncryptedPassword) {
        this.userEncryptedPassword = userEncryptedPassword;
    }

    public void setUserSalt(String userSalt) {
        this.userSalt = userSalt;
    }

    public String getUserid() {
        return userid;
    }

    public String getUserEncryptedPassword() {
        return userEncryptedPassword;
    }

    public String getUserSalt() {
        return userSalt;
    }
}
