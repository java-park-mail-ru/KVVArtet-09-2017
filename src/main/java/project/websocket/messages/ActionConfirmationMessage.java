package project.websocket.messages;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ActionConfirmationMessage extends Message {
    private final String message;

    public ActionConfirmationMessage(@NotNull String message) {
        this.message = message;
    }

    public ActionConfirmationMessage() {
        this.message = "success";
    }

    public String getMessage() {
        return message;
    }

}
