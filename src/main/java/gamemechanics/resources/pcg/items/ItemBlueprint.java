package gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public final class ItemBlueprint {
    private final Map<Integer, Property> properties;
    private final Map<Integer, Integer> itemParts;

    public ItemBlueprint(@JsonProperty("properties") @NotNull Map<Integer, Property> properties,
                         @JsonProperty("itemParts") @NotNull Map<Integer, Integer> itemParts) {
        this.properties = properties;
        this.itemParts = itemParts;
    }

    public Map<Integer, Property> getProperties() {
        return properties;
    }

    @JsonIgnore
    public Set<Integer> getAvailableProperties() {
        return properties.keySet();
    }

    public Map<Integer, Integer> getItemParts() {
        return itemParts;
    }

    @JsonIgnore
    public Set<Integer> getAvailableItemParts() {
        return itemParts.keySet();
    }
}
