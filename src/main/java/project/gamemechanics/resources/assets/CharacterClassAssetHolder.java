package project.gamemechanics.resources.assets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import project.gamemechanics.flyweights.CharacterClass;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.globals.MappingIndices;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.CharacterRole;
import project.gamemechanics.resources.holders.GameResourceHolder;
import project.gamemechanics.resources.holders.ResourceHolder;
import project.gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CharacterClassAssetHolder extends AbstractAssetHolder<CharacterRole>
        implements AssetHolder.CharacterClassHolder {
    public CharacterClassAssetHolder(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities,
                                     @NotNull Map<Integer, PerkBranch> perkBranches) {
        super();
        fillFromFile(fileName, abilities, perkBranches);
    }

    private void fillFromFile(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities,
                              @NotNull Map<Integer, PerkBranch> perkBranches) {
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection TryWithIdenticalCatches
        try {
            final ResourceHolder holder = mapper.readValue(Resources.getResource(fileName), GameResourceHolder.class);
            final Map<Integer, GameResource> characterClassResources = holder.getAllResources();
            for (Integer classId : characterClassResources.keySet()) {
                final GameResource characterClassResource = characterClassResources.get(classId);

                final Map<Integer, Ability> classAbilities = new HashMap<>();
                final List<Integer> resourceAbilities =
                        characterClassResource.getMapping(MappingIndices.ABILITIES_MAPPING);
                for (Integer abilityId : Objects.requireNonNull(resourceAbilities)) {
                    final Ability ability = abilities.getOrDefault(abilityId, null);
                    if (ability != null) {
                        classAbilities.put(ability.getID(), ability);
                    }
                }

                final Map<Integer, PerkBranch> classBranches = new HashMap<>();
                final List<Integer> resourceBranches =
                        characterClassResource.getMapping(MappingIndices.PERK_BRANCHES_MAPPING);
                for (Integer branchId : Objects.requireNonNull(resourceBranches)) {
                    final PerkBranch branch = perkBranches.getOrDefault(branchId, null);
                    if (branch != null) {
                        classBranches.put(branch.getID(), branch);
                    }
                }

                final CharacterClass.CharacterClassModel model =
                        new CharacterClass.CharacterClassModel(characterClassResource.getID(),
                                characterClassResource.getName(), characterClassResource.getDescription(),
                                classAbilities, classBranches,
                                Objects.requireNonNull(characterClassResource.getAllProperties()));
                assets.put(model.id, new CharacterClass(model));
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
