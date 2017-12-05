package project.websocket.services;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.gamemechanics.smartcontroller.SmartController;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import project.websocket.ConnectionPool;
import project.websocket.messages.Message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectionPoolService {

    private ConnectionPool connectionPool = new ConnectionPool();
    private Map<Integer, SmartController> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public ConnectionPoolService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerUser(@NotNull Integer userId, @NotNull WebSocketSession webSocketSession) {
        final SmartController smartControllerForUser = connectionPool.getElement();
        smartControllerForUser.setWebSocketSession(webSocketSession);
        sessions.put(userId, smartControllerForUser);
    }

    public boolean isConnected(@NotNull Integer userId) {
        return sessions.containsKey(userId) && sessions.get(userId).getWebSocketSession().isOpen();
    }

    public void removeUser(@NotNull Integer userId)
    {
        connectionPool.addElement(sessions.get(userId));
        sessions.remove(userId);
    }

    public void cutDownConnection(@NotNull Integer userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId).getWebSocketSession();
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(@NotNull Integer userId, @NotNull Message message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(userId).getWebSocketSession();
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + userId);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        //noinspection OverlyBroadCatchBlock
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            throw new IOException("Unnable to send message", e);
        }
    }
}
