package gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import gamemechanics.components.affectors.Affector;
import gamemechanics.components.properties.Property;
import gamemechanics.interfaces.AffectorProvider;
import gamemechanics.interfaces.GameEntity;
import gamemechanics.interfaces.PropertyProvider;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(ItemPartAsset.class),
})
public interface ItemPart extends GameEntity, AffectorProvider, PropertyProvider {
    int FIRST_PART_ID = 0;
    int SECOND_PART_ID = 1;
    int THIRD_PART_ID = 2;
    int ITEM_PARTS_COUNT = 3;

    @JsonIgnore
    Integer getLevel();

    Integer getPartIndex();

    Map<Integer, Affector> getAllAffectors();

    Map<Integer, Property> getAllProperties();
}
