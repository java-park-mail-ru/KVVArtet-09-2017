package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.messages.Message;
import statemachine.PendingStack;

@SuppressWarnings("SpellCheckingInspection")
public class SignupState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(SignupState.class);

    SignupState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("SignupState updated");
        return true;
    }

    @Override
    public boolean handleMessage(final Message message) {
        LOGGER.info("SignupState handles packet");
        return true;
    }
}
