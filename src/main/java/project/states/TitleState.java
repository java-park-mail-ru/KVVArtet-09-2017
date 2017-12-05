package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.statemachine.PendingStack;
import project.websocket.messages.Message;

public class TitleState extends AbstractState {
  private static final Logger LOGGER = LoggerFactory.getLogger(TitleState.class);

  TitleState(PendingStack stack) {
      super(stack);
  }

  @Override
  public boolean update() {
      LOGGER.info("updated");
      return true;
  }

  @Override
  public boolean handleMessage(final Message message) {
      LOGGER.info("handles packet");
      return true;
  }
}
