package project.gamemechanics.interfaces;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * An interface providing methods to access entity's affectors.
 *
 * @see project.gamemechanics.components.affectors.Affector
 */
public interface AffectorProvider {
    /**
     * check if entity has an affector.
     *
     * @param affectorKind affector ID
     * @return true if entity has an affector or false otherwise
     */
    @NotNull Boolean hasAffector(@NotNull Integer affectorKind);

    /**
     * get IDs of all entity's affectors.
     *
     * @return entity's affectors' IDs set.
     */
    @NotNull Set<Integer> getAvailableAffectors();

    /**
     * get single value from multi-value affector.
     *
     * @param affectorKind   affector ID
     * @param affectionIndex value index in affector
     * @return value if both parameters are valid, 0 or Integer.MIN_VALUE otherwise
     */
    @NotNull Integer getAffection(@NotNull Integer affectorKind, @NotNull Integer affectionIndex);

    /**
     * get an affector value from single-value or reducable affector.
     *
     * @param affectorKind affector ID
     * @return affector value
     * @see project.gamemechanics.components.affectors.SingleValueAffector
     * @see project.gamemechanics.components.affectors.ListAffector
     * @see project.gamemechanics.components.affectors.MapAffector
     */
    @NotNull Integer getAffection(@NotNull Integer affectorKind);
}
