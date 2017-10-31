package gamemechanics.aliveentities;

import gamemechanics.components.properties.Property;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.flyweights.CharacterClass;
import gamemechanics.flyweights.CharacterRace;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.Bag;
import gamemechanics.interfaces.DecisionMaker;
import gamemechanics.interfaces.Effect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractAliveEntity implements AliveEntity {
    private final String name;
    private final String description;

    private final Map<Integer, Property> properties;
    private final List<Bag> bags;
    private final List<Effect> effects = new ArrayList<>();

    private static class AbstractAliveEntityModel {
        public String name;
        public String description;
        public Map<Integer, Property> properties;
        public List<Bag> bags; // for monsters bags will contain generated loot

        private AbstractAliveEntityModel(String name, String description, Map<Integer, Property> properties, List<Bag> bags) {
            this.name = name;
            this.description = description;
            this.properties = properties;
            this.bags = bags;
        }
    }

    public static class NPCModel extends AbstractAliveEntityModel {
        public List<DecisionMaker> phases;

        public NPCModel(String name, String description, Map<Integer, Property> properties,
                        List<Bag> bags, List<DecisionMaker> phases) {
            super(name, description, properties, bags);
            this.phases = phases;
        }
    }

    public static class UserCharacterModel extends AbstractAliveEntityModel {
        public CharacterClass characterClass;
        public CharacterRace characterRace;

        public UserCharacterModel(String name, String description, Map<Integer, Property> properties,
                                   List<Bag> bags, CharacterClass characterClass, CharacterRace characterRace) {
            super(name, description, properties, bags);
            this.characterClass = characterClass;
            this.characterRace = characterRace;
        }
    }

    public AbstractAliveEntity(NPCModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        bags = model.bags;
    }

    public AbstractAliveEntity(UserCharacterModel model) {
        name = model.name;
        description = model.description;
        properties = model.properties;
        bags = model.bags;
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
    public Set<Integer> getAvailableProperties(){
        return properties.keySet();
    }

    @Override
    public Integer getProperty(Integer propertyKind, Integer propertyIndex) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty(propertyIndex);
    }

    @Override
    public Integer getProperty(Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return Integer.MIN_VALUE;
        }
        return properties.get(propertyKind).getProperty();
    }

    @Override
    public Boolean addProperty(Integer propertyKind, Property property) {
        if (hasProperty(propertyKind)) {
            return false;
        }
        properties.put(propertyKind, property);
        return true;
    }

    @Override
    public Boolean removeProperty(Integer propertyKind) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        properties.remove(propertyKind);
        return true;
    }

    @Override
    public Boolean setProperty(Integer propertyKind, Integer propertyValue) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).setSingleProperty(propertyValue);
    }

    @Override
    public Boolean setProperty(Integer propertyKind, Integer propertyIndex, Integer propertyValue) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).setSingleProperty(propertyIndex, propertyValue);
    }

    @Override
    public Boolean setProperty(Integer propertyKind, List<Integer> propertyValue) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).setPropertyList(propertyValue);
    }

    @Override
    public Boolean modifyPropertyByPercentage(Integer propertyKind, Float percentage) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).modifyByPercentage(percentage);
    }

    @Override
    public Boolean modifyPropertyByAddition(Integer propertyKind, Integer toAdd) {
        if (!hasProperty(propertyKind)) {
            return false;
        }
        return properties.get(propertyKind).modifyByAddition(toAdd);
    }

    @Override
    public Boolean addEffect(Effect effect) {
        if (effect == null) {
            return false;
        }
        effects.add(effect);
        return true;
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

    private void tickEffects() {
        for (Effect effect : effects) {
            Integer hitpointsAffection = effect.tick();
            if (hitpointsAffection != 0) {
                affectHitpoints(hitpointsAffection);
            }
        }
        for (Effect effect : effects) {
            if (effect.isExpired()) {
                effects.remove(effect);
            }
        }
    }

    private void reduceCooldowns() {
        if (!hasProperty(PropertyCategories.PC_ABILITIES_COOLDOWN)) {
            return;
        }
        modifyPropertyByAddition(PropertyCategories.PC_ABILITIES_COOLDOWN, -1);
    }
}
