package gamemechanics.flyweights.abilities;

import gamemechanics.battlefield.Tile;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.AbilityEffect;
import gamemechanics.interfaces.AliveEntity;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class IngameAbility implements Ability {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer abilityID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final Integer maxDistance;
    private final Integer area;
    private final Integer categories;
    private final Integer cooldown;
    private final List<Integer> effect;
    private final AbilityEffect perform;

    public static class AbilityModel {
        public String name;
        public String description;
        public Integer maxDistance;
        public Integer area;
        public Integer categories;
        public Integer cooldown;
        public List<Integer> effect;
        public AbilityEffect perform;

        public AbilityModel(String name, String description, Integer maxDistance, Integer area,
                            Integer categories, Integer cooldown, List<Integer> effect, AbilityEffect perform) {
            this.name = name;
            this.description = description;
            this.maxDistance = maxDistance;
            this.area = area;
            this.categories = categories;
            this.cooldown = cooldown;
            this.effect = effect;
            this.perform = perform;
        }
    }

    public IngameAbility(AbilityModel model) {
        name = model.name;
        description = model.description;
        maxDistance = model.maxDistance;
        area = model.area;
        categories = model.categories;
        cooldown = model.cooldown;
        effect = model.effect;
        perform = model.perform;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    public Integer getID() {
        return abilityID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getMaxDistance() {
        return maxDistance;
    }

    @Override
    public Integer getArea() {
        return 0;
    }

    @Override
    public Integer getCooldown() {
        return cooldown;
    }

    @Override
    public Integer getCategories() {
        return categories;
    }

    @Override
    public Boolean execute(AliveEntity sender, Tile target) {
        return perform.execute(sender, target, effect);
    }

    protected Integer getEffect() {
        Random random = new Random(System.currentTimeMillis());
        return effect.get(DigitsPairIndices.MIN_VALUE_INDEX)
                + random.nextInt(effect.get(DigitsPairIndices.MAX_VALUE_INDEX)
                - effect.get(DigitsPairIndices.MIN_VALUE_INDEX));
    }
}
