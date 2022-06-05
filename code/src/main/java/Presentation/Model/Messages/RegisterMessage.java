package Presentation.Model.Messages;

public class RegisterMessage {
    private String guestUsername;
    private String username;
    private String password;

    public RegisterMessage(){}

    public String getUsername() {
        return username;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
