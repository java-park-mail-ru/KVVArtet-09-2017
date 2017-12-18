package project.gamemechanics.battlefield;

import org.jetbrains.annotations.Nullable;
import project.gamemechanics.aliveentities.helpers.CashCalculator;
import project.gamemechanics.aliveentities.helpers.ExperienceCalculator;
import project.gamemechanics.aliveentities.npcs.ai.AI;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventCategories;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.battlefield.aliveentitiescontainers.SpawnPoint;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.battlefield.map.BattleMap;
import project.gamemechanics.battlefield.map.actions.BattleAction;
import project.gamemechanics.battlefield.map.helpers.Pathfinder;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.*;
import project.gamemechanics.interfaces.*;
import project.gamemechanics.items.containers.MonsterLootBag;
import project.gamemechanics.resources.pcg.items.ItemBlueprint;
import project.gamemechanics.resources.pcg.items.ItemPart;
import project.gamemechanics.resources.pcg.items.ItemsFactory;
import project.websocket.messages.ActionRequestMessage;
import project.websocket.messages.ConfirmationMessage;
import project.websocket.messages.ErrorMessage;
import project.websocket.messages.Message;

import javax.validation.constraints.NotNull;
import java.util.*;

public class Battlefield implements Updateable {
    private static final int SQUADS_COUNT = 2;

    private static final int INITIAL_TURN_NUMBER = 1;

    private static final int ACTIONS_PER_TURN = 2;

    public static final int PVE_GAME_MODE = 0;
    public static final int PVP_GAME_MODE = 1;

    @SuppressWarnings("FieldCanBeLocal")
    private final Map<Integer, AI.BehaviorFunction> behaviors;

    private final BattleMap map;
    @SuppressWarnings("FieldCanBeLocal")
    private final Pathfinder pathfinder;

    private final List<Squad> squads = new ArrayList<>(SQUADS_COUNT);

    private final Deque<AliveEntity> battlersQueue = new ArrayDeque<>();

    private final Deque<Action> actionsQueue = new ArrayDeque<>();
    private final List<ActionResult> battleLog = new ArrayList<>();
    private Integer activeBattlerActionsPooled = 0;

    private final List<Effect> appliedEffects = new ArrayList<>();

    private Integer turnCounter = INITIAL_TURN_NUMBER;

    private final ItemsFactory itemsGenerator;

    private final Integer mode;

    public static class BattlefieldModel {
        @SuppressWarnings("PublicField")
        public final Map<Integer, AI.BehaviorFunction> behaviors;
        @SuppressWarnings("PublicField")
        public final BattleMap map;
        @SuppressWarnings("PublicField")
        public final List<SpawnPoint> spawnPoints;
        @SuppressWarnings("PublicField")
        public final ItemsFactory itemsGenerator;
        public final Integer mode;

        public BattlefieldModel(@Nullable Map<Integer, AI.BehaviorFunction> behaviors, @NotNull BattleMap map,
                                @NotNull List<SpawnPoint> spawnPoints, @NotNull ItemsFactory itemsGenerator,
                                @NotNull Integer mode) {
            this.behaviors = behaviors;
            this.map = map;
            this.spawnPoints = spawnPoints;
            this.itemsGenerator = itemsGenerator;
            this.mode = mode;
        }
    }

