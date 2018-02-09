package project.websocket.messages.battle;

import org.jetbrains.annotations.NotNull;
import project.websocket.messages.Message;

@SuppressWarnings("unused")
public class ActionConfirmationMessage extends Message {
    private final String message;

    public ActionConfirmationMessage(@NotNull String message) {
        this.message = message;
    }

    public ActionConfirmationMessage() {
        this.message = "success";
    }

    public @NotNull String getMessage() {
        return message;
    }

}
