package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.websocket.handlers.ActionRequestHandler;
import project.websocket.handlers.NextRoomRequestHandler;
import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.NextRoomRequestMessage;

public class DungeonState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(DungeonState.class);

    public DungeonState(){
        super();
        handlersMap.put(ActionRequestMessage.class, new ActionRequestHandler());
        handlersMap.put(NextRoomRequestMessage.class, new NextRoomRequestHandler());
    }
}
