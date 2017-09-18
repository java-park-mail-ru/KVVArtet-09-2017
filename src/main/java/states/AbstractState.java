package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public abstract class AbstractState implements State {
  public AbstractState(PendingStack stack) {
      this.stack = stack;
      System.out.print("AbstractState created: ");
  }

  @Override
  public void finalize() {
      System.out.println("AbstractState destroyed");
  }

  @Override
  public void requestStackPush(State.StateId stateId) {
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
      System.out.println("AbstractState updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      System.out.println("Package handled");
      return true;
  }

  private final PendingStack stack;
}
