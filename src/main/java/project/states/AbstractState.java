package project.states;

import project.statemachine.PendingStack;
import project.websocket.messages.Message;

public abstract class AbstractState implements State {

    @Override
    public boolean handleMessage(Message message) {

    }
}
