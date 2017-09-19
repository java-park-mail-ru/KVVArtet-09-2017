package states;

import packets.Packet;
import statemachine.PendingStack;

public class TitleState extends AbstractState {
  public TitleState(PendingStack stack) {
      super(stack);
  }

  @Override
  public boolean update() {
      LOGGER.info("TitleState updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      LOGGER.info("TitleState handles packet");
      return true;
  }
}
