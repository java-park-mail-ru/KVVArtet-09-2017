package project.websocket.messages;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public abstract class TextMessage extends Message {
    private final String message;

    protected TextMessage(@NotNull String message) {
        this.message = message;
    }

    public @NotNull String getMessage() {
        return message;
    }
}
