package domain.user;

// Each user has a unique salt
// This salt must be recomputed during password change!
public class UserInfo {
    String userEncryptedPassword;
    String userSalt;
    String userid;
}
