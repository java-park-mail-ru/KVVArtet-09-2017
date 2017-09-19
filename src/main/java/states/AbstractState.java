package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import statemachine.PendingStack;

public abstract class AbstractState implements State {
  public AbstractState(PendingStack stack) {
      this.stack = stack;
  }

  @Override
  public void requestStackPush(State.StateId stateId) {
      this.stack.pushState(stateId);
      LOGGER.info("push request sent with state = " + stateId.toString());
  }

  @Override
  public void requestStackPop() {
      this.stack.popState();
      LOGGER.info("pop request sent");
  }

  @Override
  public void requestStackClear() {
      this.stack.clearState();
      LOGGER.info("clear request sent");
  }

  @Override
  public boolean update() {
      LOGGER.info("State updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      LOGGER.info("Packet handled");
      return true;
  }

  private final PendingStack stack;
  protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractState.class);
}
