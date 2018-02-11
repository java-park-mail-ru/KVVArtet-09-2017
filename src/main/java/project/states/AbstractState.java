package project.states;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import project.websocket.handlers.MessageHandler;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("RedundantSuppression")
@Service
public abstract class AbstractState implements State {

    private final Map<Class<?>, MessageHandler<?>> handlersMap = new HashMap<>();

    @SuppressWarnings("ConstantConditions")
    @Override
    public @NotNull Message handleMessage(@NotNull Message message, @NotNull Integer forUser)  {
        final MessageHandler<?> messageHandler = handlersMap.get(message.getClass());
        return messageHandler != null ? messageHandler.handleMessage(message, forUser)
                : new ErrorMessage("unrecognized request");
    }

    @Override
    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, @NotNull MessageHandler<T> handler) {
        handlersMap.put(clazz, handler);
    }
}
