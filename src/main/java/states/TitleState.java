package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class TitleState extends AbstractState {
  public TitleState(PendingStack stack) {
      super(stack);
      System.out.println("TitleState created");
  }

  @Override
  public boolean update() {
      System.out.println("TitleState updated");
      return true;
  }

  @Override
  public boolean handlePacket(final Packet packet) {
      System.out.println("TitleState handles packet");
      return true;
  }
}
