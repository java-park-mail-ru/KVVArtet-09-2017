package project.gamemechanics.smartcontroller;

import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import project.statemachine.StateService;
import project.websocket.messages.Message;

import java.util.ArrayDeque;
import java.util.Deque;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class SmartController {
    private final Deque<Message> inboxMessageQueue = new ArrayDeque<>();
    private final Deque<Message> outboxMessageQueue = new ArrayDeque<>();
    private UserCharacter activeChar;
    private CharacterList characterList;
    private final StateService stateService = new StateService();
    private WebSocketSession webSocketSession;
    private Integer ownerID;

    public void tick() {
        while (!inboxMessageQueue.isEmpty()) {
            outboxMessageQueue.add(stateService.handleMessage(inboxMessageQueue.getFirst(), this.ownerID));
        }
    }

    @SuppressWarnings("ConstantConditions")
    public Message getOutboxMessage() {
        if (outboxMessageQueue.isEmpty()) {
            return null;
        } else {
            return outboxMessageQueue.getFirst();
        }
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

    public Boolean isValid() {
        return webSocketSession.isOpen();
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getOwnerID() {
        return ownerID;
    }
}
