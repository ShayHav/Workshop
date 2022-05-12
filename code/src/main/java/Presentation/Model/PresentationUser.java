package Presentation.Model;

import domain.user.User;

public class PresentationUser {

    private String username;
    private String password;

    public PresentationUser(String username){
        this.username = username;
    }

    public PresentationUser(User user){
        username = user.getId();
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
}
