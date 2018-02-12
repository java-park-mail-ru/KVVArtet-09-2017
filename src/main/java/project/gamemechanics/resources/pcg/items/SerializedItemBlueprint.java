package project.gamemechanics.resources.pcg.items;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Map;

public final class SerializedItemBlueprint {
    private Integer itemId;
    private Integer ownerID;
    private final Integer level;
    private final Map<Integer, Integer> itemPartsIdToRarityMap;

    public SerializedItemBlueprint(@JsonProperty @NotNull Integer itemId, @JsonProperty @NotNull Integer ownerID,
                                   @JsonProperty @NotNull Integer level,
                                   @JsonProperty @NotNull Map<Integer, Integer> itemPartsIdToRarityMap) {
        this.itemId = itemId;
        this.ownerID = ownerID;
        this.level = level;
        this.itemPartsIdToRarityMap = itemPartsIdToRarityMap;
    }

    public @NotNull Integer getItem_id() {
        return itemId;
    }

    public @NotNull Integer getOwnerID() {
        return ownerID;
    }

    public @NotNull Integer getLevel() {
        return level;
    }

    public @NotNull Map<Integer, Integer> getItemPartsIdToRarityMap() {
        return itemPartsIdToRarityMap;
    }
}
