package domain.user;

import domain.ErrorLoggerSingleton;


import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;



//TODO: https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
public class SecurePasswordStorage {
    private static SecurePasswordStorage securePasswordStorage_singleton = null;
    // Simulates database of users!
    private final Map<String, UserInfo> userDatabase = new HashMap<>();
    private static final ErrorLoggerSingleton errorLogger = ErrorLoggerSingleton.getInstance();

    SecurePasswordStorage(){}

    public static SecurePasswordStorage getSecurePasswordStorage_singleton(){
        if(securePasswordStorage_singleton==null)
            securePasswordStorage_singleton = new SecurePasswordStorage();
        return securePasswordStorage_singleton;
    }

    public boolean passwordCheck(String inputUser, String inputPass){
        try{
            return authenticateUser(inputUser,inputPass);
        }catch (Exception e) {
            errorLogger.logMsg(Level.WARNING,String.format("passwordCheck of %s failed.",inputUser));
            return false;
        }
    }

    private boolean authenticateUser(String inputUser, String inputPass) throws Exception {
        UserInfo user;
        synchronized (userDatabase) {
            user = userDatabase.get(inputUser);
        }
        if (user == null) {
            return false;
        } else {
            String salt = user.userSalt;
            String calculatedHash = getEncryptedPassword(inputPass, salt);
            return calculatedHash.equals(user.userEncryptedPassword);
        }
    }
    public void inRole(String userid, String password){
        try{ signUp(userid,password); }
        catch (Exception e){ errorLogger.logMsg(Level.WARNING,String.format("Cryptographic Hash password of %d failed.: "+e.getMessage(),userid)); }
    }

    private synchronized void signUp(String userid, String password) throws Exception {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(password, salt);
        UserInfo user = new UserInfo();
        user.userEncryptedPassword = encryptedPassword;
        user.userid = userid;
        user.userSalt = salt;
        saveUser(user);
    }

    // Get a encrypted password using PBKDF2 hash algorithm
    public String getEncryptedPassword(String password, String salt) throws Exception {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 20000; // NIST specifies 10000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        byte[] encBytes = f.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encBytes);
    }

    // Returns base64 encoded salt
    public String getNewSalt() throws Exception {
        // Don't use Random!
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // NIST recommends minimum 4 bytes. We use 8.
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private synchronized void saveUser(UserInfo user) {
        userDatabase.put(user.userid, user);
    }

    public boolean isUserRole(String userName) {
        return userDatabase.containsKey(userName);
    }
}

