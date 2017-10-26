package gamemechanics.interfaces;

import java.util.Set;

public interface AffectorProvider {
    Boolean hasAffector(Integer affectorKind);
    Set<Integer> getAvailableAffectors();

    Integer getAffection(Integer affectorKind, Integer affectionIndex);
    Integer getAffection(Integer affectorKind);
}
