package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.effects.IngameEffect;

/**
 * interface for various effects (DoTs/HoTs, buffs/debuffs, etc.)
 * that may be applied on {@link AliveEntity} during the battle.
 *
 * @see GameEntity
 * @see AffectorProvider
 * @see AliveEntity
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngameEffect.class),
})
public interface Effect extends GameEntity, AffectorProvider {
    /**
     * get effect's duration
     *
     * @return turns before expiring
     */
    Integer getDuration();

    /**
     * check if the effect is expired
     *
     * @return true if the effect's duration is less or equal to 0
     * and the effect isn't perpetual or false otherwise
     */
    Boolean isExpired();

    /**
     * check if the effect is perpetual (like from {@link Perk} action)
     *
     * @return true if the effect is perpetual or false otherwise
     */
    Boolean isPerpetual();

    /**
     * make a single tick
     */
    void tick();
}
