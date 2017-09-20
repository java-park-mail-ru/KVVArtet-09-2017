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

    private enum StackActions { SA_PUSH, SA_POP, SA_CLEAR }

    private static class PendingChange {
        private final StackActions action;
        private final State.StateId state;

        PendingChange(StackActions action, State.StateId stateId) {
            this.action = action;
            this.state = stateId;
        }

        PendingChange(StackActions action) {
            this(action, State.StateId.SI_NONE);
        }

        StackActions getAction() {
            return action;
        }

        State.StateId getState() {
            return state;
        }
    }

    @Override
    public void pushState(State.StateId stateId) {
        pendingChanges.add(new PendingChange(StackActions.SA_PUSH, stateId));
        LOGGER.info("push request for state " + stateId.toString() + " enqueued");
    }

    @Override
    public void popState() {
        pendingChanges.add(new PendingChange(StackActions.SA_POP));
        LOGGER.info("pop request enqueued");
    }

    @Override
    public void clearState() {
        pendingChanges.add(new PendingChange(StackActions.SA_CLEAR));
        LOGGER.info("clear request enqueued");
    }

    @Override
    public void update() {
        if (!stack.isEmpty()) {
            LOGGER.info("active state update call: ");
            stack.peek().update();
        }
        applyPendingChanges();
    }

    @Override
    public void handlePacket(final Packet packet) {
        LOGGER.info("state handlePacket call: ");
        for (State state : stack) {
            if (!state.handlePacket(packet)) {
                break;
            }
        }
        applyPendingChanges();
    }

    public boolean isStackEmpty() {
        return stack.isEmpty();
    }

    public int stackSize() {
        return stack.size();
    }

    public boolean hasPendingChanges() {
        return !pendingChanges.isEmpty();
    }

    public int pendingChangesCount() {
        return pendingChanges.size();
    }

    private void applyPendingChanges() {
        while (!pendingChanges.isEmpty()) {
            final PendingChange change = pendingChanges.poll();
            implementChange(change);
        }
    }

    private void implementChange(PendingChange change) {
        switch (change.getAction()) {
            case SA_PUSH:
                implementPush(change.getState());
                break;
            case SA_POP:
                implementPop();
                break;
            case SA_CLEAR:
                implementClear();
                break;
            default:
                break;
        }
    }

    private void implementPush(State.StateId stateId) {
        final State state = StateFactory.createState(stateId, this);
        if (state != null) {
            stack.push(state);
        }
        LOGGER.info("push request for state " + stateId.toString() + " done");
    }

    private void implementPop() {
        stack.poll();
        LOGGER.info("pop request done");
    }

    private void implementClear() {
        stack.clear();
        LOGGER.info("clear request done");
    }
}
