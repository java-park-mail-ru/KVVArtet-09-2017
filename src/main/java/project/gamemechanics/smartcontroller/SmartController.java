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
    private Deque<Message> messageQueue = new ArrayDeque<>();
    private UserCharacter activeChar;
    private CharacterList characterList;
    private StateStack stateMachine = new StateStack();
    private WebSocketSession webSocketSession;

    public Queue<Message> getMessageQueue() {
        return messageQueue;
    }

    public void setMessageQueue(Deque<Message> messageQueue) {
        this.messageQueue = messageQueue;
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

    @SuppressWarnings("OverlyComplexMethod")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        //noinspection QuestionableName
        final SmartController that = (SmartController) o;

        //noinspection OverlyComplexBooleanExpression
        return (messageQueue != null ? messageQueue.equals(that.messageQueue) : that.messageQueue == null)
                && (activeChar != null ? activeChar.equals(that.activeChar) : that.activeChar == null)
                && (characterList != null ? characterList.equals(that.characterList) : that.characterList == null)
                && (stateMachine != null ? stateMachine.equals(that.stateMachine) : that.stateMachine == null)
                && (webSocketSession != null ? webSocketSession.equals(that.webSocketSession)
                : that.webSocketSession == null);
    }

    @Override
    public int hashCode() {
        int result = messageQueue != null ? messageQueue.hashCode() : 0;
        result = 31 * result + (activeChar != null ? activeChar.hashCode() : 0);
        result = 31 * result + (characterList != null ? characterList.hashCode() : 0);
        result = 31 * result + (stateMachine != null ? stateMachine.hashCode() : 0);
        result = 31 * result + (webSocketSession != null ? webSocketSession.hashCode() : 0);
        return result;
    }

}
