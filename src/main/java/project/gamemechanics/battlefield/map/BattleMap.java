package gamemechanics.battlefield.map;

import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.MapNode;

import java.util.ArrayList;
import java.util.List;

public class BattleMap {
    private final List<List<MapNode>> rows;

    public BattleMap(List<List<MapNode>> rows) {
        this.rows = rows;
    }

    public List<MapNode> getRow(Integer rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        return null;
    }

    public MapNode getTile(Integer rowIndex, Integer tileIndex) {
        final List<MapNode> row = getRow(rowIndex);
        if (row == null || tileIndex < 0) {
            return null;
        }
        if (tileIndex < row.size()) {
            return row.get(tileIndex);
        }
        return null;
    }

    public AliveEntity getInhabitant(Integer rowIndex, Integer tileIndex) {
        final MapNode tile = getTile(rowIndex, tileIndex);
        if (tile == null) {
            return null;
        }
        return tile.getInhabitant();
    }

    public List<Integer> getSize() {
        final List<Integer> size = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        size.set(DigitsPairIndices.ROW_COORD_INDEX, rows.size());
        size.set(DigitsPairIndices.COL_COORD_INDEX, rows.get(0).size()); // we're assuming that all rows in the list have the same length
        return size;
    }
}
