package gamemechanics.interfaces;

/**
 * interface for various character classes' perks.
 * Perk is an object permanently affecting character's stats or altering some skills.
 * As we
 *
 * @see GameEntity
 * @see AffectorProvider
 * @see gamemechanics.aliveentities.UserCharacter
 * @see CharacterRole
 */
public interface Perk extends GameEntity, AffectorProvider {
    /**
     * get a rank-based single affection value from the multi-value affector
     *
     * @param affectorKind  {@link gamemechanics.components.affectors.Affector} ID to get affection from
     * @param affectorIndex index of the requested affection from the affector
     * @param perkRank      amount of skill points the character has invested in that perk
     * @return if successful - a rank-based affection value (usually it's perkRank * baseAffectionValue)
     * or a special constant if one of IDs is invalid or the affector registered under such ID
     * is not a {@link gamemechanics.components.affectors.ListAffector} or {@link gamemechanics.components.affectors.MapAffector}
     * @see gamemechanics.components.affectors.Affector
     * @see gamemechanics.components.affectors.ListAffector
     * @see gamemechanics.components.affectors.MapAffector
     */
    Integer getRankBasedAffection(Integer affectorKind, Integer affectorIndex, Integer perkRank);

    /**
     * get a rank-based affection value from the single-value affector by its ID
     *
     * @param affectorKind {@link gamemechanics.components.affectors.Affector} ID to get affection from
     * @param perkRank     amoount of skill points the character has invested in that perk
     * @return if successful - a rank-based affection value (usually it's perkRank * baseAffectionValue)
     * or a special constant if ID is invalid or affector registered under such ID
     * is not a {@link gamemechanics.components.affectors.SingleValueAffector}
     * @see gamemechanics.components.affectors.Affector
     * @see gamemechanics.components.affectors.SingleValueAffector
     */
    Integer getRankBasedAffection(Integer affectorKind, Integer perkRank);
}
