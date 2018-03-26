package project.states;

import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import project.websocket.handlers.MessageHandler;
import project.websocket.messages.Message;

@SuppressWarnings({"UnusedReturnValue", "SameReturnValue", "unused"})
public interface State {
    <T extends Message> void registerHandler(@NotNull Class<T> clazz, @NotNull MessageHandler<T> handler);

    enum StateId { SI_NONE, SI_TITLE, SI_SIGNUP,
        SI_CHARACTER_LIST, SI_CITY, SI_DUNGEON, SI_CHARACTER_CREATION }

    @Nullable
    Message handleMessage(@NotNull Message message, @NotNull Integer forUser);

}
