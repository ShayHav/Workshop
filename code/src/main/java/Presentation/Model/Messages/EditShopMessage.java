package Presentation.Model.Messages;

public class EditShopMessage {

    public String type;
    public String requestingUser;
    public String subject;

    public EditShopMessage(){}

    public String getType() {
        return type;
    }

    public String getRequestingUser() {
        return requestingUser;
    }

    public String getSubject() {
        return subject;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRequestingUser(String requestingUser) {
        this.requestingUser = requestingUser;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
