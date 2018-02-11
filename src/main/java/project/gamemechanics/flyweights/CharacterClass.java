package project.gamemechanics.flyweights;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.interfaces.Perk;

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
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final Map<Integer, Ability> abilities;
        final Map<Integer, PerkBranch> branches;
        final Map<Integer, Property> properties;
        // CHECKSTYLE:ON

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
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull Integer getID() {
        return characterClassID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    public @NotNull Ability getAbility(@NotNull Integer abilityID) {
        return abilities.getOrDefault(abilityID, null);
    }

    @Override
    public @NotNull Map<Integer, Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public @Nullable PerkBranch getBranch(@NotNull Integer branchID) {
        return branches.get(branchID);
    }

    @Override
    public @Nullable Perk getPerk(@NotNull Integer branchID, @NotNull Integer perkID) {
        return branches.get(branchID).getPerk(perkID);
    }

    @Override
    public @NotNull Boolean hasProperty(@NotNull Integer propertyKind) {
        return properties.containsKey(propertyKind);
    }

    @Override
    public @NotNull Integer getProperty(@NotNull Integer propertyKind) {
        final Property property = properties.getOrDefault(propertyKind, null);
        if (property == null) {
            return Constants.WRONG_INDEX;
        }
        return property.getProperty();
    }

    @Override
    public @NotNull Boolean canEquip(@NotNull Integer equipmentKindId) {
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

    @Override
    @JsonIgnore
    public @Nullable Set<Integer> getPerkBranchesIds() {
        return branches.keySet();
    }

    @Override
    public @Nullable Set<Integer> getBranchPerksIds(@NotNull Integer branchId) {
        return branches.containsKey(branchId) ? branches.get(branchId).getPerkIds() : null;
    }

    @Override
    @JsonIgnore
    public @Nullable Set<Integer> getAvailableRoles() {
        return properties.get(PropertyCategories.PC_AVAILABLE_ROLES).getPropertySet();
    }
}
