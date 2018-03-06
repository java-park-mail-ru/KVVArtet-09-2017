package project.websocket.messages.bool;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class BooleanMessage extends Message {
    private final Boolean flag;

    public BooleanMessage(@JsonProperty("flag")
                          @NotNull Boolean flag) {
        this.flag = flag;
    }

    public @NotNull Boolean getFlag() {
        return flag;
    }

    public static @NotNull Message createPositiveResponse() {
        return new BooleanMessage(true);
    }

    public static @NotNull Message createNegativeResponse() {
        return new BooleanMessage(false);
    }
}
