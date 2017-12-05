package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.flyweights.perks.IngamePerk;

import javax.validation.constraints.NotNull;

/**
 * interface for various character classes' perks.
 * Perk is an object permanently affecting character's stats or altering some skills.
 * As we
 *
 * @see GameEntity
 * @see AffectorProvider
 * @see project.gamemechanics.aliveentities.UserCharacter
 * @see CharacterRole
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(IngamePerk.class),
})
public interface Perk extends GameEntity, AffectorProvider {
    /**
     * get a rank-based single affection value from the multi-value affector
     *
     * @param affectorKind  {@link project.gamemechanics.components.affectors.Affector} ID to get affection from
     * @param affectorIndex index of the requested affection from the affector
     * @param perkRank      amount of skill points the character has invested in that perk
     * @return if successful - a rank-based affection value (usually it's perkRank * baseAffectionValue)
     * or a special constant if one of IDs is invalid or the affector registered under such ID
     * is not a {@link project.gamemechanics.components.affectors.ListAffector} or {@link project.gamemechanics.components.affectors.MapAffector}
     * @see project.gamemechanics.components.affectors.Affector
     * @see project.gamemechanics.components.affectors.ListAffector
     * @see project.gamemechanics.components.affectors.MapAffector
     */
    Integer getRankBasedAffection(@NotNull Integer affectorKind, @NotNull Integer affectorIndex,
                                  @NotNull Integer perkRank);

    /**
     * get a rank-based affection value from the single-value affector by its ID
     *
     * @param affectorKind {@link project.gamemechanics.components.affectors.Affector} ID to get affection from
     * @param perkRank     amoount of skill points the character has invested in that perk
     * @return if successful - a rank-based affection value (usually it's perkRank * baseAffectionValue)
     * or a special constant if ID is invalid or affector registered under such ID
     * is not a {@link project.gamemechanics.components.affectors.SingleValueAffector}
     * @see project.gamemechanics.components.affectors.Affector
     * @see project.gamemechanics.components.affectors.SingleValueAffector
     */
    Integer getRankBasedAffection(@NotNull Integer affectorKind, @NotNull Integer perkRank);
}
