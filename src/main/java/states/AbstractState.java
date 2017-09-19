package states;

import statemachine.PendingStack;
import packets.Packet;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public abstract class AbstractState implements State {
  public AbstractState(PendingStack stack) {
      this.stack = stack;
  }

  @Override
  public void requestStackPush(State.StateId stateId) {
      this.stack.pushState(stateId);
      logger.info("push request sent with state = " + stateId.toString());
  }

  @Override
  public void requestStackPop() {
      this.stack.popState();
      logger.info("pop request sent");
  }

  @Override
  public void requestStackClear() {
      this.stack.clearState();
      logger.info("clear request sent");
  }

  @Override
  public boolean update() {
      logger.info("State updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      logger.info("Packet handled");
      return true;
  }

  private final PendingStack stack;
  protected static final Logger logger = LoggerFactory.getLogger(AbstractState.class);
}
