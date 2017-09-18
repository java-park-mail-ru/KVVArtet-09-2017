package statemachine;

import states.State;
import packets.Packet;

public interface PendingStack {
    void pushState(State.StateId stateId);
    void popState();
    void clearState();
    void update();
    void handlePacket(final Packet packet);
}
