package project.websocket.messages.matchmaking;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

@SuppressWarnings("unused")
public class LobbyConfirmationMessage extends Message {
    private final String message;

    public LobbyConfirmationMessage(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public LobbyConfirmationMessage() {
        this("success");
    }

    public @NotNull String getMessage() {
        return message;
    }
}
