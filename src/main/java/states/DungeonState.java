package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import statemachine.PendingStack;

public class DungeonState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonState.class);

    DungeonState(PendingStack stack) {
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
