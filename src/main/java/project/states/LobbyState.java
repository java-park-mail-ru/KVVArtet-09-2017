package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import project.websocket.handlers.LobbyRequestHandler;
import project.websocket.messages.LobbyRequestMessage;
@Component
public class LobbyState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LobbyState.class);

    public LobbyState(){
        super();
    }
}
