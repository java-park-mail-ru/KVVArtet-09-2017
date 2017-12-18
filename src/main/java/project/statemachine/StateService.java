package project.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.states.CharacterListState;
import project.states.DungeonState;
import project.states.LobbyState;
import project.states.State;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import java.util.*;

public class StateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);
    private List<State> stateList = new ArrayList<>();

    public Message handleMessage(final Message message, Integer forUser) {
        LOGGER.info("StateService call: ");
        Message response = null;
        for (State state : stateList) {
            response = state.handleMessage(message, forUser);
            if (response != null) {
                break;
            }
        }
        if (response == null) {
            response = new ErrorMessage("unable to handle message");
        }
        return response;
    }

    public StateService(){
        stateList.add(new CharacterListState());
        stateList.add(new DungeonState());
        stateList.add(new LobbyState());
    };
}
