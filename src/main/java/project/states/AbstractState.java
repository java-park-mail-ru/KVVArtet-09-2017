package project.states;

import project.websocket.handlers.MessageHandler;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractState implements State {

    Map<Class<?>, MessageHandler<?>> handlersMap = new HashMap<>();

    @Override
    public Message handleMessage(Message message)  {
        final MessageHandler<?> messageHandler = handlersMap.get(message.getClass());
        if (messageHandler == null) {
            return new ErrorMessage("no handler for message of " + message.getClass().getName() + " type");
        }
        return messageHandler.handleMessage(message);
    }
}
