package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import packets.Packet;
import statemachine.PendingStack;

public class SignupState extends AbstractState {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SignupState.class);

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
