package project.websocket.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
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


@SuppressWarnings("OverlyBroadCatchBlock")
public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    private final @NotNull UserService userService;
    private final @NotNull ConnectionPoolService connectionPoolService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull UserService authService,
                             @NotNull ConnectionPoolService connectionPoolService,
                             @NotNull ObjectMapper objectMapper) {
        this.userService = authService;
        this.connectionPoolService = connectionPoolService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession webSocketSession) {
        final Integer userId = (Integer) webSocketSession.getAttributes().get("id");
        if (userId == null || userService.getUserById(userId) == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in."
                    + "Openning websocket session is denied.");
            closeSessionSilently(webSocketSession);
            return;
        }
        connectionPoolService.registerUser(userId, webSocketSession);
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession webSocketSession,
                                     @NotNull TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final Integer userId = (Integer) webSocketSession.getAttributes().get("id");
        final User user;
        // CHECKSTYLE:OFF
        if (userId == null || (user = userService.getUserById(userId)) == null) {
        // CHECKSTYLE:ON
            LOGGER.warn("User requested websocket is not registred or not logged in. Text message can't be handled.");
            closeSessionSilently(webSocketSession);
            return;
        }
        handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(@NotNull User user, @NotNull TextMessage text) {
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
    public void handleTransportError(@NotNull WebSocketSession webSocketSession,
                                     @NotNull Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession webSocketSession,
                                      @NotNull CloseStatus closeStatus) {
        final Integer userId = (Integer) webSocketSession.getAttributes().get("id");
        if (userId == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        connectionPoolService.removeUser(userId);
    }

    private void closeSessionSilently(@NotNull WebSocketSession session) {
        // CHECKSTYLE:OFF
        try {
            session.close(GameSocketHandler.ACCESS_DENIED);
        } catch (Exception ignore) {
        }
        // CHECKSTYLE:ON

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
