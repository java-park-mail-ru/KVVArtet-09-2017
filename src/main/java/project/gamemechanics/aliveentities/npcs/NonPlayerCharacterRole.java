package project.gamemechanics.aliveentities.npcs;

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
        public final Integer id;
        public final String name;
        public final String description;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Affector> affectors;
        @SuppressWarnings("PublicField")
        public final List<Integer> behaviorIds;
        @SuppressWarnings("PublicField")
        public final Map<Integer, Ability> abilities;

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
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    public Integer getID() {
        return npcRoleID;
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
    public Ability getAbility(Integer abilityID) {
        return abilities.getOrDefault(abilityID, null);
    }

    @Override
    public Map<Integer, Ability> getAllAbilities() {
        return abilities;
    }

    @Override
    public Boolean hasAffector(Integer affectorKind) {
        return affectors.containsKey(affectorKind);
    }

    @Override
    public Set<Integer> getAvailableAffectors() {
        return affectors.keySet();
    }

    @Override
    public Integer getAffection(Integer affectorKind) {
        if (!hasAffector(affectorKind)) {
            return 0;
        }
        return affectors.get(affectorKind).getAffection();
    }

    @Override
    public Integer getAffection(Integer affectorKind, Integer affectionIndex) {
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
