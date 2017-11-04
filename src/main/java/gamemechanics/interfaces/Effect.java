package gamemechanics.interfaces;

public interface Effect extends GameEntity, AffectorProvider {
    Integer getDuration();
    Boolean isExpired();
    Boolean isPerpetual();

    void tick();
}
