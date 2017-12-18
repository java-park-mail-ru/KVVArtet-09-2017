package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import project.websocket.handlers.ActionRequestHandler;
import project.websocket.handlers.NextRoomRequestHandler;
import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.NextRoomRequestMessage;
@Component
public class DungeonState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonState.class);

    public DungeonState(){
        super();
        LOGGER.info("DungeonState");;
    }
}
