package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public abstract class State implements AbstractState {
  public State(PendingStack stack) {
      this.stack = stack;
      System.out.print("State created: ");
  }

  @Override
  public void finalize() {
      System.out.println("State destroyed");
  }

  @Override
  public void requestStackPush(AbstractState.ID stateId) {
      this.stack.pushState(stateId);
      System.out.println("push request sent with state = " + stateId.toString());
  }

  @Override
  public void requestStackPop() {
      this.stack.popState();
      System.out.println("pop request sent");
  }

  @Override
  public void requestStackClear() {
      this.stack.clearState();
      System.out.println("clear request sent");
  }

  @Override
  public boolean update() {
      System.out.println("State updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      System.out.println("Package handled");
      return true;
  }

  private final PendingStack stack;
}
