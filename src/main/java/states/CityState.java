package states;

import packets.Packet;
import statemachine.PendingStack;

public class CityState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CityState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("CityState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("CityState handles packet");
        return true;
    }
}
