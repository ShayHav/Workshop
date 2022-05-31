package Presentation.Model.Messages;

import domain.notifications.Message;

import java.time.LocalDateTime;
import java.util.Objects;

public class NotificationMessage {

    private String sender;
    private String addressee;
    private String content;
    private boolean read;
    private LocalDateTime sentDate;

    public NotificationMessage(){}

    public NotificationMessage(Message domainMessage){
        this.sender = domainMessage.getSender();
        this.addressee = domainMessage.getAddressee().getUserName();
        this.content = domainMessage.getContent();
        this.read = domainMessage.isRead();
        sentDate = domainMessage.getSentDate();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean readMessage) {
        this.read = readMessage;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationMessage message = (NotificationMessage) o;
        return Objects.equals(sender, message.sender) && Objects.equals(addressee, message.addressee) && Objects.equals(content, message.content) && Objects.equals(sentDate, message.sentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, addressee, content, sentDate);
    }
}
