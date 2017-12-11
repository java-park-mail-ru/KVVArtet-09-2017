package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.websocket.handlers.LobbyRequestHandler;
import project.websocket.messages.LobbyRequestMessage;

public class LobbyState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);

    public LobbyState(){
        super();
        handlersMap.put(LobbyRequestMessage.class, new LobbyRequestHandler());
    }
}
