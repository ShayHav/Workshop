package Presentation.Model.Messages;

import domain.notifications.Message;

public class NotificationMessage {

    private String sender;
    private String addressee;
    private String content;
    private boolean read;

    public NotificationMessage(){}

    public NotificationMessage(Message domainMessage){
        this.sender = domainMessage.getSender();
        this.addressee = domainMessage.getAddressee().getUserName();
        this.content = domainMessage.getContent();
        this.read = domainMessage.isRead();
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
}
