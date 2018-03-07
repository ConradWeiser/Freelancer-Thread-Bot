package sql.exceptions;

public class DataNotExistsException extends Exception {

    String error = "Generic data does not exist";

    public DataNotExistsException(String userMessage) {

        this.error = userMessage;
    }

    @Override public String getMessage() {

        return error;
    }
}
