package project.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import project.websocket.handlers.CharacterListRequestHandler;
import project.websocket.handlers.CreateCharacterRequestHandler;
import project.websocket.handlers.MessageHandler;
import project.websocket.messages.CharacterListRequestMessage;
import project.websocket.messages.CreateCharacterRequestMessage;
import project.websocket.messages.Message;

public class CharacterListState extends AbstractState {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharacterListState.class);

    public CharacterListState(){
        super();
        handlersMap.put(CharacterListRequestMessage.class, new CharacterListRequestHandler());
        handlersMap.put(CreateCharacterRequestMessage.class, new CreateCharacterRequestHandler());
    }
}
