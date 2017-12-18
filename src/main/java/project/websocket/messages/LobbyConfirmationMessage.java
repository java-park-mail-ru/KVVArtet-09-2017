package project.websocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class LobbyConfirmationMessage extends Message {
    private final String message;

    public LobbyConfirmationMessage(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public LobbyConfirmationMessage() {
        this("success");
    }

    public String getMessage() {
        return message;
    }
}
