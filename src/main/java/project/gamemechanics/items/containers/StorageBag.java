package project.gamemechanics.items.containers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnusedAssignment", "RedundantSuppression"})
public class StorageBag implements Bag {
    private final Integer bagID;

    private final String name;
    private final String description;

    private final List<EquipableItem> contents;

    public static class EmptyBagModel {
        // CHECKSTYLE:OFF
        final String name;
        final String description;
        final Integer bagSize;
        // CHECKSTYLE:ON

        public EmptyBagModel(@NotNull String name, @NotNull String description, @NotNull Integer bagSize) {
            this.name = name;
            this.description = description;
            this.bagSize = bagSize;
        }
    }

    @SuppressWarnings("unused")
    public static class FilledBagModel {
        // CHECKSTYLE:OFF
        public final Integer id;
        public final String name;
        public final String description;
        public final List<EquipableItem> contents;
        // CHECKSTYLE:ON


        public FilledBagModel(@JsonProperty("id") @NotNull Integer id, @JsonProperty("name") @NotNull String name,
                              @JsonProperty("description") @NotNull String description,
                              @JsonProperty("contents") @NotNull List<EquipableItem> contents) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.contents = contents;
        }
    }

    public StorageBag(@NotNull EmptyBagModel model) {
        bagID = null;
        name = model.name;
        description = model.description;
        contents = new ArrayList<>(model.bagSize);
        for (Integer i = 0; i < model.bagSize; ++i) {
            contents.add(null);
        }
    }

    public StorageBag(@NotNull FilledBagModel model) {
        bagID = model.id;
        name = model.name;
        description = model.description;
        contents = model.contents;
    }

    @Override
    @JsonIgnore
    public @NotNull Integer getInstancesCount() {
        return null;
    }

    @Override
    @JsonProperty("id")
    public @NotNull Integer getID() {
        return bagID;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull String getDescription() {
        return description;
    }

    @Override
    @JsonProperty("slotsCount")
    public @NotNull Integer getSlotsCount() {
        return contents.size();
    }

    @Override
    @JsonProperty("freeSlots")
    public @NotNull Integer getFreeSlotsCount() {
        @NotNull Integer freeSlotsCount = 0;
        for (EquipableItem item : contents) {
            if (item == null) {
                ++freeSlotsCount;
            }
        }
        return freeSlotsCount;
    }

    @Override
    public @NotNull Boolean swap(@NotNull Integer fromPos, @NotNull Integer toPos) {
        //noinspection OverlyComplexBooleanExpression
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
    public @NotNull Boolean swap(@NotNull Integer fromPos, @NotNull Bag toBag, @NotNull Integer toPos) {
        //noinspection OverlyComplexBooleanExpression
        if (fromPos < 0 || fromPos >= contents.size() || toBag == null || toPos < 0) {
            return false;
        }
        if (toPos >= toBag.getSlotsCount()) {
            return false;
        }
        final EquipableItem tmp = contents.get(fromPos);
        contents.set(fromPos, toBag.getItem(toPos));
        toBag.throwAway(toPos, true);
        toBag.addItem(tmp, toPos);
        return true;
    }

    @Override
    public @NotNull Boolean addItem(@NotNull EquipableItem item) {
        final Integer putIndex = getFirstFreeSlot();
        return addItem(item, putIndex);
    }

    @Override
    public @NotNull Boolean addItem(@NotNull EquipableItem item, @NotNull Integer toPos) {
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
    public void throwAway(@NotNull Integer fromPos, @NotNull Boolean isConfirmed) {
        if (!isConfirmed || fromPos < 0 || fromPos >= contents.size()) {
            return;
        }
        contents.set(fromPos, null);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public @Nullable EquipableItem getItem(@NotNull Integer itemIndex) {
        if (itemIndex < 0 || itemIndex >= contents.size()) {
            return null;
        }
        return contents.get(itemIndex);
    }

    @JsonProperty("items")
    @NotNull List<EquipableItem> getContents() {
        return contents;
    }

    @JsonIgnore
    private @NotNull Integer getFirstFreeSlot() {
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
