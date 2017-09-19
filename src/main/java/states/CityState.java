package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CityState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CityState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        logger.info("CityState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        logger.info("CityState handles packet");
        return true;
    }
}
