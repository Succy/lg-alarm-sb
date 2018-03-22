package cn.succy.alarm.exception;

public class EmailRuntimeException extends RuntimeException {
    public EmailRuntimeException() {
        super();
    }

    public EmailRuntimeException(String message) {
        super(message);
    }

    public EmailRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailRuntimeException(Throwable cause) {
        super(cause);
    }

    protected EmailRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
