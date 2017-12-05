package project.websocket.handlers;

import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;

public abstract class MessageHandler<T> {
    private Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    public void handleMessage(@NotNull Message message, @NotNull Integer userId) throws Exception {
        try {
            handle(clazz.cast(message), userId);
        } catch (ClassCastException e) {
            e.printStackTrace();
            throw new Exception("Message is not convertible");
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Integer userId) throws Exception;
}