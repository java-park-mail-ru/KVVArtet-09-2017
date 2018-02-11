package project.gamemechanics.resources.pcg.npcs;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.interfaces.Levelable;
import project.gamemechanics.interfaces.PropertyProvider;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(NpcPartAsset.class),
})
public interface NpcPart extends Levelable, PropertyProvider {
    int PREFIX_PART_ID = 0;
    int BODY_PART_ID = 1;
    int NPC_PARTS_COUNT = 2;

    @NotNull Integer getPartIndex();

    @NotNull Map<Integer, Property> getAllProperties();

    @NotNull List<ItemBlueprint> getLootList();
}
