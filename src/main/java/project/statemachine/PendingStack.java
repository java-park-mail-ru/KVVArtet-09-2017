package project.statemachine;

import project.websocket.messages.Message;
import project.states.State;

public interface PendingStack {
    void pushState(State.StateId stateId);

    void popState();

    void clearState();

    @SuppressWarnings("unused")
    void update();

    @SuppressWarnings("unused")
    Message handleMessage(Message message);
}
