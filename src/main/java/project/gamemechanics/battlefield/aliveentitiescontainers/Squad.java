package gamemechanics.battlefield.aliveentitiescontainers;

import gamemechanics.components.properties.PropertyCategories;
import gamemechanics.components.properties.SingleValueProperty;
import gamemechanics.globals.Constants;
import gamemechanics.interfaces.AliveEntity;
import gamemechanics.interfaces.Countable;

import javax.validation.constraints.NotNull;
import java.util.List;
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
    private final Integer squadID;

    public Squad(@NotNull List<AliveEntity> members, @NotNull Integer squadID) {
        this.members = members;
        this.squadID = squadID;
        for (AliveEntity member : members) {
            if (member != null) {
                member.setProperty(PropertyCategories.PC_SQUAD_ID, this.squadID);
            }
        }
    }

    @Override
    public Integer getInstancesCount() {
        return INSTANCE_COUNTER.get();
    }

    @Override
    public Integer getID() {
        return id;
    }

    public AliveEntity getMember(Integer memberIndex) {
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

    public Integer getSquadID() {
        return squadID;
    }

    public Boolean areAllDead() {
        return getAliveMembersCount() == 0;
    }
}
