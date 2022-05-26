package domain.notifications;

import domain.user.User;

public class Message {

    private final User sender;
    private final User addressee;
    private final String content;
    private boolean readed;

    public Message(User sender, User addressee, String content){
        this.sender = sender;
        this.addressee = addressee;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public User getAddressee() {
        return addressee;
    }

    public User getSender() {
        return sender;
    }
}
