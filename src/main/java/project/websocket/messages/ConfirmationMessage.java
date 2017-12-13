package project.websocket.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ConfirmationMessage extends Message {
    private final String message;

    public ConfirmationMessage(@JsonProperty("message") @NotNull String message) {
        this.message = message;
    }

    public ConfirmationMessage() {
        this("success");
    }

    public String getMessage() {
        return message;
    }
}
