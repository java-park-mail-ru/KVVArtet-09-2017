package gamemechanics.battlefield.map.tilesets;

import java.util.List;

public interface FieldOfVision {
    void refresh(List<Integer> newPosition);
    Boolean isVisible(List<Integer> position);
}
