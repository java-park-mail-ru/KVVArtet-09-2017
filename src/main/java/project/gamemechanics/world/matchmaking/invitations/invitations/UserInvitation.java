package project.gamemechanics.world.matchmaking.invitations.invitations;

import javax.validation.constraints.NotNull;

public class UserInvitation implements Invitation {
    private Integer status = VS_IDLE;
    private Integer lifetime = 0;

    public UserInvitation() {
    }

    public UserInvitation(@NotNull Integer status) {
        this.status = status;
    }

    @Override
    public @NotNull Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(@NotNull Integer status) {
        if (this.status != VS_EXPIRED) {
            this.status = status;
        }
    }

    @Override
    public void update() {
        if (lifetime < TIMEOUT_LOOPS_COUNT - 1) {
            ++lifetime;
        } else {
            status = VS_EXPIRED;
        }
    }

    @Override
    public @NotNull Boolean isIdle() {
        return status == VS_IDLE;
    }

    @Override
    public @NotNull Boolean isConfirm() {
        return status == VS_CONFIRM;
    }

    @Override
    public @NotNull Boolean isCancel() {
        return status == VS_CANCEL;
    }

    @Override
    public @NotNull Boolean isExpired() {
        return status == VS_EXPIRED;
    }
}
