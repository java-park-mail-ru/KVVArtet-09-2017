package project.gamemechanics.world.matchmaking.invitations.invitations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotNull;

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

    @NotNull Integer getStatus();

    void setStatus(@NotNull Integer status);

    void update();

    @SuppressWarnings("unused")
    @NotNull Boolean isIdle();

    @NotNull Boolean isConfirm();

    @NotNull Boolean isCancel();

    @NotNull Boolean isExpired();
}
