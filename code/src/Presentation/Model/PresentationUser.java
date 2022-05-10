package Presentation.Model;

import domain.user.User;

public class PresentationUser {

    private String username;

    public PresentationUser(String username){
        this.username = username;
    }

    public PresentationUser(User user){
        username = user.getId();
    }

    public String getUsername() {
        return username;
    }
}
