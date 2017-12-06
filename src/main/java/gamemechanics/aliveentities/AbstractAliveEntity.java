package gamemechanics.aliveentities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gamemechanics.components.affectors.AffectorCategories;
import gamemechanics.components.properties.Property;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.flyweights.CharacterRace;
import gamemechanics.globals.Constants;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.*;
import gamemechanics.items.containers.CharacterDoll;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.*;

public abstract class AbstractAliveEntity implements AliveEntity {
    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;
    private final List<Bag> bags;
    private final CharacterRole characterRole;
    private final List<Effect> effects = new ArrayList<>();

    @SuppressWarnings("PublicField")
    private static class AbstractAliveEntityModel {
        public final String name;
        public final String description;
        public final Map<Integer, Property> properties;
        public final List<Bag> bags; // for monsters bags will contain generated loot
        public final CharacterRole characterRole; // character class for user characters and playable bots
        // or an AI-driven role for monsters

        AbstractAliveEntityModel(@NotNull String name, @NotNull String description,
                                 @NotNull Map<Integer, Property> properties,
                                 @NotNull List<Bag> bags, @NotNull CharacterRole characterRole) {
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.bags = bags;
            this.characterRole = characterRole;
        }
    }

    @SuppressWarnings("PublicField")
    public static class NPCModel extends AbstractAliveEntityModel {
        public final DecisionMaker behavior;

        public NPCModel(@NotNull String name, @NotNull String description,
                        @NotNull Map<Integer, Property> properties,
                        @NotNull List<Bag> bags, @NotNull DecisionMaker behavior,
                        @NotNull CharacterRole characterRole) {
            super(name, description, properties, bags, characterRole);
            this.behavior = behavior;
        }

        public NPCModel(@NotNull String name, @NotNull String description,
                        @NotNull Map<Integer, Property> properties,
                        @NotNull List<Bag> bags, @NotNull CharacterRole characterRole) {
            super(name, description, properties, bags, characterRole);
            this.behavior = null;
        }
    }

    @SuppressWarnings("PublicField")
    public static class UserCharacterModel extends AbstractAliveEntityModel {
        public final Integer id;
        public final CharacterRace characterRace;
        public final CharacterDoll equipment;
        public final Map<Integer, Map<Integer, Integer>> perkRanks;

        public UserCharacterModel(@NotNull Integer id,
                                  @NotNull String name, @NotNull String description,
                                  @NotNull Map<Integer, Property> properties,
                                  @NotNull List<Bag> bags, @NotNull CharacterRole characterRole,
                                  @NotNull CharacterRace characterRace, @NotNull CharacterDoll equipment,
                                  @NotNull Map<Integer, Map<Integer, Integer>> perkRanks) {
            super(name, description, properties, bags, characterRole);
            this.id = id;
            this.characterRace = characterRace;
            this.equipment = equipment;
            this.perkRanks = perkRanks;
        }
    }

    public AbstractAliveEntity(@NotNull NPCModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        bags = model.bags;
        characterRole = model.characterRole;
    }

    AbstractAliveEntity(@NotNull UserCharacterModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        bags = model.bags;
        characterRole = model.characterRole;
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
    public Integer getLevel() {
        return getProperty(PropertyCategories.PC_LEVEL);
    }

    @Override
    public Boolean hasProperty(Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        if (propertyKind == PropertyCategories.PC_BASE_DAMAGE) {
            final Random random = new Random(System.currentTimeMillis());
            final List<Integer> damage = properties.get(propertyKind).getPropertyList();
            return damage.get(DigitsPairIndices.MIN_VALUE_INDEX)
                    + random.nextInt(damage.get(DigitsPairIndices.MAX_VALUE_INDEX)
                    - damage.get(DigitsPairIndices.MIN_VALUE_INDEX));
        }
        return properties.get(propertyKind).getProperty();
    }

    @Override
    public void addProperty(@NotNull Integer propertyKind, @NotNull Property property) {
        if (hasProperty(propertyKind)) {
            return;
        }
        properties.put(propertyKind, property);
    }

    @Override
    public void removeProperty(@NotNull Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.remove(propertyKind);
    }

    @Override
    public void setProperty(@NotNull Integer propertyKind, @NotNull Integer propertyValue) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.get(propertyKind).setSingleProperty(propertyValue);
    }

