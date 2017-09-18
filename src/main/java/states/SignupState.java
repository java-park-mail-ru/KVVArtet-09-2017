package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class SignupState extends AbstractState {
    public SignupState(PendingStack stack) {
        super(stack);
        System.out.println("SignupState created");
    }

    @Override
    public boolean update() {
        System.out.println("SignupState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        System.out.println("SignupState handles packet");
        return true;
    }
}
