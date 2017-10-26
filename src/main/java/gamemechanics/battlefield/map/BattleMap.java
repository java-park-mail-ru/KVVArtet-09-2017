package gamemechanics.battlefield.map;

import gamemechanics.battlefield.Tile;
import gamemechanics.globals.DigitsPairIndices;
import gamemechanics.interfaces.AliveEntity;

import java.util.ArrayList;
import java.util.List;

public class BattleMap {
    private final List<List<Tile>> rows;

    public BattleMap(List<List<Tile>> rows) {
        this.rows = rows;
    }

    public List<Tile> getRow(Integer rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        return null;
    }

    public Tile getTile(Integer rowIndex, Integer tileIndex) {
        List<Tile> row = getRow(rowIndex);
        if (row == null || tileIndex < 0) {
            return null;
        }
        if (tileIndex < row.size()) {
            return row.get(tileIndex);
        }
        return null;
    }

    public AliveEntity getInhabitant(Integer rowIndex, Integer tileIndex) {
        Tile tile = getTile(rowIndex, tileIndex);
        if (tile == null) {
            return null;
        }
        return tile.getInhabitant();
    }

    public List<Integer> getSize() {
        List<Integer> size = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        size.set(DigitsPairIndices.X_COORD_INDEX, rows.size());
        size.set(DigitsPairIndices.Y_COORD_INDEX, rows.get(0).size()); // we're assuming that all rows in the list have the same length
        return size;
    }
}
