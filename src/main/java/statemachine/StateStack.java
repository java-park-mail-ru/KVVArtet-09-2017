package statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import states.State;
import states.StateFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

public class StateStack implements PendingStack {
    private final Deque<PendingChange> pendingChanges = new LinkedList<>();
    private final Deque<State> stack = new ArrayDeque<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(StateStack.class);

    @Override
    public void pushState(State.StateId stateId) {
        this.pendingChanges.add(new PendingChange(StackActions.SA_PUSH, stateId));
        final String stateName = stateId.toString();
        LOGGER.info("push request for state " + stateName + " enqueued");
    }

    @Override
    public void popState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_POP));
        LOGGER.info("pop request enqueued");
    }

    @Override
    public void clearState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_CLEAR));
        LOGGER.info("clear request enqueued");
    }

    @Override
    public void update() {
        for (State state : this.stack) {
            LOGGER.info("state update call: ");
            if (!state.update()) {
                break;
            }
        }
        this.applyPendingChanges();
    }

    @Override
    public void handlePacket(final Packet packet) {
        LOGGER.info("state handlePacket call: ");
        for (State state : this.stack) {
            if (!state.handlePacket(packet)) {
                break;
            }
        }
        this.applyPendingChanges();
    }

    private void applyPendingChanges() {
        for (PendingChange change : this.pendingChanges) {
            this.implementChange(change);
        }
        this.pendingChanges.clear();
    }

    private void implementChange(PendingChange change) {
        switch (change.getAction()) {
            case SA_PUSH:
                this.implementPush(change.getState());
                break;
            case SA_POP:
                this.implementPop();
                break;
            case SA_CLEAR:
                this.implementClear();
                break;
            default:
                break;
        }
    }

    private void implementPush(State.StateId stateId) {
        this.stack.push(StateFactory.createState(stateId, this));
        LOGGER.info("push request for state " + stateId.toString() + " done");
    }

    private void implementPop() {
        this.stack.pop();
        LOGGER.info("pop request done");
    }

    private void implementClear() {
        this.stack.clear();
        LOGGER.info("clear request done");
    }

    private enum StackActions { SA_PUSH, SA_POP, SA_CLEAR }

    private static class PendingChange {
        PendingChange(StackActions action, State.StateId stateId) {
          this.action = action;
          this.state = stateId;
        }

        PendingChange(StackActions action) {
            this(action, State.StateId.SI_NONE);
        }

        StackActions getAction() {
            return this.action;
        }

        State.StateId getState() {
            return this.state;
        }

        private final StackActions action;
        private final State.StateId state;
    }
}
