package Presentation.Model.Messages;

public class AppointMangerMessage {

    public String errorMessage;
    public String type;
    public String addedManager;

    public AppointMangerMessage(String errorMessage, String addedManager){
        type = "addManager";
        this.addedManager = addedManager;
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public String getAddedManager() {
        return addedManager;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAddedManager(String addedManager) {
        this.addedManager = addedManager;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
