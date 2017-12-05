package project.gamemechanics.interfaces;

import java.util.Set;

/**
 * An interface providing methods to access entity's affectors.
 *
 * @see project.gamemechanics.components.affectors.Affector
 */
public interface AffectorProvider {
    /**
     * check if entity has an affector
     *
     * @param affectorKind affector ID
     * @return true if entity has an affector or false otherwise
     */
    Boolean hasAffector(Integer affectorKind);

    /**
     * get IDs of all entity's affectors
     *
     * @return entity's affectors' IDs set.
     */
    Set<Integer> getAvailableAffectors();

    /**
     * get single value from multi-value affector
     *
     * @param affectorKind   affector ID
     * @param affectionIndex value index in affector
     * @return value if both parameters are valid, 0 or Integer.MIN_VALUE otherwise
     */
    Integer getAffection(Integer affectorKind, Integer affectionIndex);

    /**
     * get an affector value from single-value or reducable affector
     *
     * @param affectorKind affector ID
     * @return affector value
     * @see project.gamemechanics.components.affectors.SingleValueAffector
     * @see project.gamemechanics.components.affectors.ListAffector
     * @see project.gamemechanics.components.affectors.MapAffector
     */
    Integer getAffection(Integer affectorKind);
}
