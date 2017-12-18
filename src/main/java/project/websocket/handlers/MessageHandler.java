package project.websocket.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.states.LobbyState;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public abstract class MessageHandler<T extends Message> {
    private Class<T> clazz;

    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    public Message handleMessage(@NotNull Message message, Integer forUser) {
        try {
            return handle(clazz.cast(message), forUser);
        } catch (ClassCastException e) {
            e.printStackTrace();
            LOGGER.error("Message is not convertible");
            return new ErrorMessage("Message is not convertible");
        }
    }

    public abstract Message handle(@NotNull T message, Integer forUser);
}