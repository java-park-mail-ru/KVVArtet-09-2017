package gamemechanics.items.containers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.Bag;
import gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageBag implements Bag {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer bagID;

    private final String name;
    private final String description;

    private final List<EquipableItem> contents;

    public static class EmptyBagModel {
        public String name;
        public String description;
        public Integer bagSize;

        public EmptyBagModel(@NotNull String name, @NotNull String description, @NotNull Integer bagSize) {
            this.name = name;
            this.description = description;
            this.bagSize = bagSize;
        }
    }

    public static class FilledBagModel {
        public Integer id;
        public String name;
        public String description;
        public List<EquipableItem> contents;

        public FilledBagModel(@NotNull Integer id, @NotNull String name,
                              @NotNull String description, @NotNull List<EquipableItem> contents) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.contents = contents;
        }
    }

    public StorageBag(@NotNull EmptyBagModel model) {
        bagID = instanceCounter.getAndIncrement();
        name = model.name;
        description = model.description;
        contents = new ArrayList<>(model.bagSize);
    }

    public StorageBag(@NotNull FilledBagModel model) {
        bagID = model.id;
        name = model.name;
        description = model.description;
        contents = model.contents;
    }

    @Override
    @JsonIgnore
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
    @JsonProperty("id")
    public Integer getID() {
        return bagID;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    @JsonProperty("slotsCount")
    public Integer getSlotsCount() {
        return contents.size();
    }

    @Override
    @JsonProperty("freeSlots")
    public Integer getFreeSlotsCount() {
        Integer freeSlotsCount = 0;
        for (EquipableItem item : contents) {
            if (item == null) {
                ++freeSlotsCount;
            }
        }
        return freeSlotsCount;
    }

    @Override
    public Boolean swap(@NotNull Integer fromPos, @NotNull Integer toPos) {
        if (fromPos < 0 || toPos < 0 || fromPos >= contents.size() || toPos >= contents.size()) {
            return false;
        }
        EquipableItem tmp = contents.get(fromPos);
        contents.set(fromPos, contents.get(toPos));
        contents.set(toPos, tmp);
        tmp = null;
        return true;
    }

    @Override
    public Boolean swap(@NotNull Integer fromPos, @NotNull Bag toBag, @NotNull Integer toPos) {
        if (fromPos < 0 || fromPos >= contents.size() || toBag == null || toPos < 0) {
            return false;
        }
        if (toPos >= toBag.getSlotsCount()) {
            return false;
        }
        EquipableItem tmp = contents.get(fromPos);
        contents.set(fromPos, toBag.getItem(toPos));
        toBag.throwAway(toPos, true);
        toBag.addItem(tmp, toPos);
        return true;
    }

    @Override
    public Boolean addItem(@NotNull EquipableItem item) {
        Integer putIndex = getFirstFreeSlot();
        return addItem(item, putIndex);
    }

    @Override
    public Boolean addItem(@NotNull EquipableItem item, @NotNull Integer toPos) {
        if (toPos < 0 || toPos >= contents.size()) {
            return false;
        }
        if (contents.get(toPos) != null) {
            return false;
        }
        contents.set(toPos, item);
        return true;
    }

    @Override
    public Boolean throwAway(@NotNull Integer fromPos, @NotNull Boolean isConfirmed) {
        if (!isConfirmed || fromPos < 0 || fromPos >= contents.size()) {
            return false;
        }
        contents.set(fromPos, null);
        return true;
    }

    @Override
    public EquipableItem getItem(@NotNull Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= contents.size()) {
            return null;
        }
        return contents.get(itemIndex);
    }

    @JsonProperty("items")
    protected List<EquipableItem> getContents() {
        return contents;
    }

    @JsonIgnore
    private Integer getFirstFreeSlot() {
        if (contents.isEmpty()) {
            return Constants.WRONG_INDEX;
        }
        Integer slotIndex = 0;
        for (EquipableItem item : contents) {
            if (item == null) {
                return slotIndex;
            }
            ++slotIndex;
        }
        return Constants.WRONG_INDEX;
    }
}
