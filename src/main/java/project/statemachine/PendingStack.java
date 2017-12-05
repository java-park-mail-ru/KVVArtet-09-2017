package statemachine;

import websocket.messages.Message;
import states.State;

public interface PendingStack {
    void pushState(State.StateId stateId);

    void popState();

    void clearState();

    @SuppressWarnings("unused")
    void update();

    @SuppressWarnings("unused")
    void handleMessage(Message message);
}
