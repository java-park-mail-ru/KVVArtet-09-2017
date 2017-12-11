package project.gamemechanics.battlefield.aliveentitiescontainers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.EquipmentKind;
import project.gamemechanics.globals.ItemRarity;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.interfaces.Countable;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemPart;
import project.gamemechanics.resources.pcg.items.ItemsFactory;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CharactersParty implements Countable {
    private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger(0);
    private final Integer partyID = INSTANCE_COUNTER.getAndIncrement();

    private final Map<Integer, AliveEntity> members;
    private final PendingLootPool lootPool;

    public CharactersParty(@NotNull Map<Integer, AliveEntity> members,
                           @NotNull PendingLootPool lootPool) {
        this.members = members;
        this.lootPool = lootPool;
    }

    public CharactersParty(@NotNull PendingLootPool lootPool) {
        this(new HashMap<>(), lootPool);
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public Integer getID() {
        return partyID;
    }

    public Squad toSquad(@NotNull Integer squadId) {
        final List<AliveEntity> membersList = new ArrayList<>();
        for (Integer roleId : members.keySet()) {
            if (members.get(roleId) != null) {
                membersList.add(members.get(roleId));
            }
        }
        return new Squad(membersList, lootPool, squadId);
    }

    public Squad toSquad() {
        return this.toSquad(Squad.PLAYERS_SQUAD_ID);
    }

    public Boolean hasRole(Integer roleId) {
        return members.getOrDefault(roleId, null) != null;
    }

    public Boolean addMember(@NotNull Integer roleId, @NotNull AliveEntity member) {
        if (hasRole(roleId) || !members.containsKey(roleId)) {
            return false;
        }
        members.replace(roleId, member);
        return true;
    }

    public Boolean removeMember(@NotNull Integer roleId) {
        if (!hasRole(roleId)) {
            return false;
        }
        members.replace(roleId, null);
        return true;
    }

    public AliveEntity getMember(@NotNull Integer roleId) {
        return members.getOrDefault(roleId, null);
    }

    public Integer getPartySize() {
        Integer membersCount = 0;
        for (Integer roleId : members.keySet()) {
            if (members.get(roleId) != null) {
                ++membersCount;
            }
        }
        return membersCount;
    }

    public Boolean areAllDead() {
        for (Integer roleId : members.keySet()) {
            if (members.get(roleId) != null) {
                if (members.get(roleId).isAlive()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Integer getAverageLevel() {
        Integer accumulatedLevel = 0;
        Integer partySize = 0;
        for (Integer roleId : members.keySet()) {
            if (members.get(roleId) != null) {
                ++partySize;
                accumulatedLevel += members.get(roleId).getLevel();
            }
        }
        if (partySize == 0) {
            return 0;
        }
        return accumulatedLevel / partySize;
    }

    public void giveRewardForInstance(@NotNull Integer roleId, @NotNull Integer extraExp,
                                      @NotNull Integer extraGold, @Nullable ItemsFactory itemsFactory) {
        final AliveEntity member = members.get(roleId);
        if (member == null) {
            return;
        }
        member.modifyPropertyByAddition(PropertyCategories.PC_XP_POINTS,
                DigitsPairIndices.CURRENT_VALUE_INDEX, extraExp);
        member.modifyPropertyByAddition(PropertyCategories.PC_CASH_AMOUNT, extraGold);
        if (itemsFactory != null) {
            final Map<Integer, Integer> itemParts = new HashMap<>(ItemPart.ITEM_PARTS_COUNT);
            for (Integer i = ItemPart.FIRST_PART_ID; i < ItemPart.ITEM_PARTS_COUNT; ++i) {
                itemParts.put(i, Constants.UNDEFINED_ID);
            }
            final Set<Integer> equipableKinds = member.getCharacterRole().getEquipableKinds();
            final List<Integer> availableEquipmentKinds;
            if (equipableKinds != null) {
                availableEquipmentKinds = new ArrayList<>(equipableKinds);
            } else {
                availableEquipmentKinds = null;
            }
            final Random random = new Random(System.currentTimeMillis());
            final Integer extraLootCount = Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE
                    + random.nextInt(Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE / 2);
            for (Integer i = 0; i < extraLootCount; ++i) {
                final Integer equipmentKind = availableEquipmentKinds == null
                        ? random.nextInt(EquipmentKind.EK_SIZE.asInt())
                        : availableEquipmentKinds.get(
                        random.nextInt(availableEquipmentKinds.size()));

                Integer level = member.getLevel()
                        + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING) / 2
                        - random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING) / 2;
                level = level > Constants.MAX_LEVEL ? Constants.MAX_LEVEL
                        : level < Constants.START_LEVEL ? Constants.START_LEVEL : level;

                final Map<Integer, Property> properties = new HashMap<>();
                properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));

                final Property rarityProperty =
                        new SingleValueProperty(ItemRarity.IR_UNDEFINED.asInt());
                properties.put(PropertyCategories.PC_ITEM_RARITY, rarityProperty);

                properties.put(PropertyCategories.PC_ITEM_KIND, new SingleValueProperty(equipmentKind));
                lootPool.offerItemToPool(member, itemsFactory.makeItem(new ItemBlueprint(
                        Constants.WIDE_PERCENTAGE_CAP_INT, properties, itemParts)));
            }
        }
    }

    @JsonIgnore
    public Set<Integer> getRoleIds() {
        return members.keySet();
    }
}
