package project.gamemechanics.battlefield.map;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.MapNode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"RedundantSuppression", "unused"})
public class BattleMap {
    private final List<List<MapNode>> rows;

    public BattleMap(@NotNull List<List<MapNode>> rows) {
        this.rows = rows;
    }

    @SuppressWarnings("ConstantConditions")
    private @Nullable List<MapNode> getRow(@NotNull Integer rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public @Nullable MapNode getTile(@NotNull Integer rowIndex, @NotNull Integer tileIndex) {
        final List<MapNode> row = getRow(rowIndex);
        if (row == null || tileIndex < 0) {
            return null;
        }
        if (tileIndex < row.size()) {
            return row.get(tileIndex);
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public @Nullable AliveEntity getInhabitant(@NotNull Integer rowIndex, @NotNull Integer tileIndex) {
        final MapNode tile = getTile(rowIndex, tileIndex);
        if (tile == null) {
            return null;
        }
        return tile.getInhabitant();
    }

    public @NotNull List<Integer> getSize() {
        final List<Integer> size = new ArrayList<>(DigitsPairIndices.PAIR_SIZE);
        size.add(DigitsPairIndices.ROW_COORD_INDEX, rows.size());
        size.add(DigitsPairIndices.COL_COORD_INDEX, rows.get(0).size()); // we're assuming that all rows in the list have the same length
        return size;
    }

    public @NotNull List<Long> encode() {
        final List<Long> encodedMap = new ArrayList<>();

        Long chunk = 0L;
        Integer bitIndex = 0;
        for (List<MapNode> row : rows) {
            for (MapNode tile : row) {
                if (tile.getIsPassable()) {
                    chunk = chunk | (1 << bitIndex);
                }
                ++bitIndex;
                bitIndex %= Long.bitCount(chunk);
                if (bitIndex == 0) {
                    encodedMap.add(chunk);
                    chunk = 0L;
                }
            }
        }

        return encodedMap;
    }
}
