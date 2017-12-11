package project.gamemechanics.flyweights;

import com.fasterxml.jackson.annotation.JsonIgnore;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Perk;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public class CharacterClass implements CharacterRole {
    private final Integer characterClassID;

    private final String name;
    private final String description;

    private final Map<Integer, Ability> abilities;
    private final Map<Integer, PerkBranch> branches;

    private final Map<Integer, Property> properties;

    public static class CharacterClassModel {
        public final Integer id;
        public final String name;
        public final String description;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Ability> abilities;
        @SuppressWarnings("PublicField")
        public final Map<Integer, PerkBranch> branches;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Property> properties;

        public CharacterClassModel(@NotNull Integer id, @NotNull String name,
                                   @NotNull String description, @NotNull Map<Integer, Ability> abilities,
                                   @NotNull Map<Integer, PerkBranch> branches,
                                   @NotNull Map<Integer, Property> properties) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.abilities = abilities;
            this.branches = branches;
            this.properties = properties;
        }
    }

    public CharacterClass(@NotNull CharacterClassModel model) {
        characterClassID = model.id;
        name = model.name;
        description = model.description;
        abilities = model.abilities;
        branches = model.branches;
        properties = model.properties;
    }

    @Override
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public Integer getID() {
        return characterClassID;
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
    public Ability getAbility(@NotNull Integer abilityID) {
        return abilities.getOrDefault(abilityID, null);
    }

    @Override
    public Map<Integer, Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public PerkBranch getBranch(@NotNull Integer branchID) {
        return branches.get(branchID);
    }

    @Override
    public Perk getPerk(@NotNull Integer branchID, @NotNull Integer perkID) {
        return branches.get(branchID).getPerk(perkID);
    }

    @Override
    public Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    public Integer getProperty(@NotNull Integer propertyKind) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property == null) {
            return Constants.WRONG_INDEX;
        }
        return property.getProperty();
    }

    @Override
    public Boolean canEquip(@NotNull Integer equipmentKindId) {
        final Set<Integer> availableEquipmentIds = getEquipableKinds();
        return availableEquipmentIds != null && availableEquipmentIds.contains(equipmentKindId);
    }

    @Override
    @JsonIgnore
    public @Nullable Set<Integer> getEquipableKinds() {
        final Property availableEquipmentProperty = properties.getOrDefault(
                PropertyCategories.PC_AVAILABLE_EQUIPMENT, null);
        if (availableEquipmentProperty == null) {
            return null;
        }
        return availableEquipmentProperty.getPropertySet();
    }
}
