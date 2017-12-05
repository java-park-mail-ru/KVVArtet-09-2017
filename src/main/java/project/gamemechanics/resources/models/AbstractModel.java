package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public abstract class AbstractModel implements GameResource {
    private final Integer modelID;

    private final String name;
    private final String description;

    public AbstractModel(@JsonProperty("modelId") @NotNull Integer modelID,
                         @JsonProperty("name") @NotNull String name,
                         @JsonProperty("description") @NotNull String description) {
        this.modelID = modelID;
        this.name = name;
        this.description = description;
    }

    @Override
    @JsonIgnore
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
    @JsonProperty("modelId")
    public Integer getID() {
        return modelID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
