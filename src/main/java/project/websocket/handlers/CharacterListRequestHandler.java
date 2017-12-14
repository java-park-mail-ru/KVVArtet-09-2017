package project.websocket.handlers;

import project.websocket.messages.CharacterListRequestMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class CharacterListRequestHandler extends MessageHandler<CharacterListRequestMessage> {

    public CharacterListRequestHandler() {
        super(CharacterListRequestMessage.class);
    }

    @Override
    public Message handle(@NotNull CharacterListRequestMessage message) {
        //TODO some world class method
        return message;
    }
}
