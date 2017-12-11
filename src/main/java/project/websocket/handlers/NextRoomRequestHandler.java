package project.websocket.handlers;

import project.websocket.messages.Message;
import project.websocket.messages.NextRoomRequestMessage;

import javax.validation.constraints.NotNull;

public class NextRoomRequestHandler extends MessageHandler<NextRoomRequestMessage> {
    public NextRoomRequestHandler() {
        super(NextRoomRequestMessage.class);
    }

    @Override
    public Message handle(@NotNull NextRoomRequestMessage message) {
        //some world class method
        return message;
    }
}