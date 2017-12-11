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

public class AreaEffectTileset extends MapNodeTileset {
    private final Integer abilityID;
    private final MapNode sender;
    private final List<Effect> effects;
    private final List<Integer> healthAffection;
    private final Set<Integer> affectedCategories;

    public AreaEffectTileset(Integer abilityID, @NotNull MapNode sender, @NotNull MapNode target, Integer shape,
                             Integer size, @NotNull List<Effect> effects, @NotNull List<Integer> healthAffection,
                             Set<Integer> affectedCategories) {
        super(target, shape, calculateDirection(sender, target), size);
        this.abilityID = abilityID;
        this.sender = sender;
        this.effects = effects;
        this.healthAffection = healthAffection;
        this.affectedCategories = affectedCategories;
    }

    @Override
    public void applyEffects(@NotNull List<TurnEvent> events) {
        for (MapNode tile : getTileset()) {
            if (tile.isOccupied()) {
                applyOnInhabitant(sender.getInhabitant(), tile, events);
            } else {
                events.add(EventsFactory.makeCastEvent(tile, abilityID));
            }
        }
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private void applyOnInhabitant(@NotNull AliveEntity sender, @NotNull MapNode target,
                                   @NotNull List<TurnEvent> events) {
        //noinspection OverlyComplexBooleanExpression
        if ((affectedCategories.contains(BattlefieldObjectsCategories.BO_ENEMY) && !areOnSameSide(sender,
                target.getInhabitant())) || (affectedCategories.contains(BattlefieldObjectsCategories.BO_ALLY)
                && areOnSameSide(sender, target.getInhabitant()))) {
            for (Effect effect : effects) {
                target.getInhabitant().addEffect(effect);
                events.add(EventsFactory.makeApplyEffectEvent(target, effect));
            }
            final Integer healthAffectionValue = getAffection(sender);
            target.getInhabitant().affectHitpoints(healthAffectionValue);
            events.add(EventsFactory.makeHitpointsChangeEvent(target, healthAffectionValue));
        }
    }

    private Boolean areOnSameSide(AliveEntity lhs, AliveEntity rhs) {
        return Objects.equals(lhs.getProperty(PropertyCategories.PC_SQUAD_ID),
                rhs.getProperty(PropertyCategories.PC_SQUAD_ID));
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    private Integer getAffection(@NotNull AliveEntity sender) {
        final Random random = new Random(System.currentTimeMillis());
        Integer resultingHealthAffection = healthAffection.get(DigitsPairIndices.MIN_VALUE_INDEX) +
                random.nextInt(healthAffection.get(DigitsPairIndices.MAX_VALUE_INDEX)
                        - healthAffection.get(DigitsPairIndices.MIN_VALUE_INDEX));
        final Integer criticalChance = sender.getProperty(PropertyCategories.PC_RATINGS,
                CharacterRatings.CR_CRITICAL_HIT.asInt());
        if (random.nextInt(Constants.PERCENTAGE_CAP_INT) < criticalChance) {
            resultingHealthAffection *= Constants.CRITICAL_HIT_MULTIPLIER;
        }
        return resultingHealthAffection;
    }
}
