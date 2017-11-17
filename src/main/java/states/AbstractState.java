package states;

import statemachine.PendingStack;

public abstract class AbstractState implements State {
    private final PendingStack stack;

    AbstractState(PendingStack stack) {
      this.stack = stack;
  }

    @Override
    public void requestStackPush(State.StateId stateId) {
        stack.pushState(stateId);
    }

    @Override
    public void requestStackPop() {
        stack.popState();
    }

    @Override
    public void requestStackClear() {
        stack.clearState();
    }
}
