package domain.notifications;

import domain.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {

    private String sender;
    private User addressee;
    private String content;
    private boolean read;
    private LocalDateTime sentDate;

    public Message(User sender, User addressee, String content){
        this.sender = sender.getUserName();
        this.addressee = addressee;
        this.content = content;
        read = false;
        sentDate = LocalDateTime.now();
    }

    public Message(){}

    public Message(String sender, User addressee, String content){
        this.sender = sender;
        this.addressee = addressee;
        this.content = content;
        read = false;
        sentDate = LocalDateTime.now();
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

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return sender.equals(message.sender) && addressee.equals(message.addressee) && content.equals(message.content) && sentDate.equals(message.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, addressee, content, sentDate);
    }

    public void copy(Message from){
        this.sender = from.getSender();
        this.addressee = from.getAddressee();
        this.content = from.getContent();
        this.sentDate = from.getSentDate();
        this.read = from.isRead();
    }
}
