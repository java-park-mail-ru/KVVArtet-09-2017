package project.gamemechanics.battlefield.aliveentitiescontainers;

import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.Countable;
import project.gamemechanics.interfaces.EquipableItem;
import project.gamemechanics.items.loot.IngameLootContainer;
import project.gamemechanics.items.loot.LootContainer;
import project.gamemechanics.items.loot.PendingLootPool;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Squad implements Countable {
    // PvE mode squad IDs
    public static final int PLAYERS_SQUAD_ID = 0;
    public static final int MONSTER_SQUAD_ID = 1;

    // PvP mode squad IDs
    public static final int TEAM_ONE_SQUAD_ID = 0;
    public static final int TEAM_TWO_SQUAD_ID = 1;

    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer id = INSTANCE_COUNTER.getAndIncrement();

    private final List<AliveEntity> members;
    private final Map<AliveEntity, LootContainer> earnedLoot = new HashMap<>();
    private final PendingLootPool lootPool;
    private final Integer squadID;

    public Squad(@NotNull List<AliveEntity> members, @NotNull PendingLootPool lootPool, @NotNull Integer squadID) {
        this.members = members;
        this.squadID = squadID;
        for (AliveEntity member : members) {
            if (member != null) {
                member.setProperty(PropertyCategories.PC_SQUAD_ID, this.squadID);
            }
            earnedLoot.put(member, new IngameLootContainer());
        }
        this.lootPool = lootPool;
    }

    public Squad(@NotNull List<AliveEntity> members, @NotNull Integer squadID) {
        this.members = members;
        this.squadID = squadID;
        this.lootPool = null;
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public Integer getID() {
        return id;
    }

    public @Nullable AliveEntity getMember(Integer memberIndex) {
        if (memberIndex < 0 && memberIndex >= members.size()) {
            return null;
        }
        return members.get(memberIndex);
    }

    public void addMember(@NotNull AliveEntity member) {
        if (member.hasProperty(PropertyCategories.PC_SQUAD_ID)) {
            member.setProperty(PropertyCategories.PC_SQUAD_ID, squadID);
        } else {
            member.addProperty(PropertyCategories.PC_SQUAD_ID, new SingleValueProperty(squadID));
        }
        members.add(member);
    }

    public void removeMember(@NotNull AliveEntity member) {
        if (members.contains(member)) {
            members.remove(member);
            member.setProperty(PropertyCategories.PC_SQUAD_ID, Constants.UNDEFINED_SQUAD_ID);
        }
    }

    public void removeMember(Integer memberIndex) {
        if (memberIndex < 0 || memberIndex >= members.size()) {
            return;
        }
        members.get(memberIndex)
                .setProperty(PropertyCategories.PC_SQUAD_ID, Constants.UNDEFINED_SQUAD_ID);
        members.remove(memberIndex.intValue());
    }

    public Integer getSquadSize() {
        return members.size();
    }

    public Integer getAliveMembersCount() {
        Integer aliveMembers = 0;
        for (AliveEntity member : members) {
            if (member != null) {
                if (member.isAlive()) {
                    ++aliveMembers;
                }
            }
        }
        return aliveMembers;
    }

    public Integer getAverageLevel() {
        Integer accumulatedLevel = 0;
        Integer partySize = 0;
        for (AliveEntity member : members) {
            if (member != null) {
                ++partySize;
                accumulatedLevel += member.getLevel();
            }
        }
        if (partySize == 0) {
            return 0;
        }
        return accumulatedLevel / partySize;
    }

    public Integer getSquadID() {
        return squadID;
    }

    public Boolean areAllDead() {
        return getAliveMembersCount() == 0;
    }

    public void generateLootFor(@NotNull AliveEntity member, @NotNull Bag lootBag) {
        if (!earnedLoot.containsKey(member)) {
            return;
        }
        for (Integer i = 0; i < lootBag.getSlotsCount(); ++i) {
            final EquipableItem item = lootBag.getItem(i);
            if (item != null) {
                earnedLoot.get(member).addItem(item);
            }
        }
    }

    public void addExpFor(@NotNull AliveEntity member, @NotNull Integer amount) {
        if (!earnedLoot.containsKey(member) || amount < 0) {
            return;
        }
        earnedLoot.get(member).changeExp(amount);
    }

    public void addCashFor(@NotNull AliveEntity member, @NotNull Integer amount) {
        if (!earnedLoot.containsKey(member) || amount < 0) {
            return;
        }
        earnedLoot.get(member).changeCash(amount);
    }

    public void distributeLoot() {
        if (lootPool == null) {
            return;
        }
        for (AliveEntity member : earnedLoot.keySet()) {
            member.modifyPropertyByAddition(PropertyCategories.PC_XP_POINTS,
                    DigitsPairIndices.CURRENT_VALUE_INDEX, earnedLoot.get(member).getExpReward());
            member.modifyPropertyByAddition(PropertyCategories.PC_CASH_AMOUNT,
                    earnedLoot.get(member).getCashReward());
            for (EquipableItem lootedItem : earnedLoot.get(member).getItemsList()) {
                lootPool.offerItemToPool(member, lootedItem);
            }
        }
    }
}