    @SuppressWarnings("OverlyComplexMethod")
    public Battlefield(@NotNull BattlefieldModel model) {
        behaviors = model.behaviors;
        map = model.map;
        mode = model.mode;
        pathfinder = new Pathfinder(map);
        itemsGenerator = model.itemsGenerator;

        emplaceBattlers(model.spawnPoints);

        switch (mode) {
            case PVE_GAME_MODE:
                squads.set(Squad.PLAYERS_SQUAD_ID, model.spawnPoints.get(Squad.PLAYERS_SQUAD_ID).getSquad());
                mergeMonsterSquads(model.spawnPoints);
                break;
            case PVP_GAME_MODE:
                squads.set(Squad.TEAM_ONE_SQUAD_ID, model.spawnPoints.get(Squad.TEAM_ONE_SQUAD_ID).getSquad());
                squads.set(Squad.TEAM_TWO_SQUAD_ID, model.spawnPoints.get(Squad.TEAM_TWO_SQUAD_ID).getSquad());
                break;
            default:
                break;
        }

        final List<AliveEntity> battlersList = new ArrayList<>();
        for (Squad squad : squads) {
            for (Integer i = 0; i < squad.getSquadSize(); ++i) {
                if (squad.getMember(i) != null) {
                    battlersList.add(squad.getMember(i));
                }
            }
        }
        battlersList.sort(Comparator.comparingInt(AliveEntity::getInitiative));
        for (AliveEntity battler : battlersList) {
            battlersQueue.addFirst(battler);
        }

        final Squad monsterSquad = squads.get(Squad.MONSTER_SQUAD_ID);
        for (Integer i = 0; i < monsterSquad.getSquadSize(); ++i) {
            final Map<Integer, AI.BehaviorFunction> monsterBehaviors = new HashMap<>();
            Integer activeBehaviorId = Constants.WRONG_INDEX;
            for (Integer behaviorId : Objects.requireNonNull(Objects.requireNonNull(
                    monsterSquad.getMember(i)).getCharacterRole().getBehaviorIds())) {
                if (behaviors.containsKey(behaviorId)) {
                    if (activeBehaviorId == Constants.WRONG_INDEX) {
                        activeBehaviorId = behaviorId;
                    }
                    monsterBehaviors.put(behaviorId, behaviors.get(behaviorId));
                }
            }
            Objects.requireNonNull(monsterSquad.getMember(i))
                    .setBehavior(new AI(Objects.requireNonNull(monsterSquad.getMember(i)),
                    monsterSquad, squads.get(Squad.PLAYERS_SQUAD_ID), map, pathfinder,
                    Objects.requireNonNull(monsterSquad.getMember(i)).getCharacterRole().getAllAbilities(),
                    monsterBehaviors, activeBehaviorId));
        }
    }

    public Integer getTurn() {
        return turnCounter;
    }

    @Override
    public void update() {
        if (actionsQueue.isEmpty()) {
            return;
        }
        ++turnCounter;
        while (!actionsQueue.isEmpty()) {
            battleLog.add(actionsQueue.getFirst().execute());
        }

        if (activeBattlerActionsPooled == 0) {
            updateBattlers();
        }

        removeDead();
        processEvents();

        if (!isBattleFinished()) {
            final AliveEntity activeBattler = battlersQueue.getFirst();
            if (activeBattler.isControlledByAI()) {
                while (activeBattlerActionsPooled < ACTIONS_PER_TURN) {
                    pushAction(activeBattler.makeDecision());
                    actionsQueue.pollFirst().execute();
                }
            }

            if (activeBattlerActionsPooled == ACTIONS_PER_TURN) {
                activeBattlerActionsPooled = 0;
                battlersQueue.addLast(battlersQueue.pollFirst());
                // end turn event will be added to the last pooled action
                battleLog.get(battleLog.size() - 1).addEvent(EventsFactory.makeEndTurnEvent());
            }
        } else {
            onBattleEnd();
        }
    }

    public Boolean isBattleFinished() {
        return isVictory() || isDefeat();
    }

    public Boolean isVictory() {
        return squads.get(Squad.TEAM_TWO_SQUAD_ID).areAllDead();
    }

    public Boolean isDefeat() {
        return squads.get(Squad.TEAM_ONE_SQUAD_ID).areAllDead();
    }

    public void pushAction(@NotNull Action action) {
        if (action.getSender().getInhabitant().equals(battlersQueue.getFirst())
                && activeBattlerActionsPooled < ACTIONS_PER_TURN) {
            actionsQueue.addLast(action);
            ++activeBattlerActionsPooled;
        }
    }

