package gamemechanics.battlefield.aliveentitiescontainers;

import gamemechanics.battlefield.Tile;

public class SpawnPoint {
    private final Tile center;
    private final Integer spawnAreaSide;

    private final Squad squad;

    public SpawnPoint(Tile center, Integer spawnAreaSide, Squad squad) {
        this.center = center;
        this.spawnAreaSide = spawnAreaSide;
        this.squad = squad;
    }

    public Squad getSquad() {
        return squad;
    }

    public void emplaceSquad() {

    }
}
