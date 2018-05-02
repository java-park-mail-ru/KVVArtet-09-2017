package project.gamemechanics.services.dbmodels;

import javafx.util.Pair;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.interfaces.Slot;
import project.gamemechanics.items.containers.StorageBag;
import project.gamemechanics.services.interfaces.ItemDAO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static project.gamemechanics.components.properties.PropertyCategories.PC_ITEM_ID;

@SuppressWarnings("unused")
public class BagDatabaseModel {
    private Integer id;
    private String name;
    private String description;
    private List<SlotImpl> slotList = new ArrayList<>();

    private ItemDAO itemDAO;

    public BagDatabaseModel(@NotNull Integer id, @NotNull String name, @NotNull String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        for (int i = 0; i < Constants.DEFAULT_NUMBER_OF_SLOTS_IN_BAG; i++) {
            slotList.add(new SlotImpl(Constants.UNDEFINED_ID, 1));
        }
    }

    public BagDatabaseModel(@NotNull String name, @NotNull String description) {
        this.id = 0;
        this.name = name;
        this.description = description;
        for (int i = 0; i < Constants.DEFAULT_NUMBER_OF_SLOTS_IN_BAG; i++) {
            slotList.add(new SlotImpl(Constants.UNDEFINED_ID, 1));
        }
    }

    BagDatabaseModel() {

    }

    public BagDatabaseModel(@NotNull StorageBag.FilledBagModel filledBagModel) {
        List<SlotImpl> slots = new ArrayList<>();
        for (EquipableItem item : filledBagModel.contents) {
            final Integer itemId = item == null ? Constants.UNDEFINED_ID : item.getID();
            final Integer quantity = item == null ? 0 : 1;
            slots.add(new SlotImpl(itemId, quantity));
        }
        this.id = filledBagModel.id;
        this.name = filledBagModel.name;
        this.description = filledBagModel.description;
        if (slots.size() < Constants.DEFAULT_NUMBER_OF_SLOTS_IN_BAG) {
            for (int i = slots.size(); i < Constants.DEFAULT_NUMBER_OF_SLOTS_IN_BAG; i++) {
                slotList.add(new SlotImpl(Constants.UNDEFINED_ID, 1));
            }
        }
        this.slotList = slots;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SlotImpl> getSlotList() {
        return slotList;
    }

    public void setSlotList(List<SlotImpl> slotList) {
        this.slotList = slotList;
    }

    private Integer[] allIds() {
        List<Integer> itemsIds = new ArrayList<>();
        for (Slot slot : slotList) {
            itemsIds.add(slot.getPropertyValueByKind(PC_ITEM_ID));
        }
        return (Integer[]) itemsIds.toArray();
    }

    public void updateSlots(List<Pair<Integer, Integer>> indexToIdItemsList) {
        for (Pair<Integer, Integer> indexToIdPair : indexToIdItemsList) {
            if (indexToIdPair.getValue() == Constants.UNDEFINED_ID) {
                slotList.get(indexToIdPair.getKey()).resetProperties(indexToIdPair.getValue(), 0);
            } else {
                slotList.get(indexToIdPair.getKey()).resetProperties(indexToIdPair.getValue(), 1);
            }
        }
    }

    public StorageBag.FilledBagModel pack() {

        List<EquipableItem> equipableItems = new ArrayList<>();
        for (Integer item : allIds()) {
            equipableItems.add(itemDAO.getItemById(item));
        }
        return new StorageBag.FilledBagModel(this.id,
                this.name, this.description, equipableItems);
    }

    //CHECKSTYLE:OFF
    @Override
    public String toString() {
        return "BagDatabaseModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", slotList=" + slotList +
                ", itemDAO=" + itemDAO +
                '}';
    }

    //CHECKSTYLE:ON
}
