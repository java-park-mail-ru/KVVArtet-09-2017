package gamemechanics.interfaces;

public interface Perk extends GameEntity, AffectorProvider {
    Integer getRankBasedAffection(Integer affectorKind, Integer affectorIndex, Integer perkRank);
    Integer getRankBasedAffection(Integer affectorKind, Integer perkRank);
}
