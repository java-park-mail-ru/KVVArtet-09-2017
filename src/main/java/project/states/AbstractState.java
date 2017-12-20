package project.states;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.websocket.handlers.MessageHandler;
import project.websocket.messages.Message;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("RedundantSuppression")
@Service
public abstract class AbstractState implements State {

    private final Map<Class<?>, MessageHandler<?>> handlersMap = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
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
