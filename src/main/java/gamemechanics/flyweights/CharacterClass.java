package gamemechanics.flyweights;

import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.GameEntity;
import gamemechanics.interfaces.Perk;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CharacterClass implements GameEntity {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer characterClassID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final HashMap<Integer, Ability> abilities;
    private final HashMap<Integer, PerkBranch> branches;

    public static class CharacterClassModel {
        public String name;
        public String description;
        public HashMap<Integer, Ability> abilities;
        public HashMap<Integer, PerkBranch> branches;

        public CharacterClassModel(String name, String description,
                                   HashMap<Integer, Ability> abilities, HashMap<Integer, PerkBranch> branches) {
            this.name = name;
            this.description = description;
            this.abilities = abilities;
            this.branches = branches;
        }
    }

    public CharacterClass(CharacterClassModel model) {
        name = model.name;
        description = model.description;
        abilities = model.abilities;
        branches = model.branches;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
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

    public Ability getAbility(Integer abilityID) {
        return abilities.get(abilityID);
    }

    public HashMap<Integer, Ability> getAllAbilitities() {
        return abilities;
    }

    public PerkBranch getBranch(Integer branchID) {
        return branches.get(branchID);
    }

    public Perk getPerk(Integer branchID, Integer perkID) {
        return branches.get(branchID).getPerk(perkID);
    }
}
