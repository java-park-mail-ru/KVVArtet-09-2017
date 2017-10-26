package gamemechanics.items.containers;

import gamemechanics.globals.Constants;
import gamemechanics.interfaces.Bag;
import gamemechanics.interfaces.EquipableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageBag implements Bag {
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private final Integer bagID = instanceCounter.getAndIncrement();

    private final String name;
    private final String description;

    private final List<EquipableItem> contents;

    public static class EmptyBagModel {
        public String name;
        public String description;
        public Integer bagSize;

        public EmptyBagModel(String name, String description, Integer bagSize) {
            this.name = name;
            this.description = description;
            this.bagSize = bagSize;
        }
    }

    public static class FilledBagModel {
        public String name;
        public String description;
        public List<EquipableItem> contents;

        public FilledBagModel(String name, String description, List<EquipableItem> contents) {
            this.name = name;
            this.description = description;
            this.contents = contents;
        }
    }

    public StorageBag(EmptyBagModel model) {
        name = model.name;
        description = model.description;
        contents = new ArrayList<>(model.bagSize);
    }

    public StorageBag(FilledBagModel model) {
        name = model.name;
        description = model.description;
        contents = model.contents;
    }

    @Override
    public Integer getInstancesCount() {
        return instanceCounter.get();
    }

    @Override
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
    public Integer getSlotsCount() {
        return contents.size();
    }

    @Override
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
    public Boolean swap(Integer fromPos, Integer toPos) {
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
    public Boolean swap(Integer fromPos, Bag toBag, Integer toPos) {
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
    public Boolean addItem(EquipableItem item) {
        Integer putIndex = getFirstFreeSlot();
        return addItem(item, putIndex);
    }

    @Override
    public Boolean addItem(EquipableItem item, Integer toPos) {
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
    public Boolean throwAway(Integer fromPos, Boolean isConfirmed) {
        if (!isConfirmed || fromPos < 0 || fromPos >= contents.size()) {
            return false;
        }
        contents.set(fromPos, null);
        return true;
    }

    @Override
    public EquipableItem getItem(Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= contents.size()) {
            return null;
        }
        return contents.get(itemIndex);
    }

    protected List<EquipableItem> getContents() {
        return contents;
    }

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
