package statemachine;

import states.AbstractState;
import packets.Packet;

public interface PendingStack {
    void pushState(AbstractState.StateId stateId);
    void popState();
    void clearState();
    void update();
    void handlePacket(final Packet packet);
}
