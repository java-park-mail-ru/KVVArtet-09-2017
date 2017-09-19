package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class TitleState extends AbstractState {
  public TitleState(PendingStack stack) {
      super(stack);
  }

  @Override
  public boolean update() {
      logger.info("TitleState updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      logger.info("TitleState handles packet");
      return true;
  }
}
