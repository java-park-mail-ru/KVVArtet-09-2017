package project.gamemechanics.battlefield.map.helpers;

import java.util.List;

public interface PathfindingAlgorithm {
    Route getPath(List<Integer> sourceCoordinates, List<Integer> goalCoordinates);
}
