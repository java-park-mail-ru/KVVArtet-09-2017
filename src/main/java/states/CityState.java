package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import statemachine.PendingStack;

public class CityState extends AbstractState {
    protected static final Logger LOGGER = LoggerFactory.getLogger(CityState.class);

    public CityState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("handles packet");
        return true;
    }
}
