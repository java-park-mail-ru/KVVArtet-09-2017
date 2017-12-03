package states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import websocket.messages.Message;
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
    public boolean handleMessage(final Message message) {
        LOGGER.info("DungeonState handles packet");
        return true;
    }
}
