package project.gamemechanics.smartcontroller;

import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.charlist.CharacterList;
import org.springframework.web.socket.WebSocketSession;
import project.statemachine.StateStack;
import project.websocket.messages.Message;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

public class SmartController {
    private Deque<Message> inboxMessageQueue = new ArrayDeque<>();
    private Deque<Message> outboxMessageQueue = new ArrayDeque<>();
    private UserCharacter activeChar;
    private CharacterList characterList;
    private StateStack stateMachine = new StateStack();
    private WebSocketSession webSocketSession;

    void tick() {
        while(!outboxMessageQueue.isEmpty()){
            outboxMessageQueue.add(stateMachine.handleMessage(inboxMessageQueue.getFirst()));
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

    public StateStack getStateMachine() {
        return stateMachine;
    }

    public void setStateMachine(StateStack stateMachine) {
        this.stateMachine = stateMachine;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }



}
