package Testing_System;

public class UserGenerator {

    /*
    username rules -
        1. Length: 4-16
        2. No spaces
        3. a-z, A-Z, 0-9

    passwords rules -
        1. Length: 4 - 16
        2. No spaces
     */

    private final String[][] userNames = { {"arielr77","nitay658","chikolmoral", "Shayhav", "shaharlen"}, {"badus#r1","as","ArielOmryNitayShayShahar","ok ok ok","ainalhbo1bo1~"}, {")))))DROP TABLES", "3434@@@@$^$", "()()()()()()((((())))","CREATE TABLE 3123","NO GOOD MOF#CKER"}};
    private final String[] pw = {"okokokok", "blah12blah", "!(sdor", "ylkmce812c", "9dloroororo30"};
    private final String[] notRegisteredUser = {"omroshon", "Nitka", "Arielush", "Barrr1","Mira20"};
    private final int validUsersIndex =0;
    private final int badUsersIndex = 1;
    private final int sadUsersIndex = 2;
    private final int numOfUsers = 5;
    private final int numOfPw = 5;

    public UserGenerator () {}

    public int getNumOfUser() { return numOfUsers;}
    public int getNumOfPw(){ return numOfPw; }
    public String[] GetValidUsers(){ return userNames[validUsersIndex];}
    public String[] GetBadUsers() {return userNames[badUsersIndex];}
    public String[] GetSadUsers() { return userNames[sadUsersIndex];}
    public String[] GetPW() { return pw;}
    public String[] GetNotRegisteredUsers() { return notRegisteredUser;}

}
