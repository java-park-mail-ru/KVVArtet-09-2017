package states;

import statemachine.PendingStack;
import packets.Packet;

import java.lang.*;

public class CityState extends AbstractState {
    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    public CityState(PendingStack stack) {
        super(stack);
        System.out.println("CityState created");
    }

    @Override
    public boolean update() {
        System.out.println("CityState updated");
        return true;
    }

    @Override
    public boolean handlePacket(final Packet packet) {
        System.out.println("CityState handles packet");
        return true;
    }
}
