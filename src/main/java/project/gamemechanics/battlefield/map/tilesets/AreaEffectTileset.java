package project.gamemechanics.battlefield.map.tilesets;

import project.gamemechanics.battlefield.BattlefieldObjectsCategories;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.CharacterRatings;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Effect;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("RedundantSuppression")
public class AreaEffectTileset extends MapNodeTileset {
    private final Integer abilityID;
    private final MapNode sender;
    private final List<Effect> effects;
    private final List<Integer> healthAffection;
    private final Set<Integer> affectedCategories;
    // CHECKSTYLE:OFF
    public AreaEffectTileset(@NotNull Integer abilityID, @NotNull MapNode sender, @NotNull MapNode target,
                             @NotNull Integer shape, @NotNull Integer size, @NotNull List<Effect> effects,
                             @NotNull List<Integer> healthAffection, @NotNull Set<Integer> affectedCategories) {
        super(target, shape, calculateDirection(sender, target), size);
        this.abilityID = abilityID;
        this.sender = sender;
        this.effects = effects;
        this.healthAffection = healthAffection;
        this.affectedCategories = affectedCategories;
    }
    // CHECKSTYLE:ON

    @Override
    public void applyEffects(@NotNull List<TurnEvent> events) {
        for (MapNode tile : getTileset()) {
            if (tile.isOccupied()) {
                applyOnInhabitant(Objects.requireNonNull(sender.getInhabitant()), tile, events);
            } else {
                events.add(EventsFactory.makeCastEvent(tile, abilityID));
            }
        }
    }
    // CHECKSTYLE:OFF
    @SuppressWarnings({"ParameterHidesMemberVariable", "OverlyComplexBooleanExpression"})
    private void applyOnInhabitant(@NotNull AliveEntity sender, @NotNull MapNode target,
                                   @NotNull List<TurnEvent> events) {
        //noinspection OverlyComplexBooleanExpression
        // CHECKSTYLE:ON

        if ((affectedCategories.contains(BattlefieldObjectsCategories.BO_ENEMY) && !areOnSameSide(sender,
                Objects.requireNonNull(target.getInhabitant())))
                || (affectedCategories.contains(BattlefieldObjectsCategories.BO_ALLY)
                && areOnSameSide(sender, Objects.requireNonNull(target.getInhabitant())))) {
            for (Effect effect : effects) {
                target.getInhabitant().addEffect(effect);
                events.add(EventsFactory.makeApplyEffectEvent(target, effect));
            }
            final Integer healthAffectionValue = getAffection(sender);
            target.getInhabitant().affectHitpoints(healthAffectionValue);
            events.add(EventsFactory.makeHitpointsChangeEvent(target, healthAffectionValue));
        }
    }

    private @NotNull Boolean areOnSameSide(@NotNull AliveEntity lhs, @NotNull AliveEntity rhs) {
        return Objects.equals(lhs.getProperty(PropertyCategories.PC_SQUAD_ID),
                rhs.getProperty(PropertyCategories.PC_SQUAD_ID));
    }
    // CHECKSTYLE:OFF
    @SuppressWarnings("ParameterHidesMemberVariable")
    private @NotNull Integer getAffection(@NotNull AliveEntity sender) {
        // CHECKSTYLE:ON

        final Random random = new Random(System.currentTimeMillis());
        Integer resultingHealthAffection = healthAffection.get(DigitsPairIndices.MIN_VALUE_INDEX)
                + random.nextInt(Math.abs(healthAffection.get(DigitsPairIndices.MAX_VALUE_INDEX)
                        - healthAffection.get(DigitsPairIndices.MIN_VALUE_INDEX))) * -1;
        final Integer criticalChance = sender.getProperty(PropertyCategories.PC_RATINGS,
                CharacterRatings.CR_CRITICAL_HIT.asInt());
        if (random.nextInt(Constants.PERCENTAGE_CAP_INT) < criticalChance) {
            resultingHealthAffection *= Constants.CRITICAL_HIT_MULTIPLIER;
        }
        return resultingHealthAffection;
    }
}
