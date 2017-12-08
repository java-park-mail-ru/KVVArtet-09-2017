package gamemechanics.battlefield.aliveentitiescontainers;

import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.Countable;
import gamemechanics.items.loot.PendingLootPool;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
}
