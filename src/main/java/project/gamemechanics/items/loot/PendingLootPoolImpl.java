package project.gamemechanics.items.loot;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.EquipableItem;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PendingLootPoolImpl implements PendingLootPool {
    private final Map<AliveEntity, List<EquipableItem>> lootPools = new ConcurrentHashMap<>();

    @Override
    public @Nullable List<EquipableItem> getLootPool(@NotNull AliveEntity owner) {
        return lootPools.getOrDefault(owner, null);
    }

    @Override
    public void clearLootPool(@NotNull AliveEntity owner) {
        if (!lootPools.containsKey(owner)) {
            return;
        }
        lootPools.get(owner).clear();
    }

    @Override
    public void createLootPool(@NotNull AliveEntity owner) {
        if (!lootPools.containsKey(owner)) {
            lootPools.put(owner, new ArrayList<>());
        }
    }

    @Override
    public void removeLootPool(@NotNull AliveEntity owner) {
        if (lootPools.containsKey(owner)) {
            lootPools.remove(owner);
        }
    }

    @Override
    public void pollItemFromPool(@NotNull AliveEntity owner, @NotNull Integer itemIndex) {
        if (!lootPools.containsKey(owner) || itemIndex < 0) {
            return;
        }
        if (itemIndex >= lootPools.get(owner).size() || lootPools.get(owner).isEmpty()) {
            return;
        }
        final Bag firstFreeBag = findFirstFreeBag(owner);
        if (firstFreeBag != null) {
            firstFreeBag.addItem(lootPools.get(owner).get(itemIndex));
            lootPools.get(owner).remove(itemIndex.intValue());
        }
    }

    @Override
    public void rejectItemFromPool(@NotNull AliveEntity owner, @NotNull Integer itemIndex) {
        if (!lootPools.containsKey(owner) || itemIndex < 0) {
            return;
        }
        if (itemIndex >= lootPools.get(owner).size() || lootPools.get(owner).isEmpty()) {
            return;
        }
        lootPools.get(owner).remove(itemIndex.intValue());
    }

    @Override
    public void offerItemToPool(@NotNull AliveEntity owner, @NotNull EquipableItem item) {
        if (!lootPools.containsKey(owner)) {
            return;
        }
        lootPools.get(owner).add(item);
    }

    @Override
    public void reset() {
        lootPools.clear();
    }

    private @Nullable Bag findFirstFreeBag(@NotNull AliveEntity owner) {
        Integer bagIndex = 0;
        Bag bag = owner.getBag(bagIndex);
        while (bag != null) {
            if (bag.getFreeSlotsCount() > 0) {
                break;
            }
            bag = owner.getBag(bagIndex++);
        }
        return bag;
    }
}
