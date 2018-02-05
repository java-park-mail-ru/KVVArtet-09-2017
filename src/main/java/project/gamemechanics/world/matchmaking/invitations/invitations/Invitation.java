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

    @NotNull Integer getStatus();
    void setStatus(@NotNull Integer status);

    @NotNull Boolean isIdle();
    @NotNull Boolean isConfirm();
    @NotNull Boolean isCancel();
    @NotNull Boolean isExpired();
}
