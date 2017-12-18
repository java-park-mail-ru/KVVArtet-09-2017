package project.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import project.server.models.User;
import project.server.services.UserService;
import project.websocket.messages.Message;
import project.websocket.services.ConnectionPoolService;

import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;


public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    private @NotNull UserService userService;
    private final @NotNull ConnectionPoolService connectionPoolService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull UserService authService,
                             @NotNull ConnectionPoolService connectionPoolService,
                             ObjectMapper objectMapper) {
        this.userService = authService;
        this.connectionPoolService = connectionPoolService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final Integer userId = (Integer) webSocketSession.getAttributes().get("userId");
        if (userId == null || userService.getUserById(userId) == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Openning websocket session is denied.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        connectionPoolService.registerUser(userId, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Integer userId = (Integer) webSocketSession.getAttributes().get("userId");
        final User user;
        if (userId == null || (user = userService.getUserById(userId)) == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Text message can't be handled.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(User user, TextMessage text) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at game response", ex);
            return;
        }
        connectionPoolService.putMessage(user.getId(), message);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final Integer userId = (Integer) webSocketSession.getAttributes().get("userId");
        if (userId == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        connectionPoolService.removeUser(userId);
    }

    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status = closeStatus == null ? SERVER_ERROR : closeStatus;
        try {
            session.close(status);
        } catch (Exception ignore) {
        }

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