    @Override
    public void setProperty(@NotNull Integer propertyKind, @NotNull Integer propertyIndex,
                            @NotNull Integer propertyValue) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.get(propertyKind).setSingleProperty(propertyIndex, propertyValue);
    }

    @Override
    public void setProperty(@NotNull Integer propertyKind, @NotNull List<Integer> propertyValue) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.get(propertyKind).setPropertyList(propertyValue);
    }

    @Override
    public Boolean setProperty(@NotNull Integer propertyKind, @NotNull Map<Integer, Integer> propertyValue) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).setPropertyMap(propertyValue);
    }

    @Override
    public Boolean modifyPropertyByPercentage(@NotNull Integer propertyKind, @NotNull Float percentage) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).modifyByPercentage(percentage);
    }

    @Override
    public Boolean modifyPropertyByPercentage(@NotNull Integer propertyKind, @NotNull Integer propertyIndex,
                                              @NotNull Float percentage) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).modifyByPercentage(propertyIndex, percentage);
    }

    @Override
    public void modifyPropertyByAddition(@NotNull Integer propertyKind, @NotNull Integer toAdd) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.get(propertyKind).modifyByAddition(toAdd);
    }

    @Override
    public void modifyPropertyByAddition(@NotNull Integer propertyKind, @NotNull Integer propertyIndex,
                                         @NotNull Integer toAdd) {
        if (!hasProperty(propertyKind)) {
            return;
        }
        properties.get(propertyKind).modifyByAddition(propertyIndex, toAdd);
    }

    @Override
    public Boolean isAlive() {
        return getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.CURRENT_VALUE_INDEX) > 0;
    }

    @Override
    public void affectHitpoints(@NotNull Integer amount) {
        if (!isAlive()) {
            return;
        }
        if (amount > 0) {
            final Integer missingHitpoints = getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.MAX_VALUE_INDEX)
                    - getProperty(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.CURRENT_VALUE_INDEX);
            if (missingHitpoints < amount) {
                modifyPropertyByAddition(PropertyCategories.PC_HITPOINTS,
                        DigitsPairIndices.CURRENT_VALUE_INDEX, missingHitpoints);
            } else {
                modifyPropertyByAddition(PropertyCategories.PC_HITPOINTS,
                        DigitsPairIndices.CURRENT_VALUE_INDEX, amount);
            }
        } else {
            modifyPropertyByAddition(PropertyCategories.PC_HITPOINTS, DigitsPairIndices.CURRENT_VALUE_INDEX,
                    Math.round(amount.floatValue() * (Constants.PERCENTAGE_CAP_FLOAT - getDamageReduction())));
        }
    }

    @Override
    public Integer getCash() {
        if (hasProperty(PropertyCategories.PC_CASH_AMOUNT)) {
            return getProperty(PropertyCategories.PC_CASH_AMOUNT);
        }
        return 0;
    }

    @Override
    public void addEffect(@NotNull Effect effect) {
        effects.add(effect);
    }

    @Override
    public Boolean removeEffect(Integer effectIndex) {
        if (effectIndex < 0 || effectIndex >= effects.size()) {
            return false;
        }
        effects.remove(effectIndex.intValue());
        return true;
    }

    @Override
    public Boolean removeAllEffects() {
        if (effects.isEmpty()) {
            return false;
        }
        effects.clear();
        return true;
    }

    @Override
    public @Nullable Bag getBag(@NotNull Integer bagIndex) {
        if (bagIndex < 0 || bagIndex > bags.size()) {
            return null;
        }
        return bags.get(bagIndex);
    }

    @Override
    public CharacterRole getCharacterRole() {
        return characterRole;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        tickEffects();
        reduceCooldowns();
    }

    @Override
    public Integer getInitiative() {
        return getProperty(PropertyCategories.PC_INITIATIVE);
    }

    @Override
    public Integer getSpeed() {
        return getProperty(PropertyCategories.PC_SPEED);
    }

    private void tickEffects() {
        for (Effect effect : effects) {
            if (effect.isExpired()) {
                effects.remove(effect);
            }
        }
        for (Effect effect : effects) {
            Integer hitpointsAffection = effect.getAffection(AffectorCategories.AC_OVER_TIME_AFFECTOR);
            if (hitpointsAffection == Integer.MIN_VALUE) {
                hitpointsAffection = 0;
            }
            if (hitpointsAffection != 0) {
                affectHitpoints(hitpointsAffection);
            }
            effect.tick();
        }
    }

    private void reduceCooldowns() {
        if (!hasProperty(PropertyCategories.PC_ABILITIES_COOLDOWN)) {
            return;
        }
        modifyPropertyByAddition(PropertyCategories.PC_ABILITIES_COOLDOWN, -1);
    }

    List<Effect> getEffects() {
        return effects;
    }

    private Float getDamageReduction() {
        final Float damageReduction = ((Constants.MINIMAL_DAMAGE_REDUCTION * Constants.PERCENTAGE_CAP_INT
                * getDefense().floatValue()) / getProperty(PropertyCategories.PC_BASE_DEFENSE))
                * Constants.ONE_PERCENT_FLOAT;
        if (damageReduction > Constants.PERCENTAGE_CAP_FLOAT) {
            return Constants.PERCENTAGE_CAP_FLOAT;
        }
        if (damageReduction < Constants.MINIMAL_DAMAGE_REDUCTION) {
            return Constants.MINIMAL_DAMAGE_REDUCTION;
        }
        return damageReduction;
    }

    Float intToFloatPercentage(@NotNull Integer value) {
        return value.floatValue() / Integer.valueOf(Constants.PERCENTAGE_CAP_INT).floatValue();
    }

    @SuppressWarnings("SameParameterValue")
    Integer getEffectsAffection(@NotNull Integer affectorKind) {
        Integer effectsAffection = 0;
        for (Effect effect : effects) {
            if (!effect.isExpired()) {
                final Integer effectBonus = effect.getAffection(affectorKind);
                if (effectBonus != Integer.MIN_VALUE) {
                    effectsAffection += effectBonus;
                }
            }
        }
        return effectsAffection;
    }

    Integer getEffectsAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        Integer effectsAffection = 0;
        for (Effect effect : effects) {
            if (!effect.isExpired()) {
                final Integer effectBonus = effect.getAffection(affectorKind, affectionIndex);
                if (effectBonus != Integer.MIN_VALUE) {
                    effectsAffection += effectBonus;
                }
            }
        }
        return effectsAffection;
    }
}
