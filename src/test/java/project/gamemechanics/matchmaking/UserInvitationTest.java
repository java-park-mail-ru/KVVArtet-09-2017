package project.gamemechanics.matchmaking;

import org.junit.Test;
import project.gamemechanics.world.matchmaking.invitations.invitations.Invitation;
import project.gamemechanics.world.matchmaking.invitations.invitations.UserInvitation;

import static org.junit.Assert.assertTrue;

public class UserInvitationTest {
    @Test
    public void idleOnCreationTest() {
        final Invitation invitation = new UserInvitation();
        assertTrue(invitation.isIdle());
    }

    @Test
    public void expirationTest() {
        final Invitation invitation = new UserInvitation();
        assertTrue(invitation.isIdle());
        for (Integer i = 0; i <= Invitation.TIMEOUT_LOOPS_COUNT; ++i) {
            invitation.update();
        }
        assertTrue(invitation.isExpired());
        invitation.setStatus(Invitation.VS_CONFIRM);
        assertTrue(!invitation.isConfirm() && invitation.isExpired());
    }

    @Test
    public void setStatusTest() {
        final Invitation invitation = new UserInvitation();
        assertTrue(invitation.isIdle());
        invitation.setStatus(Invitation.VS_CANCEL);
        assertTrue(invitation.isCancel());
        invitation.setStatus(Invitation.VS_CONFIRM);
        assertTrue(invitation.isConfirm());
        invitation.setStatus(Invitation.VS_IDLE);
        assertTrue(invitation.isIdle());
        invitation.setStatus(Invitation.VS_EXPIRED);
        assertTrue(invitation.isExpired());
        invitation.setStatus(Invitation.VS_CONFIRM);
        assertTrue(invitation.isExpired());
    }
}
