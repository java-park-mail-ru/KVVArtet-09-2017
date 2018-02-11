package project.gamemechanics.resources.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import project.gamemechanics.components.properties.Property;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class NewCharacterModel {
    private final Map<Integer, Property> properties;

    public NewCharacterModel(@JsonProperty("properties") @NotNull Map<Integer, Property> properties) {
        this.properties = properties;
    }

    public @NotNull Map<Integer, Property> getProperties() {
        return properties;
    }
}
