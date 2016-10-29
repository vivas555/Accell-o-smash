package ca.csf.tp3;

public class CriticalRepositoryException extends Exception {

    public CriticalRepositoryException(String message) {
        super(message);
    }

    public CriticalRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
