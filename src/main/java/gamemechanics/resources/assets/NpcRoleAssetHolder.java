package gamemechanics.resources.assets;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.aliveentities.npcs.NonPlayerCharacterRole;
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

public class NpcRoleAssetHolder extends AbstractAssetHolder<CharacterRole> implements AssetHolder.NpcRoleHolder {
    public NpcRoleAssetHolder(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities) {
        super();
        fillFromFile(fileName, abilities);
    }

    private void fillFromFile(@NotNull String fileName, @NotNull Map<Integer, Ability> abilities) {
        final ObjectMapper mapper = new ObjectMapper();
        //noinspection TryWithIdenticalCatches
        try {
            final ResourceHolder holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            final Map<Integer, GameResource> npcRoleResources = holder.getAllResources();
            for (Integer roleId : npcRoleResources.keySet()) {
                final GameResource npcRoleResource = npcRoleResources.get(roleId);

                final List<Integer> resourceBehaviorIds =
                        npcRoleResource.getMapping(MappingIndices.NPC_ROLE_BEHAVIOR_MAPPING);

                final List<Integer> resourceAbilityIds =
                        npcRoleResource.getMapping(MappingIndices.NPC_ROLE_ABILITY_MAPPING);
                final Map<Integer, Ability> roleAbilities = new HashMap<>();
                for (Integer abilityId : resourceAbilityIds) {
                    final Ability ability = abilities.getOrDefault(abilityId, null);
                    if (ability != null) {
                        roleAbilities.put(ability.getID(), ability);
                    }
                }

                final NonPlayerCharacterRole.NPCRoleModel model = new NonPlayerCharacterRole.NPCRoleModel(roleId,
                        npcRoleResource.getName(), npcRoleResource.getDescription(), npcRoleResource.getAllAffectors(),
                        resourceBehaviorIds, roleAbilities);
                assets.put(model.id, new NonPlayerCharacterRole(model));
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
