package states;

import packets.Packet;
import statemachine.PendingStack;

public class CharacterListState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CharacterListState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        LOGGER.info("CharacterListState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        LOGGER.info("CharacterListState handles packet");
        return true;
    }
}
