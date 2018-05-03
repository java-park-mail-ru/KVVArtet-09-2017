package project.websocket.messages.typecontainer;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class TypeContainerMessage<T> extends Message {
    private final T value;

    @SuppressWarnings("WeakerAccess")
    public TypeContainerMessage(@JsonProperty ("value")
                                @NotNull T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
