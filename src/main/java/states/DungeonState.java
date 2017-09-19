package states;

import packets.Packet;
import statemachine.PendingStack;

public class DungeonState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public DungeonState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("DungeonState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("DungeonState handles packet");
        return true;
    }
}
