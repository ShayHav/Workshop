package domain.Responses;

public class Response {

    public String errorMessage;

    public Response() {
        errorMessage = null;
    }

    public Response(String msg) {
        this.errorMessage = msg;
    }

    public boolean isErrorOccurred() {
        return errorMessage != null;
    }
}

