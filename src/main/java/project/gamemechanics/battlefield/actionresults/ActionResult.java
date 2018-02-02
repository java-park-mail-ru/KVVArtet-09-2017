package project.gamemechanics.battlefield.actionresults;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;

@SuppressWarnings("RedundantSuppression")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(BattleActionResult.class),
})
public interface ActionResult {
    @SuppressWarnings("unused")
    @NonNull Integer getActionID();

    @NonNull MapNode getSender();

    @SuppressWarnings({"ConstantConditions", "unused"})
    default @Nullable MapNode getTarget() {
        return null;
    }

    @SuppressWarnings({"ConstantConditions", "unused"})
    default @Nullable Ability getAbility() {
        return null;
    }

    @NonNull Integer getEventsCount();

    @Nullable TurnEvent getEvent(@NotNull Integer eventIndex);

    void addEvent(@NotNull TurnEvent event);

    void addEvent(@NotNull Integer position, @NotNull TurnEvent event);

    @NotNull Integer getEventIndex(@NotNull TurnEvent event);

    @NotNull Boolean getIsProcessed();

    void markProcessed();
}
