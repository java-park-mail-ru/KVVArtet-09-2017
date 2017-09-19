package states;

import statemachine.PendingStack;
import packets.Packet;

public class SignupState extends AbstractState {
    public SignupState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        logger.info("SignupState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        logger.info("SignupState handles packet");
        return true;
    }
}
