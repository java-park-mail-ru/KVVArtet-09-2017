package project.gamemechanics.aliveentities.npcs;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NonPlayerCharacterRole implements CharacterRole {
    private final Integer npcRoleID;

    private final String name;
    private final String description;

    // affectors modifying NPC's stats, defense and damage
    private final Map<Integer, Affector> affectors;
    private final List<Integer> behaviorIds;

    // abilities list for that role;
    private final Map<Integer, Ability> abilities;

    public static class NPCRoleModel {
        // CHECKSTYLE:OFF
        public final Integer id;
        final String name;
        final String description;
        final Map<Integer, Affector> affectors;
        final List<Integer> behaviorIds;
        final Map<Integer, Ability> abilities;
        // CHECKSTYLE:ON

        public NPCRoleModel(@NotNull Integer id,
                            @NotNull String name, @NotNull String description,
                            @NotNull Map<Integer, Affector> affectors,
                            @NotNull List<Integer> behaviorIds,
                            @NotNull Map<Integer, Ability> abilities) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.affectors = affectors;
            this.behaviorIds = behaviorIds;
            this.abilities = abilities;
        }
    }

    public NonPlayerCharacterRole(@NotNull NPCRoleModel model) {
        npcRoleID = model.id;
        name = model.name;
        description = model.description;
        affectors = model.affectors;
        behaviorIds = model.behaviorIds;
        abilities = model.abilities;
    }

    @Override
    public @NotNull Integer getInstancesCount() {
        return 0;
    }

    @Override
    public @NotNull Integer getID() {
        return npcRoleID;
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
    public @Nullable Ability getAbility(@NotNull Integer abilityID) {
        return abilities.getOrDefault(abilityID, null);
    }

    @Override
    public @NotNull Map<Integer, Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public @NotNull Boolean hasAffector(@NotNull Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public @NotNull Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return 0;
        }
        return affectors.get(affectorKind).getAffection();
    }

    @Override
    public @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex) {
        if (!hasAffector(affectorKind)) {
            return 0;
        }
        return affectors.get(affectorKind).getAffection(affectionIndex);
    }

    @Override
    public List<Integer> getBehaviorIds() {
        return behaviorIds;
    }
}
