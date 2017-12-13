package project.gamemechanics.battlefield.map.helpers;

import java.util.List;

public interface Route {
    Integer getLength();

    Integer getTravelCost();

    void walkThrough(Integer distance);

    void walkThrough();

    List<Integer> getStartCoordinates();

    List<Integer> getGoalCoordinates(Integer distance);

    List<Integer> getGoalCoordinates();
}
