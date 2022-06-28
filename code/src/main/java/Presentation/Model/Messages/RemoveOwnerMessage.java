package Presentation.Model.Messages;

public class RemoveOwnerMessage {
    public String errorMessage;
    public String type;
    public String removedOwner;

    public RemoveOwnerMessage(String errorMessage, String removedOwner){
        type = "removeOwner";
        this.removedOwner = removedOwner;
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public String getRemovedOwner() {
        return removedOwner;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRemovedOwner(String addedOwner) {
        this.removedOwner = addedOwner;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
