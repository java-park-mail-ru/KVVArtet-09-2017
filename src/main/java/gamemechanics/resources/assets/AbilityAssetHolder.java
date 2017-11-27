package gamemechanics.resources.assets;

import com.fasterxml.jackson.databind.ObjectMapper;
import gamemechanics.effects.IngameEffect;
import gamemechanics.flyweights.abilities.AbilityBehaviors;
import gamemechanics.flyweights.abilities.IngameAbility;
import gamemechanics.globals.MappingIndices;
import gamemechanics.interfaces.Ability;
import gamemechanics.resources.holders.GameResourceHolder;
import gamemechanics.resources.holders.ResourceHolder;
import gamemechanics.resources.models.GameResource;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbilityAssetHolder extends AbstractAssetHolder<Ability> implements AssetHolder.AbilityHolder {
    public AbilityAssetHolder(@NotNull String fileName) {
        super();
        fillFromFile(fileName);
    }

    private void fillFromFile(@NotNull String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        ResourceHolder holder = null;
        try {
            holder = mapper.readValue(new File(fileName), GameResourceHolder.class);
            Map<Integer, GameResource> abilityResources = holder.getAllResources();

            for (Integer abilityID : abilityResources.keySet()) {
                GameResource abilityResource = abilityResources.get(abilityID);

                List<GameResource> effectResources = abilityResource.getAllInlaid();
                List<IngameEffect.EffectModel> effectModels = new ArrayList<>();
                for (GameResource effectResource : effectResources) {
                    effectModels.add(new IngameEffect.EffectModel(effectResource.getName(),
                            effectResource.getDescription(), effectResource.getID(),
                            effectResource.getAllAffectors()));
                }

                IngameAbility.AbilityModel abilityModel = new IngameAbility.AbilityModel(abilityID,
                        abilityResource.getName(), abilityResource.getDescription(),
                        abilityResource.getAllProperties(), abilityResource.getAllAffectors(),
                        effectModels, AbilityBehaviors.getBehavior(
                                abilityResource.getMapping(MappingIndices.ABILITY_BEHAVIOR_MAPPING).get(0)));
                assets.put(abilityModel.id, new IngameAbility(abilityModel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
