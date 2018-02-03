package project.websocket.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import project.gamemechanics.smartcontroller.SmartController;
import project.websocket.ConnectionPool;
import project.websocket.messages.Message;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"UnusedAssignment", "SameParameterValue", "unused"})
@Service
public class ConnectionPoolService {

    private final ConnectionPool connectionPool = new ConnectionPool();
    private final Map<Integer, SmartController> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public ConnectionPoolService(@NotNull ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void tick() {
        for (SmartController smart : sessions.values()) {
            if (smart.isValid()) {
                smart.tick();
                Message response = null;
                while ((response = smart.getOutboxMessage()) != null) {
                    try {
                        this.sendMessageToUser(smart.getOwnerID(), response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                removeUser(smart.getOwnerID());
            }
        }

    }

    public void putMessage(@NotNull Integer userId, @NotNull Message message) {
        sessions.get(userId).addInboxMessage(message);
    }

    public void registerUser(@NotNull Integer userId, @NotNull WebSocketSession webSocketSession) {
        final SmartController smartControllerForUser = connectionPool.getElement();
        smartControllerForUser.setWebSocketSession(webSocketSession);
        smartControllerForUser.setOwnerID(userId);
        sessions.put(userId, smartControllerForUser);
    }

    public boolean isConnected(@NotNull Integer userId) {
        return sessions.containsKey(userId) && Objects.requireNonNull(
                sessions.get(userId).getWebSocketSession()).isOpen();
    }

    public void removeUser(@NotNull Integer userId) {
        connectionPool.addElement(sessions.get(userId));
        sessions.remove(userId);
    }

    private void cutDownConnection(@NotNull Integer userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId).getWebSocketSession();
        if (webSocketSession != null && webSocketSession.isOpen()) {
            /// CHECKSTYLE:OFF
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
            // CHECKSTYLE:ON

        }
    }

    private void sendMessageToUser(@NotNull Integer userId, @NotNull Message message) throws IOException {
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

    public @NotNull Map<Integer, SmartController> getActiveSmartControllers() {
        return sessions;
    }

    public @NotNull SmartController getSmartController(@NotNull Integer userID) {
        return sessions.get(userID);
    }

    public void reset() {
        for (Map.Entry<Integer, SmartController> entry : sessions.entrySet()) {
            cutDownConnection(entry.getKey(), CloseStatus.SERVER_ERROR);
        }
    }
}
