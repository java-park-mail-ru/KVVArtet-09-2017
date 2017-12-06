package statemachine;

import states.State;
import websocket.messages.Message;

public interface PendingStack {
    void pushState(State.StateId stateId);

    void popState();

    void clearState();

    @SuppressWarnings("unused")
    void update();

    @SuppressWarnings("unused")
    void handleMessage(Message message);
}
