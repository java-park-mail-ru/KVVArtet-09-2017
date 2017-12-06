package gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonProperty;
import gamemechanics.aliveentities.AbstractAliveEntity;
import gamemechanics.aliveentities.npcs.NonPlayerCharacter;
import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.CharacterRole;
import gamemechanics.resources.assets.AssetProvider;

import javax.validation.constraints.NotNull;
import java.util.*;

public class NpcsFactoryImpl implements NpcsFactory {
    private final Map<Integer, Map<Integer, NpcPart>> npcParts;

    public NpcsFactoryImpl(@JsonProperty("npcParts") @NotNull Map<Integer, Map<Integer, NpcPart>> npcParts) {
        this.npcParts = npcParts;
    }

    @Override
    public AliveEntity makeNpc(@NotNull NpcBlueprint blueprint, @NotNull AssetProvider assetProvider) {
        final Map<Integer, Integer> parts = new HashMap<>(blueprint.getNpcParts());
        final Random random = new Random(System.currentTimeMillis());
        for (Integer partIndex : parts.keySet()) {
            final List<Integer> keysList = new ArrayList<>(npcParts.get(partIndex).keySet());
            if (parts.get(partIndex) == Constants.UNDEFINED_ID) {
                parts.replace(partIndex, keysList.get(random.nextInt(keysList.size())));
            }
        }

        final CharacterRole npcRole;
        if (blueprint.getAvailableProperties().contains(PropertyCategories.PC_CHARACTER_ROLE_ID)) {
            if (blueprint.getProperties().get(PropertyCategories.PC_CHARACTER_ROLE_ID).getProperty()
                    != Constants.UNDEFINED_ID) {
                npcRole = assetProvider.getNpcRole(blueprint.getProperties()
                        .get(PropertyCategories.PC_CHARACTER_ROLE_ID).getProperty());
            } else {
                npcRole = assetProvider.getNpcRole();
            }
        } else {
            npcRole = assetProvider.getNpcRole();
        }

        List<NpcPart> npcPartsList = getNpcParts(parts);

        final StringBuilder nameBuilder = new StringBuilder();
        for (NpcPart part : npcPartsList) {
            nameBuilder.append(part.getName());
            nameBuilder.append(' ');
        }
        nameBuilder.deleteCharAt(nameBuilder.length() - 1);

        final StringBuilder descriptionBuilder = new StringBuilder();
        for (NpcPart part : npcPartsList) {
            descriptionBuilder.append(part.getDescription());
            descriptionBuilder.append(' ');
        }
        descriptionBuilder.deleteCharAt(descriptionBuilder.length() - 1);

        final AbstractAliveEntity.NPCModel model = new AbstractAliveEntity.NPCModel(nameBuilder.toString(),
                descriptionBuilder.toString(), new HashMap<>(), new ArrayList<>(), npcRole);

        return new NonPlayerCharacter(model);
    }

    private List<NpcPart> getNpcParts(@NotNull Map<Integer, Integer> npcPartsIndices) {
        final List<NpcPart> partsList = new ArrayList<>();
        for (Integer partId : npcPartsIndices.keySet()) {
            final Integer partIndex = npcPartsIndices.get(partId);
            partsList.add(npcParts.get(partId).get(partIndex));
        }
        return partsList;
    }
}
