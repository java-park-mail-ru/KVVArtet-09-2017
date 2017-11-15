package gamemechanics.battlefield.actionresults;

import gamemechanics.battlefield.actionresults.events.TurnEvent;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;

public class BattleActionResult implements ActionResult {
    private final Integer actionID;
    private final MapNode sender;
    private final MapNode target;
    private final Ability ability;
    private final List<TurnEvent> events;
    private Boolean isProcessed = false;

    public BattleActionResult(@NotNull Integer actionID,@NotNull MapNode sender,
                              MapNode target, Ability ability,
                              @NotNull List<TurnEvent> events) {
        this.actionID = actionID;
        this.sender = sender;
        this.target = target;
        this.ability = ability;
        this.events = events;
    }

    @Override
    public MapNode getSender() {
        return sender;
    }

    @Override
    public MapNode getTarget() {
        return target;
    }

    @Override
    public Ability getAbility() {
        return ability;
    }

    @Override
    public Integer getActionID() {
        return actionID;
    }

    @Override
    public Integer getEventsCount() {
        return events.size();
    }

    @Override
    public TurnEvent getEvent(Integer eventIndex) {
        if (eventIndex < 0 || eventIndex >= events.size()) {
            return null;
        }
        return events.get(eventIndex);
    }

    @Override
    public void addEvent(TurnEvent event) {
        events.add(event);
    }

    @Override
    public Boolean getIsProcessed() {
        return isProcessed;
    }

    @Override
    public void markProcessed() {
        isProcessed = true;
    }
}
