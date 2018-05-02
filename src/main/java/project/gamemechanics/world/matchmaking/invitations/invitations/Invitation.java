package project.gamemechanics.world.matchmaking.invitations.invitations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotNull;

/**
 * user invitation status. Is created before every game
 * mode (except solo PvE one) matchmaking's done before actual
 * instance (Land or Dungeon one) {@see Instance} {@see LandInstance}
 * {@see DungeonInstance} will be created, to ensure that all players
 * are ready to start and haven't changed their minds.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(UserInvitation.class)
})
public interface Invitation {
    int VS_IDLE = 0;
    int VS_CONFIRM = 1;
    int VS_CANCEL = 2;
    int VS_EXPIRED = 3;

    int TIMEOUT_LOOPS_COUNT = 7200; // 2 minutes on 60 loops per second

    /**
     * get current user response status
     * (no answer, confirmed, canceled or expired).
     *
     * @return invitation status
     */
    @SuppressWarnings("unused")
    @NotNull Integer getStatus();

    /**
     * change current user response status
     * to specified one.
     *
     * @param status - new user response status
     */
    void setStatus(@NotNull Integer status);

    /**
     * updates user response status during server tick.
     */
    void update();

    /**
     * checks if the invitation still isn't responded.
     *
     * @return true if current status is VS_IDLE, or false otherwise
     */
    @SuppressWarnings("unused")
    @NotNull Boolean isIdle();

    /**
     * checks if the invitation is confirmed.
     *
     * @return true if current status is VS_CONFIRM, or false otherwise
     */
    @NotNull Boolean isConfirm();

    /**
     * checks if the invitation is rejected.
     *
     * @return true if current status is VS_CANCEL, or false otherwise
     */
    @NotNull Boolean isCancel();

    /**
     * checks if the invitation has expired.
     *
     * @return true if current status is VS_EXPIRED, or false otherwise
     */
    @NotNull Boolean isExpired();
}
