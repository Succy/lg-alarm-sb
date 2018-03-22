package cn.succy.alarm.exception;

public class WeChatRuntimeException extends RuntimeException {
    public WeChatRuntimeException() {
        super();
    }

    public WeChatRuntimeException(String message) {
        super(message);
    }

    public WeChatRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeChatRuntimeException(Throwable cause) {
        super(cause);
    }

    protected WeChatRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
