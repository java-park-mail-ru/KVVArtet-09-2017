package states;

import statemachine.PendingStack;

public abstract class AbstractState implements State {
    private final PendingStack stack;

    public AbstractState(PendingStack stack) {
      this.stack = stack;
  }

    @Override
    public void requestStackPush(State.StateId stateId) {
        this.stack.pushState(stateId);
    }

    @Override
    public void requestStackPop() {
        this.stack.popState();
    }

    @Override
    public void requestStackClear() {
        this.stack.clearState();
    }
}
