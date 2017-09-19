package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CharacterListState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CharacterListState(PendingStack stack) {
        super(stack);
    }

    @Override
    public boolean update() {
        logger.info("CharacterListState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        logger.info("CharacterListState handles packet");
        return true;
    }
}
