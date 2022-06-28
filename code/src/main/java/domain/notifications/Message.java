package domain.notifications;

import domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
public class Message {

    @Id
    private String sender;
    @Id
    private String reciver;
    @Transient
    private User addressee;
    private String content;
    private boolean read;
    @Id
    private LocalDateTime sentDate;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public void setAddressee(User addressee) {
        this.addressee = addressee;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public Message(User sender, User addressee, String content){
        this.sender = sender.getUserName();
        this.addressee = addressee;
        this.content = content;
        read = false;
        sentDate = LocalDateTime.now();
    }

    public Message merge(Message m)
    {
        setContent(m.getContent());
        setRead(m.isRead());
        setReciver(m.getReciver());
        setSender(m.getSender());
        setSentDate(m.getSentDate());
        return this;
    }

    public Message(){}

    public Message(String sender, User addressee, String content){
        this.sender = sender;
        this.addressee = addressee;
        this.content = content;
        read = false;
        sentDate = LocalDateTime.now();
        this.reciver = addressee.getUserName();
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
