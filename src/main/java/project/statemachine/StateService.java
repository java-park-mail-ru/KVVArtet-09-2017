package project.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.states.State;
import project.states.StateFactory;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import java.util.*;

public class StateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StateService.class);
    private Map<Message, State> messageToStateMap = new HashMap<>();
    private List<State> stateList = new ArrayList<>();

    public Message handleMessage(final Message message) {
        LOGGER.info("state handlePacket call: ");
        Message response = null;
        for (State state : stateList) {
            response = state.handle(message);
            if (response != null) {
                break;
            }
        }
        if (response == null) {
            response = new ErrorMessage("unable to handle message");
        }
        return response;
    }



    StateService(){ };
}
