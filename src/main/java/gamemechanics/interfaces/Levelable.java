package gamemechanics.interfaces;

public interface Levelable extends GameEntity {
    Integer getLevel();
    default void levelUp() {}
}
