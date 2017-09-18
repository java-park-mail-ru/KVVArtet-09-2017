package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class DungeonState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public DungeonState(PendingStack stack) {
        super(stack);
        System.out.println("DungeonState created");
    }

    @Override
    public boolean update() {
        System.out.println("DungeonState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        System.out.println("DungeonState handles packet");
        return true;
    }
}
