package nz.co.logicons.tlp.core.exception;

public abstract class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String supportCode;

    private final String supportMessage;

    public ApplicationException() {
        this(null);
    }

    public ApplicationException(String message) {
        this(message, null, null);
    }

    public ApplicationException(String message, Throwable cause) {
        this(message, null, null, cause);
    }

    public ApplicationException(String message, String supportCode,
            String supportMessage) {
        super(message);
        this.supportCode = supportCode;
        this.supportMessage = supportMessage;
    }

    public ApplicationException(String message, String supportCode,
            String supportMessage, Throwable cause) {
        super(message, cause);
        this.supportCode = supportCode;
        this.supportMessage = supportMessage;
    }

    public String getSupportCode() {
        return supportCode;
    }

    public String getSupportMessage() {
        return supportMessage;
    }
}
