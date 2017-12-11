package project.websocket.handlers;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import project.websocket.messages.Message;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageHandlerContainer {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(MessageHandlerContainer.class);
    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    public void handle(@NotNull Message message, @NotNull Integer forUser) throws Exception {

        final MessageHandler<?> messageHandler = handlerMap.get(message.getClass());
        if (messageHandler == null) {
            throw new Exception("no handler for message of " + message.getClass().getName() + " type");
        }
        messageHandler.handleMessage(message);
        LOGGER.trace("message handled: type =[" + message.getClass().getName()+ ']');
    }

    public <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }


}