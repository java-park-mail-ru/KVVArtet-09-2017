package project.gamemechanics.battlefield.actionresults;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;

@SuppressWarnings("RedundantSuppression")
public class BattleActionResult implements ActionResult {
    private final Integer actionID;
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final List<TurnEvent> events;
    private Boolean isProcessed = false;

    public BattleActionResult(@NotNull Integer actionID, @NotNull MapNode sender,
                              @Nullable MapNode target, @Nullable Ability ability,
                              @NotNull List<TurnEvent> events) {
        this.actionID = actionID;
        this.sender = sender;
        this.target = target;
        this.ability = ability;
        this.events = events;
    }

    @Override
    public @NotNull MapNode getSender() {
        return sender;
    }

    @Override
    public @Nullable MapNode getTarget() {
        return target;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable Ability getAbility() {
        return ability;
    }

    @Override
    public @NotNull Integer getActionID() {
        return actionID;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getEventsCount() {
        return events.size();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable TurnEvent getEvent(@NotNull Integer eventIndex) {
        if (eventIndex < 0 || eventIndex >= events.size()) {
            return null;
        }
        return events.get(eventIndex);
    }

    @Override
    public void addEvent(@NotNull TurnEvent event) {
        events.add(event);
    }

    @Override
    public void addEvent(@NotNull Integer position, @NotNull TurnEvent event) {
        events.add(position, event);
    }

    @Override
    public @NotNull Integer getEventIndex(@NotNull TurnEvent event) {
        for (Integer i = 0; i < events.size(); ++i) {
            if (events.get(i).equals(event)) {
                return i;
            }
        }
        return Constants.WRONG_INDEX;
    }

    @Override
    public @NotNull Boolean getIsProcessed() {
        return isProcessed;
    }

    @Override
    public void markProcessed() {
        isProcessed = true;
    }
}
