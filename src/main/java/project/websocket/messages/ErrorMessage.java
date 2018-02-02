package project.websocket.messages;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ErrorMessage extends Message {

    private final String message;

    public ErrorMessage(@NotNull String message) {
        this.message = message;
    }

    public ErrorMessage() {
        this.message = "some error occured";
    }

    public @NotNull String getMessage() {
        return message;
    }
}
