package project.gamemechanics.smartcontroller;

import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import org.springframework.web.socket.WebSocketSession;
import project.statemachine.StateService;
import project.websocket.messages.Message;

import java.util.ArrayDeque;
import java.util.Deque;

public class SmartController {
    private Deque<Message> inboxMessageQueue = new ArrayDeque<>();
    private Deque<Message> outboxMessageQueue = new ArrayDeque<>();
    private UserCharacter activeChar;
    private CharacterList characterList;
    private StateService stateService = new StateService();
    private WebSocketSession webSocketSession;

    void tick() {
        while(!inboxMessageQueue.isEmpty()){
            outboxMessageQueue.add(stateService.handleMessage(inboxMessageQueue.getFirst()));
        }
    }

    public Message getOutboxMessage() {
        return outboxMessageQueue.getFirst();
    }

    public void addInboxMessage(Message inboxMessage) {
        this.inboxMessageQueue.add(inboxMessage);
    }

    public UserCharacter getActiveChar() {
        return activeChar;
    }

    public void setActiveChar(UserCharacter activeChar) {
        this.activeChar = activeChar;
    }

    public CharacterList getCharacterList() {
        return characterList;
    }

    public void setCharacterList(CharacterList characterList) {
        this.characterList = characterList;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

}