    @SuppressWarnings("SameReturnValue")
    public Message pushAction(ActionRequestMessage message) {
        MapNode senderTile = map.getTile(message.getSender().getCoordinate(0), message.getSender().getCoordinate(1));
        if(senderTile.isOccupied() && senderTile != null){
            BattleAction battleAction = new BattleAction(message.getSender(), message.getTarget(), message.getAbility(), pathfinder);
            pushAction(battleAction);
            return new ConfirmationMessage("Action delivered");
        } else {
            return new ErrorMessage("Unexpected data in ActionRequestMessage");
        }

    }

    public Integer getBattleLogLength() {
        return battleLog.size();
    }

    public List<ActionResult> getBattleLog() {
        return battleLog;
    }

    public @Nullable ActionResult getBattleLogEntry(Integer entryIndex) {
        if (entryIndex < 0 || entryIndex >= battleLog.size()) {
            return null;
        }
        return battleLog.get(entryIndex);
    }

    public List<Long> encodeMap() {
        return map.encode();
    }

    private void emplaceBattlers(@NotNull List<SpawnPoint> spawnPoints) {
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint != null) {
                spawnPoint.emplaceSquad();
            }
        }
    }

    private void mergeMonsterSquads(@NotNull List<SpawnPoint> spawnPoints) {
        final Squad monsterSquad = new Squad(new ArrayList<>(), Squad.MONSTER_SQUAD_ID);
        for (SpawnPoint spawnPoint : spawnPoints) {
            if (spawnPoint != null) {
                if (spawnPoint.getSquad().getSquadID() == Squad.MONSTER_SQUAD_ID) {
                    for (Integer i = 0; i < spawnPoint.getSquad().getSquadSize(); ++i) {
                        final AliveEntity member = spawnPoint.getSquad().getMember(i);
                        if (member != null) {
                            monsterSquad.addMember(member);
                        }
                    }
                }
            }
        }
        squads.set(Squad.MONSTER_SQUAD_ID, monsterSquad);
    }

    private void removeDead() {
        for (AliveEntity battler : battlersQueue) {
            if (!battler.isAlive()) {
                battlersQueue.remove(battler);
            }
        }
    }

    private void removeExpiredEffects() {
        for (Effect effect : appliedEffects) {
            if (effect.isExpired()) {
                appliedEffects.remove(effect);
            }
        }
    }

    private void endBattleCleanup() {
        for (Squad squad : squads) {
            for (Integer i = 0; i < squad.getSquadSize(); ++i) {
                final AliveEntity member = squad.getMember(i);
                if (member != null) {
                    member.removeProperty(PropertyCategories.PC_COORDINATES);
                }
            }
        }
    }

    private void generateLoot() {
        final Integer winnersExpBonus = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(squads.get(Squad.PLAYERS_SQUAD_ID).getAverageLevel(),
                        squads.get(Squad.MONSTER_SQUAD_ID).getAverageLevel()),
                squads.get(Squad.PLAYERS_SQUAD_ID).getSquadSize());
        final Integer winnersGoldBonus = CashCalculator.getPartyBiasedCashReward(
                CashCalculator.getCashReward(squads.get(Squad.MONSTER_SQUAD_ID).getAverageLevel()),
                squads.get(Squad.PLAYERS_SQUAD_ID).getSquadSize());

        for (Integer i = 0; i < squads.get(Squad.PLAYERS_SQUAD_ID).getSquadSize(); ++i) {
            final AliveEntity member = squads.get(Squad.PLAYERS_SQUAD_ID).getMember(i);
            if (member != null) {
                squads.get(Squad.PLAYERS_SQUAD_ID).addExpFor(member, winnersExpBonus);
                squads.get(Squad.PLAYERS_SQUAD_ID).addCashFor(member, winnersGoldBonus);
            }
        }

        squads.get(Squad.PLAYERS_SQUAD_ID).distributeLoot();
    }

    private void rewardTeam(@NotNull Integer teamID) {
        final Integer looseTeamID;
        switch (teamID) {
            case Squad.TEAM_ONE_SQUAD_ID:
                looseTeamID = Squad.TEAM_TWO_SQUAD_ID;
                break;
            case Squad.TEAM_TWO_SQUAD_ID:
                looseTeamID = Squad.TEAM_ONE_SQUAD_ID;
                break;
            default:
                return;
        }
        final Integer winnersExpBonus = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(squads.get(teamID).getAverageLevel(),
                        squads.get(looseTeamID).getAverageLevel()), squads.get(teamID).getSquadSize());
        final Integer losersExpBonus = ExperienceCalculator.getPartyBiasedXPReward(
                ExperienceCalculator.getXPReward(squads.get(looseTeamID).getAverageLevel(),
                        squads.get(teamID).getAverageLevel()), squads.get(looseTeamID).getSquadSize()) / 2;

        final Integer winnersGoldBonus = CashCalculator.getPartyBiasedCashReward(
                CashCalculator.getCashReward(squads.get(looseTeamID).getAverageLevel()),
                squads.get(teamID).getSquadSize());
        final Integer losersGoldBonus = CashCalculator.getPartyBiasedCashReward(
                CashCalculator.getCashReward(squads.get(teamID).getAverageLevel()),
                squads.get(looseTeamID).getSquadSize());
        // reward winners with full-scale
        for (Integer i = 0; i < squads.get(teamID).getSquadSize(); ++i) {
            final AliveEntity member = squads.get(teamID).getMember(i);
            if (member != null) {
                squads.get(teamID).addExpFor(member, winnersExpBonus);
                squads.get(teamID).addCashFor(member, winnersGoldBonus);
                generatePvpKillReward(squads.get(teamID), member);
            }
        }
        // also reward the team that's lost but with lesser reward
        for (Integer i = 0; i < squads.get(looseTeamID).getSquadSize(); ++i) {
            final AliveEntity member = squads.get(looseTeamID).getMember(i);
            if (member != null) {
                squads.get(looseTeamID).addExpFor(member, losersExpBonus);
                squads.get(looseTeamID).addCashFor(member, losersGoldBonus);
            }
        }

        for (Squad squad : squads) {
            squad.distributeLoot();
        }
    }

    private void generateReward() {
        switch (mode) {
            case PVE_GAME_MODE:
                if (isVictory()) {
                    generateLoot();
                }
                break;
            case PVP_GAME_MODE:
                if (isVictory()) {
                    rewardTeam(Squad.TEAM_ONE_SQUAD_ID);
                } else {
                    rewardTeam(Squad.TEAM_TWO_SQUAD_ID);
                }
                break;
            default:
                break;
        }
    }

    private void onBattleEnd() {
        endBattleCleanup();
        generateReward();
    }

    @SuppressWarnings("OverlyComplexMethod")
    private void processEvents() {
        for (ActionResult entry : battleLog) {
            if (!entry.getIsProcessed()) {
                for (Integer eventIndex = 0; eventIndex < entry.getEventsCount(); ++eventIndex) {
                    final TurnEvent event = entry.getEvent(eventIndex);
                    if (event.getEventKind() == EventCategories.EC_ROLLBACK) {
                        if (activeBattlerActionsPooled > 0) {
                            --activeBattlerActionsPooled;
                            break;
                        }
                    }

                    if (event.getEventKind() == EventCategories.EC_END_TURN) {
                        break;
                    }

                    if (event.getEventKind() == EventCategories.EC_HITPOINTS_CHANGE) {
                        if (event.getAmount() < 0 && !event.getWhere().getInhabitant().isAlive()) {
                            final Integer squadIdToReward = event.getWhere().getInhabitant()
                                    .getProperty(PropertyCategories.PC_SQUAD_ID) == Squad.TEAM_ONE_SQUAD_ID
                                    ? Squad.TEAM_TWO_SQUAD_ID : Squad.TEAM_ONE_SQUAD_ID;
                            if (squadIdToReward == Squad.PLAYERS_SQUAD_ID || mode == PVP_GAME_MODE) {
                                Integer averagePartyLevel = 0;
                                for (Integer i = 0; i < squads.get(squadIdToReward).getSquadSize(); ++i) {
                                    final AliveEntity member = squads.get(squadIdToReward).getMember(i);
                                    if (member != null) {
                                        if (member.isAlive()) {
                                            averagePartyLevel += member.getLevel();
                                        }
                                    }
                                }
                                final Integer expAmount = ExperienceCalculator.getPartyBiasedXPReward(
                                        ExperienceCalculator.getXPReward(averagePartyLevel
                                                        / squads.get(squadIdToReward).getAliveMembersCount(),
                                                event.getWhere().getInhabitant().getLevel()),
                                        squads.get(squadIdToReward).getAliveMembersCount());
                                final Integer cashAmount = CashCalculator.getPartyBiasedCashReward(
                                        CashCalculator.getCashReward(event.getWhere().getInhabitant().getLevel()),
                                        squads.get(squadIdToReward).getAliveMembersCount());
                                for (Integer i = 0; i < squads.get(squadIdToReward).getSquadSize(); ++i) {
                                    final AliveEntity member = squads.get(squadIdToReward).getMember(i);
                                    if (member != null) {
                                        if (member.isAlive() && member.hasProperty(PropertyCategories.PC_XP_POINTS)
                                                && member.hasProperty(PropertyCategories.PC_CASH_AMOUNT)) {
                                            member.modifyPropertyByAddition(PropertyCategories.PC_XP_POINTS,
                                                    DigitsPairIndices.CURRENT_VALUE_INDEX, expAmount);
                                            member.modifyPropertyByAddition(PropertyCategories.PC_CASH_AMOUNT,
                                                    cashAmount);
                                            member.modifyPropertyByAddition(PropertyCategories.PC_STATISTICS,
                                                    UserCharacterStatistics.US_GOLD_EARNED, cashAmount);
                                            entry.addEvent(entry.getEventIndex(event) + 1,
                                                    EventsFactory.makeRewardEvent(map.getTile(
                                                            member.getProperty(PropertyCategories.PC_COORDINATES,
                                                                    DigitsPairIndices.ROW_COORD_INDEX),
                                                            member.getProperty(PropertyCategories.PC_COORDINATES,
                                                                    DigitsPairIndices.COL_COORD_INDEX)),
                                                            expAmount, cashAmount));
                                            if (mode == PVE_GAME_MODE) {
                                                Integer propertyIndex = UserCharacterStatistics.US_PVE_ASSISTS;
                                                if (member.equals(entry.getSender().getInhabitant())) {
                                                    propertyIndex = UserCharacterStatistics.US_PVE_KILLS;
                                                }
                                                member.modifyPropertyByAddition(PropertyCategories.PC_STATISTICS,
                                                        propertyIndex, 1);
                                            } else {
                                                Integer propertyIndex = UserCharacterStatistics.US_PVP_ASSISTS;
                                                if (member.equals(entry.getSender().getInhabitant())) {
                                                    propertyIndex = UserCharacterStatistics.US_PVP_KILLS;
                                                }
                                                member.modifyPropertyByAddition(PropertyCategories.PC_STATISTICS,
                                                        propertyIndex, 1);
                                            }
                                        }
                                    }
                                }

                                if (mode == PVE_GAME_MODE) {
                                    final Bag lootBag = event.getWhere().getInhabitant()
                                            .getBag(Constants.PERSONAL_REWARD_LOOT_BAG_ID);
                                    if (lootBag != null) {
                                        squads.get(squadIdToReward)
                                                .generateLootFor(entry.getSender().getInhabitant(), lootBag);
                                    }
                                } else {
                                    generatePvpKillReward(squads.get(squadIdToReward),
                                            entry.getSender().getInhabitant());
                                }
                                shareGroupKillReward(squads.get(squadIdToReward), event.getWhere().getInhabitant());
                            }
                        }
                    }
                }

                entry.markProcessed();
            }
        }
    }

    private ItemRarity defineItemRarity(@NotNull Random random) {
        final Integer roll = random.nextInt(Constants.WIDE_PERCENTAGE_CAP_INT);
        if (roll < ItemRarity.IR_LEGENDARY.getDefaultDropChance()) {
            return ItemRarity.IR_LEGENDARY;
        }
        if (roll < ItemRarity.IR_EPIC.getDefaultDropChance()) {
            return ItemRarity.IR_EPIC;
        }
        if (roll < ItemRarity.IR_RARE.getDefaultDropChance()) {
            return ItemRarity.IR_RARE;
        }
        if (roll < ItemRarity.IR_GOOD.getDefaultDropChance()) {
            return ItemRarity.IR_GOOD;
        }
        if (roll < ItemRarity.IR_COMMON.getDefaultDropChance()) {
            return ItemRarity.IR_COMMON;
        }
        return ItemRarity.IR_TRASH;
    }

    private void generatePvpKillReward(@NotNull Squad killersSquad, @NotNull AliveEntity killer) {

        final Random random = new Random(System.currentTimeMillis());

        final Map<Integer, Integer> itemParts = new HashMap<>(ItemPart.ITEM_PARTS_COUNT);
        for (Integer i = ItemPart.FIRST_PART_ID; i < ItemPart.ITEM_PARTS_COUNT; ++i) {
            itemParts.put(i, Constants.UNDEFINED_ID);
        }
        final Set<Integer> equipableKinds = killer.getCharacterRole().getEquipableKinds();
        final List<Integer> availableEquipmentKinds;
        if (equipableKinds != null) {
            availableEquipmentKinds = new ArrayList<>(equipableKinds);
        } else {
            availableEquipmentKinds = null;
        }
        final Integer lootBagSize = Constants.DEFAULT_PERSONAL_REWARD_BAG_SIZE
                + random.nextInt(2) - random.nextInt(2);
        final List<ItemBlueprint> lootList = new ArrayList<>(lootBagSize);
        for (Integer i = 0; i < lootBagSize; ++i) {
            final Integer equipmentKind = availableEquipmentKinds == null
                    ? random.nextInt(EquipmentKind.EK_SIZE.asInt())
                    : availableEquipmentKinds.get(
                    random.nextInt(availableEquipmentKinds.size()));

            Integer level = killer.getLevel()
                    + random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING) / 2
                    - random.nextInt(Constants.LEVEL_RANGE_FOR_LOOT_DROPPING) / 2;
            level = level > Constants.MAX_LEVEL ? Constants.MAX_LEVEL
                    : level < Constants.START_LEVEL ? Constants.START_LEVEL : level;

            final ItemRarity rarity = defineItemRarity(random);

            final Map<Integer, Property> properties = new HashMap<>();
            properties.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(level));

            final Property rarityProperty =
                    new SingleValueProperty(rarity == ItemRarity.IR_TRASH
                            ? ItemRarity.IR_UNDEFINED.asInt() : rarity.asInt());
            properties.put(PropertyCategories.PC_ITEM_RARITY, rarityProperty);

            properties.put(PropertyCategories.PC_ITEM_KIND, new SingleValueProperty(equipmentKind));

            lootList.set(i, new ItemBlueprint(
                    rarity == ItemRarity.IR_UNDEFINED
                            ? rarity.getDefaultDropChance()
                            : Constants.UNDEFINED_RARITY_DEFAULT_DROP_CHANCE,
                    properties, itemParts));
            killersSquad.generateLootFor(killer, new MonsterLootBag(lootList, level, itemsGenerator));
        }
    }

    private void shareGroupKillReward(@NotNull Squad killers, @NotNull AliveEntity killed) {
        for (Integer memberIndex = 0; memberIndex < killers.getSquadSize(); ++memberIndex) {
            final AliveEntity member = killers.getMember(memberIndex);
            if (member != null && member.isAlive()) {
                if (mode == PVE_GAME_MODE) {
                    killers.generateLootFor(member, killed.getBag(Constants.GENERIC_REWARD_LOOT_BAG_ID));
                } else {
                    generatePvpKillReward(killers, member);
                }
            }
        }
    }

    private void updateBattlers() {
        for (Squad squad : squads) {
            for (Integer memberIndex = 0; memberIndex < squad.getSquadSize(); ++memberIndex) {
                final AliveEntity member = squad.getMember(memberIndex);
                if (member != null && member.isAlive()) {
                    member.update();
                }
            }
        }
        removeExpiredEffects();
    }
}
