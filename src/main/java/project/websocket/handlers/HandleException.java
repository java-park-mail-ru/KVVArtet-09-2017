package project.websocket.handlers;

class HandleException extends Exception {
    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleException(String message) {
        super(message);
    }
}
