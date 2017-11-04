package gamemechanics.battlefield.map.tilesets;

public interface Tileset {
    Integer getTilesCount();
    Boolean isValid();
    default void applyEffects() {}
}
