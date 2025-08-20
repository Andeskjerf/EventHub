package sh.lmao.event_hub.exceptions;

public class AlreadyExistsException extends Exception {
    public AlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
