package project.websocket.messages.typecontainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class BooleanMessage extends TypeContainerMessage<Boolean> {

    public BooleanMessage(@JsonProperty("value")
                          @NotNull Boolean value) {
        super(value);
    }

    public static @NotNull Message createPositiveResponse() {
        return new BooleanMessage(true);
    }

    public static @NotNull Message createNegativeResponse() {
        return new BooleanMessage(false);
    }
}
