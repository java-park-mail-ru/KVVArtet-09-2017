package project.websocket.handlers;

import project.websocket.messages.CreateCharacterRequestMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class CreateCharacterRequestHandler extends MessageHandler<CreateCharacterRequestMessage> {
    public CreateCharacterRequestHandler() {
        super(CreateCharacterRequestMessage.class);
    }

    @Override
    public Message handle(@NotNull CreateCharacterRequestMessage message) {
        //TODO some world class method
        return message;
    }
}
