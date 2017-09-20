package statemachine;

import packets.Packet;
import states.State;

public interface PendingStack {
    void pushState(State.StateId stateId);

    void popState();

    void clearState();

    void update();

    void handlePacket(final Packet packet);
}
