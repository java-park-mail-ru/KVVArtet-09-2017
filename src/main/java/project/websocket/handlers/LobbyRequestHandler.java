package project.websocket.handlers;

import project.websocket.messages.LobbyRequestMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class LobbyRequestHandler extends MessageHandler<LobbyRequestMessage> {
    public LobbyRequestHandler() {
        super(LobbyRequestMessage.class);
    }

    @Override
    public Message handle(@NotNull LobbyRequestMessage message) {
        //some world class method
        return message;
    }
}