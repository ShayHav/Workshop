package domain.notifications;

import domain.user.User;

public class Message {

    private final String sender;
    private final User addressee;
    private final String content;
    private boolean read;

    public Message(User sender, User addressee, String content){
        this.sender = sender.getUserName();
        this.addressee = addressee;
        this.content = content;
        read = false;
    }

    public Message(String sender, User addressee, String content){
        this.sender = sender;
        this.addressee = addressee;
        this.content = content;
        read = false;
    }

    public String getContent() {
        return content;
    }

    public User getAddressee() {
        return addressee;
    }

    public String getSender() {
        return sender;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead(){
        read = true;
    }
}
