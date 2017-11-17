package statemachine;

import packets.Packet;
import states.State;

public interface PendingStack {
    void pushState(State.StateId stateId);

    void popState();

    void clearState();

    @SuppressWarnings("unused")
    void update();

    @SuppressWarnings("unused")
    void handlePacket(Packet packet);
}
