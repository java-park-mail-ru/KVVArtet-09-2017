package project.gamemechanics.world.matchmaking.invitations.polls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPoll implements Poll {
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private final Integer pollId = COUNTER.getAndIncrement();

    @JsonProperty("id")
    @Override
    public @NotNull Integer getID() {
        return pollId;
    }

    @JsonIgnore
    @Override
    public @NotNull Integer getInstancesCount() {
        return COUNTER.get();
    }
}
