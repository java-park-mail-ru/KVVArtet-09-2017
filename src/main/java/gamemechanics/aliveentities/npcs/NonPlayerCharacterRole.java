package gamemechanics.aliveentities.npcs;

import gamemechanics.components.affectors.Affector;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.Action;
import gamemechanics.interfaces.CharacterRole;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NonPlayerCharacterRole implements CharacterRole {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer npcRoleID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    // affectors modifying NPC's stats, defense and damage
    private final Map<Integer, Affector> affectors;

    // abilities list for that role;
    private final Map<Integer, Ability> abilities;

    public static class NPCRoleModel {
        public String name;
        public String description;
        public Map<Integer, Affector> affectors;
        public Map<Integer, Ability> abilities;

        public NPCRoleModel(String name, String description, Map<Integer, Affector> affectors,
                            Map<Integer, Ability> abilities) {
            this.name = name;
            this.description = description;
            this.affectors = affectors;
            this.abilities = abilities;
        }
    }

    public NonPlayerCharacterRole(NPCRoleModel model) {
        name = model.name;
        description = model.description;
        affectors = model.affectors;
        abilities = model.abilities;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
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
    public Action makeDecision() {
        return null;
    }
}
