package states;

import packets.Packet;
import statemachine.PendingStack;

public class CharacterCreationState extends AbstractState {
    public CharacterCreationState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("CharacterCreationState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("CharacterCreationState handles packet");
        return true;
    }
}
