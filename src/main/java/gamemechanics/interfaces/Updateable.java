package gamemechanics.interfaces;

public interface Updateable {
    default void update() {} // for battle turns
    default void update(Integer timestep) {} // for any other occasions;
}
