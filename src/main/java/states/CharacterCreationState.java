package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CharacterCreationState extends AbstractState {
    public CharacterCreationState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        logger.info("CharacterCreationState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        logger.info("CharacterCreationState handles packet");
        return true;
    }
}
