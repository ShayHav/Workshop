package Presentation.Model;

import domain.user.User;

public class PresentationUser {

    private String username;
    private String password;
    private boolean loggedIn;

    public PresentationUser(String username, boolean loggedIn){
        this.username = username;
        this.loggedIn = loggedIn;
    }

    public PresentationUser(User user){
        username = user.getUserName();
        loggedIn = user.isLoggedIn();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean hasInventoryPermission(){
        throw new UnsupportedOperationException("to be done");
    }
}
