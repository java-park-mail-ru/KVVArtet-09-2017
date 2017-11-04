package gamemechanics.battlefield.map.helpers;

public interface Route {
    Integer getLength();
    Integer getTravelCost();
    void walkThrough(Integer distance);
    void walkThrough();
}
