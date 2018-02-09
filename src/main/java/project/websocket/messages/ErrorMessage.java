package project.websocket.messages;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ErrorMessage extends TextMessage {

    public ErrorMessage(@NotNull String message) {
        super(message);
    }

    public ErrorMessage() {
        super("some error occured");
    }
}
