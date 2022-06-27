package domain.shop.user;

import javax.persistence.Entity;
import javax.persistence.Id;

// Each user has a unique salt
// This salt must be recomputed during password change!
@Entity
public class UserInfo {
    String userEncryptedPassword;
    String userSalt;
    @Id
    String userid;
}
