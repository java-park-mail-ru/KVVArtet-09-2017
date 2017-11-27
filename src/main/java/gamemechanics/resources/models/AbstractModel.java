package gamemechanics.resources.models;

import javax.validation.constraints.NotNull;

public abstract class AbstractModel implements GameResource {
    private final Integer modelID;

    private final String name;
    private final String description;

    public AbstractModel(@NotNull Integer modelID, @NotNull String name, @NotNull String description) {
        this.modelID = modelID;
        this.name = name;
        this.description = description;
    }

    @Override
    public Integer getInstancesCount() {
        return 0;
    }

    @Override
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
