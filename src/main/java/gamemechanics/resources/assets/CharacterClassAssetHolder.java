package gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.flyweights.CharacterClass;
import gamemechanics.flyweights.PerkBranch;
import gamemechanics.globals.MappingIndices;
import gamemechanics.interfaces.Ability;
import gamemechanics.interfaces.CharacterRole;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterClassAssetHolder extends AbstractAssetHolder<CharacterRole>
        implements AssetHolder.CharacterClassHolder {
    public CharacterClassAssetHolder(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities,
                                     @NotNull Map<Integer, PerkBranch> perkBranches) {
        super();
        fillFromFile(fileName, abilities, perkBranches);
    }

    private void fillFromFile(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities,
                              @NotNull Map<Integer, PerkBranch> perkBranches) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            ResourceHolder holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            Map<Integer, GameResource> characterClassResources = holder.getAllResources();
            for (Integer classId : characterClassResources.keySet()) {
                GameResource characterClassResource = characterClassResources.get(classId);

                Map<Integer, Ability> classAbilities = new HashMap<>();
                List<Integer> resourceAbilities =
                        characterClassResource.getMapping(MappingIndices.ABILITIES_MAPPING);
                for (Integer abilityId : resourceAbilities) {
                    Ability ability = abilities.getOrDefault(abilityId, null);
                    if (ability != null) {
                        classAbilities.put(ability.getID(), ability);
                    }
                }

                Map<Integer, PerkBranch> classBranches = new HashMap<>();
                List<Integer> resourceBranches =
                        characterClassResource.getMapping(MappingIndices.PERK_BRANCHES_MAPPING);
                for (Integer branchId : resourceBranches) {
                    PerkBranch branch = perkBranches.getOrDefault(branchId, null);
                    if (branch != null) {
                        classBranches.put(branch.getID(), branch);
                    }
                }

                CharacterClass.CharacterClassModel model =
                        new CharacterClass.CharacterClassModel(characterClassResource.getID(),
                                characterClassResource.getName(), characterClassResource.getDescription(),
                                classAbilities, classBranches);
                assets.put(model.id, new CharacterClass(model));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
