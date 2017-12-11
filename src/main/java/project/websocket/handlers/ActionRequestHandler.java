package project.websocket.handlers;

import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public class ActionRequestHandler extends MessageHandler<ActionRequestMessage> {
    public ActionRequestHandler() {
        super(ActionRequestMessage.class);
    }

    @Override
    public Message handle(@NotNull ActionRequestMessage message) {
        //some world class method
        return message;
    }
}