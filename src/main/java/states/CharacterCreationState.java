package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CharacterCreationState extends State {
    public CharacterCreationState(PendingStack stack) {
        super(stack);
        System.out.println("CharacterCreationState created");
    }

    @Override
    public boolean update() {
        System.out.println("CharacterCreationState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        System.out.println("CharacterCreationState handles packet");
        return true;
    }
}
