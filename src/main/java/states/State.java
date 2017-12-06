package states;

import websocket.messages.Message;

@SuppressWarnings({"UnusedReturnValue", "SameReturnValue", "unused"})
public interface State {
    enum StateId {SI_NONE, SI_TITLE, @SuppressWarnings("SpellCheckingInspection") SI_SIGNUP, SI_CHARACTER_LIST, SI_CITY, SI_DUNGEON, SI_CHARACTER_CREATION}

    void requestStackPush(StateId stateId);

    void requestStackPop();

    void requestStackClear();

    boolean update();

    boolean handleMessage(Message message);

}
