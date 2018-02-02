package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class InstanceNameDescription extends AbstractModel {
    public InstanceNameDescription(@JsonProperty("modelID") @NotNull Integer modelID,
                                   @JsonProperty("name") @NotNull String name,
                                   @JsonProperty("description") @NotNull String description) {
        super(modelID, name, description);
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Property> getAllProperties() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, Affector> getAllAffectors() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable List<GameResource> getAllInlaid() {
        return null;
    }

    @Override
    @JsonIgnore
    public @Nullable Map<Integer, List<Integer>> getAllMappings() {
        return null;
    }
}
