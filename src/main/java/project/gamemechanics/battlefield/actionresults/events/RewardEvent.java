package project.gamemechanics.battlefield.actionresults.events;

import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

@SuppressWarnings("ALL")
public class RewardEvent implements TurnEvent {
    private final MapNode where;
    private final Integer expAmount;
    private final Integer goldAmount;

    public RewardEvent(@NotNull MapNode where, @NotNull Integer expAmount, @NotNull Integer goldAmount) {
        this.where = where;
        this.expAmount = expAmount;
        this.goldAmount = goldAmount;
    }

    @Override
    public @NotNull Integer getEventKind() {
        return EventCategories.EC_REWARD;
    }

    @Override
    public @NotNull MapNode getWhere() {
        return where;
    }

    public @NotNull Integer getExpAmount() {
        return expAmount;
    }

    public @NotNull Integer getGoldAmount() {
        return goldAmount;
    }
}
