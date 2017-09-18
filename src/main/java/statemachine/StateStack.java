package statemachine;

import states.AbstractState;
import states.StateFactory;
import packets.Packet;

import java.util.Stack;
import java.util.LinkedList;

import java.lang.*;

public class StateStack implements PendingStack {

    @Override
    public void pushState(AbstractState.StateId stateId) {
        this.pendingChanges.add(new PendingChange(StackActions.SA_PUSH, stateId));
        final String stateName = stateId.toString();
        System.out.println("push request for state " + stateName + " enqueued");
    }

    @Override
    public void popState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_POP));
        System.out.println("pop request enqueued");
    }

    @Override
    public void clearState() {
        this.pendingChanges.add(new PendingChange(StackActions.SA_CLEAR));
        System.out.println("clear request enqueued");
    }

    @Override
    public void update() {
        for (AbstractState state : this.stack) {
            System.out.print("state update call: ");
            if (!state.update()) {
                break;
            }
        }
        this.applyPendingChanges();
    }

    @Override
    public void handlePacket(final Packet packet) {
        System.out.println("state handlePacket call: ");
        for (AbstractState state : this.stack) {
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

    private void implementPush(AbstractState.StateId stateId) {
        this.stack.push(StateFactory.createState(stateId, this));
        System.out.println("push request for state " + stateId.toString() + " done");
    }

    private void implementPop() {
        this.stack.pop();
        System.out.println("pop request done");
    }

    private void implementClear() {
        this.stack.clear();
        System.out.println("clear request done");
    }

    private enum StackActions { SA_PUSH, SA_POP, SA_CLEAR }

    private static class PendingChange {
        PendingChange(StackActions action, AbstractState.StateId stateId) {
          this.action = action;
          this.state = stateId;
        }

        PendingChange(StackActions action) {
            this(action, AbstractState.StateId.SI_NONE);
        }

        StackActions getAction() {
            return this.action;
        }

        AbstractState.StateId getState() {
            return this.state;
        }

        private final StackActions action;
        private final AbstractState.StateId state;
    }

    final LinkedList<PendingChange> pendingChanges = new LinkedList<>();
    final Stack<AbstractState> stack = new Stack<>();
}
