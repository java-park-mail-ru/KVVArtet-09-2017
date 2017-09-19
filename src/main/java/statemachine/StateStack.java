package statemachine;

import states.State;
import states.StateFactory;
import packets.Packet;

import java.util.Stack;
import java.util.LinkedList;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class StateStack implements PendingStack {

    @Override
    public void pushState(State.StateId stateId) {
        this.pendingChanges.add(new PendingChange(StackActions.SA_PUSH, stateId));
        final String stateName = stateId.toString();
        logger.info("push request for state " + stateName + " enqueued");
    }

    @Override
    public void popState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_POP));
        logger.info("pop request enqueued");
    }

    @Override
    public void clearState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_CLEAR));
        logger.info("clear request enqueued");
    }

    @Override
    public void update() {
        for (State state : this.stack) {
            logger.info("state update call: ");
            if (!state.update()) {
                break;
            }
        }
        this.applyPendingChanges();
    }

    @Override
    public void handlePacket(final Packet packet) {
        logger.info("state handlePacket call: ");
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
        }
    }

    private void implementPush(State.StateId stateId) {
        this.stack.push(StateFactory.createState(stateId, this));
        logger.info("push request for state " + stateId.toString() + " done");
    }

    private void implementPop() {
        this.stack.pop();
        logger.info("pop request done");
    }

    private void implementClear() {
        this.stack.clear();
        logger.info("clear request done");
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

    final LinkedList<PendingChange> pendingChanges = new LinkedList<>();
    final Stack<State> stack = new Stack<>();
    private static final Logger logger = LoggerFactory.getLogger(StateStack.class);
}
