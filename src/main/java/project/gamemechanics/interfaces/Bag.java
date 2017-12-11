package project.gamemechanics.interfaces;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import project.gamemechanics.items.containers.CharacterDoll;
import project.gamemechanics.items.containers.MonsterLootBag;
import project.gamemechanics.items.containers.StorageBag;

import javax.validation.constraints.NotNull;

/**
 * interface for various item containers.
 *
 * @see GameEntity
 * @see EquipableItem
 */
@SuppressWarnings("NewClassNamingConvention")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(StorageBag.class),
        @JsonSubTypes.Type(CharacterDoll.class),
        @JsonSubTypes.Type(MonsterLootBag.class),
})
public interface Bag extends GameEntity {
    /**
     * get total slots count
     *
     * @return slots number in the bag
     */
    Integer getSlotsCount();

    /**
     * get free slots number
     *
     * @return free slots count
     */
    Integer getFreeSlotsCount();

    /**
     * swap two slots inside single bag
     *
     * @param fromPos first slot's index
     * @param toPos   second slot's index
     * @return true if the swap was successful or false otherwise
     */
    Boolean swap(@NotNull Integer fromPos, @NotNull Integer toPos);

    /**
     * swap two slots in different bag
     *
     * @param fromPos index of first slot to swap (in this bag)
     * @param toBag   bag to swap slots with
     * @param toPos   index of the second slot to swap (in another bag)
     * @return true if the swap was successful or false otherwise
     */
    Boolean swap(@NotNull Integer fromPos, @NotNull Bag toBag, @NotNull Integer toPos);

    /**
     * put a new item in the bag
     *
     * @param item new item to put in the bag
     * @return true if there're free slots in the bag or false otherwise
     * @see EquipableItem
     */
    Boolean addItem(@NotNull EquipableItem item);

    /**
     * put a new item  in the concrete slot
     *
     * @param item  new item to put in the bag
     * @param toPos slot index to put new item in
     * @return true if the chosen slot's free or false otherwise
     * @see EquipableItem
     */
    Boolean addItem(@NotNull EquipableItem item, @NotNull Integer toPos);

    /**
     * remove the item from the bag
     *
     * @param fromPos     slot index to remove item from
     * @param isConfirmed is operation confirmed by the user
     *                    if the removal wasn't confirmed or the chosen slot was empty
     */
    void throwAway(@NotNull Integer fromPos, @NotNull Boolean isConfirmed);

    /**
     * get the item from the bag by slot index
     *
     * @param itemIndex slot index to get the item from
     * @return null if index is invalid or slot's contents
     * (null if the slot's empty or {@link EquipableItem} otherwise)
     * @see EquipableItem
     */
    EquipableItem getItem(@NotNull Integer itemIndex);
}
