package Presentation.Model.Messages;

public class RemoveManagerMessage {
    public String errorMessage;
    public String type;
    public String removedManager;

    public RemoveManagerMessage(String errorMessage, String removeManager){
        type = "removeManager";
        this.removedManager = removeManager;
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public String getRemovedManager() {
        return removedManager;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRemovedManager(String addedOwner) {
        this.removedManager = addedOwner;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
