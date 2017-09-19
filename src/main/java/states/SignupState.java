package states;

import packets.Packet;
import statemachine.PendingStack;

public class SignupState extends AbstractState {
    public SignupState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("SignupState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("SignupState handles packet");
        return true;
    }
}
