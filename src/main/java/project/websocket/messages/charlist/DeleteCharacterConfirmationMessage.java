package project.websocket.messages.charlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class DeleteCharacterConfirmationMessage extends Message {
    private final String message;

    @SuppressWarnings("WeakerAccess")
    public DeleteCharacterConfirmationMessage(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public DeleteCharacterConfirmationMessage() {
        this("success");
    }

    @SuppressWarnings("unused")
    public @NotNull String getMessage() {
        return message;
    }
}
