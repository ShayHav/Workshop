package Presentation.Model.Messages;

public class AppointOwnerMessage {

    public String errorMessage;
    public String type;
    public String addedOwner;

    public AppointOwnerMessage(String errorMessage, String addedManager){
        type = "addOwner";
        this.addedOwner = addedManager;
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public String getAddedOwner() {
        return addedOwner;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddedOwner(String addedOwner) {
        this.addedOwner = addedOwner;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
