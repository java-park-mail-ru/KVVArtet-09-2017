package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CharacterListState extends State {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CharacterListState(PendingStack stack) {
        super(stack);
        System.out.println("CharacterListState created");
    }

    @Override
    public boolean update() {
        System.out.println("CharacterListState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        System.out.println("CharacterListState handles packet");
        return true;
    }
}
