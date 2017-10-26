package gamemechanics.interfaces;

public interface Bag extends GameEntity {
    Integer getSlotsCount();
    Integer getFreeSlotsCount();
    Boolean swap(Integer fromPos, Integer toPos);
    Boolean swap(Integer fromPos, Bag toBag, Integer toPos);
    Boolean addItem(EquipableItem item);
    Boolean addItem(EquipableItem item, Integer toPos);
    Boolean throwAway(Integer fromPos, Boolean isConfirmed);
    EquipableItem getItem(Integer itemIndex);
}
