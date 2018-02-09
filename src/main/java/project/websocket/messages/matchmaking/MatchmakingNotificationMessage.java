package project.websocket.messages.matchmaking;

import project.websocket.messages.TextMessage;

import javax.validation.constraints.NotNull;

public class MatchmakingNotificationMessage extends TextMessage {
    public MatchmakingNotificationMessage(@NotNull String notification) {
        super(notification);
    }
}
