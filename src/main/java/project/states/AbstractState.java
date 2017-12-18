package project.states;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.websocket.handlers.MessageHandler;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import java.util.HashMap;
import java.util.Map;
@Service
public abstract class AbstractState implements State {

    Map<Class<?>, MessageHandler<?>> handlersMap = new HashMap<>();

    @Override
    public Message handleMessage(Message message, Integer forUser)  {
        final MessageHandler<?> messageHandler = handlersMap.get(message.getClass());
        if (messageHandler == null) {
            return null;
        }
        return messageHandler.handleMessage(message, forUser);
    }

    @Override
    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlersMap.put(clazz, handler);
    }
}
