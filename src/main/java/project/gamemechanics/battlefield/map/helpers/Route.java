package project.gamemechanics.battlefield.map.helpers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.NotNull;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(BattleMapRoute.class),
})
public interface Route {
    @JsonIgnore
    Integer getLength();

    @JsonIgnore
    Integer getTravelCost();

    void walkThrough(@NotNull Integer distance);

    void walkThrough();

    @JsonIgnore
    List<Integer> getStartCoordinates();

    List<Integer> getGoalCoordinates(@NotNull Integer distance);

    @JsonIgnore
    List<Integer> getGoalCoordinates();
}
