package project.gamemechanics.matchmaking;

import org.junit.BeforeClass;
import org.junit.Test;
import project.gamemechanics.battlefield.aliveentitiescontainers.CharactersParty;
import project.gamemechanics.battlefield.aliveentitiescontainers.Squad;
import project.gamemechanics.globals.CharacterRoleIds;
import project.gamemechanics.globals.DigitsPairIndices;
import project.gamemechanics.globals.GameModes;
import project.gamemechanics.interfaces.AliveEntity;
import project.gamemechanics.items.loot.PendingLootPool;
import project.gamemechanics.items.loot.PendingLootPoolImpl;
import project.gamemechanics.resources.assets.AssetProvider;
import project.gamemechanics.resources.assets.AssetProviderImpl;
import project.gamemechanics.resources.assets.DummiesFactory;
import project.gamemechanics.world.config.ResourcesConfig;
import project.gamemechanics.world.matchmaking.invitations.invitations.Invitation;
import project.gamemechanics.world.matchmaking.invitations.polls.CoopPvpPoll;
import project.gamemechanics.world.matchmaking.invitations.polls.Poll;
import project.gamemechanics.world.matchmaking.invitations.polls.PvePoll;
import project.gamemechanics.world.matchmaking.invitations.polls.SquadPvpPoll;

import javax.validation.constraints.NotNull;
import java.util.*;

import static org.junit.Assert.*;

public class PollsTest {
    private static DummiesFactory dummiesFactory;
    private static PendingLootPool pendingLootPool;

    @BeforeClass
    public static void initResources() {
        final AssetProvider assets =
                new AssetProviderImpl(ResourcesConfig.getAssetHoldersFileNames());
        dummiesFactory = new DummiesFactory(assets);
        pendingLootPool = new PendingLootPoolImpl();
    }

    @Test
    public void pvePollCreationTest() {
        final CharactersParty party = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacter =
                    dummiesFactory.makeNewDummy(0, "character" + i, "");
            assertTrue(party.addMember(i, Objects.requireNonNull(userCharacter)));
        }
        assertEquals(party.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(party.isFull());

        final Poll pvePoll = new PvePoll(party);

        assertFalse(pvePoll.isExpired());
        assertFalse(pvePoll.isCanceled());
        assertFalse(pvePoll.isReady());
        assertEquals(GameModes.GM_COOP_PVE, pvePoll.getGameMode().intValue());
    }

    @Test
    public void pvePollCancelTest() {
        final CharactersParty party = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacter =
                    dummiesFactory.makeNewDummy(0, "character" + i, "");
            assertTrue(party.addMember(i, Objects.requireNonNull(userCharacter)));
        }
        assertEquals(party.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(party.isFull());

        final Poll pvePoll = new PvePoll(party);
        assertFalse(pvePoll.isCanceled());
        final Random random = new Random(System.currentTimeMillis());

        final List<Integer> partyRolesList = new ArrayList<>(party.getRoleIds());
        final Integer leaverRoleId = partyRolesList.get(random.nextInt(partyRolesList.size()));
        Objects.requireNonNull(pvePoll.getPartyAnswers(Squad.PLAYERS_SQUAD_ID))
                .get(leaverRoleId).setStatus(Invitation.VS_CANCEL);
        assertTrue(pvePoll.isCanceled());
        assertTrue(Objects.requireNonNull(pvePoll.getPartyAnswers(Squad.PLAYERS_SQUAD_ID))
                .get(leaverRoleId).isCancel());
    }

    @Test
    public void pvePollManualExpirationTest() {
        final CharactersParty party = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacter =
                    dummiesFactory.makeNewDummy(0, "character" + i, "");
            assertTrue(party.addMember(i, Objects.requireNonNull(userCharacter)));
        }
        assertEquals(party.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(party.isFull());

        final Poll pvePoll = new PvePoll(party);
        assertFalse(pvePoll.isExpired());
        final Random random = new Random(System.currentTimeMillis());

        final List<Integer> partyRolesList = new ArrayList<>(party.getRoleIds());
        final Integer leaverRoleId = partyRolesList.get(random.nextInt(partyRolesList.size()));
        Objects.requireNonNull(pvePoll.getPartyAnswers(Squad.PLAYERS_SQUAD_ID))
                .get(leaverRoleId).setStatus(Invitation.VS_EXPIRED);
        assertTrue(pvePoll.isExpired());
        assertTrue(Objects.requireNonNull(pvePoll.getPartyAnswers(Squad.PLAYERS_SQUAD_ID))
                .get(leaverRoleId).isExpired());
    }

    @Test
    public void pvePollLoopExpirationTest() {
        final CharactersParty party = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacter =
                    dummiesFactory.makeNewDummy(0, "character" + i, "");
            assertTrue(party.addMember(i, Objects.requireNonNull(userCharacter)));
        }
        assertEquals(party.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(party.isFull());

        final Poll pvePoll = new PvePoll(party);
        assertFalse(pvePoll.isExpired());
        for (Integer i = 0; i <= Invitation.TIMEOUT_LOOPS_COUNT; ++i) {
            pvePoll.update();
        }
        assertTrue(pvePoll.isExpired());
    }

    @Test
    public void pvePollReadyTest() {
        final CharactersParty party = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacter =
                    dummiesFactory.makeNewDummy(0, "character" + i, "");
            assertTrue(party.addMember(i, Objects.requireNonNull(userCharacter)));
        }
        assertEquals(party.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(party.isFull());

        final Poll pvePoll = new PvePoll(party);
        assertFalse(pvePoll.isReady());
        for (Integer roleId : party.getRoleIds()) {
            Objects.requireNonNull(pvePoll.getPartyAnswers(Squad.PLAYERS_SQUAD_ID))
                    .get(roleId).setStatus(Invitation.VS_CONFIRM);
        }
        assertTrue(pvePoll.isReady());
    }

    @Test
    public void squadPvpPollCreationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll squadPvpPoll = new SquadPvpPoll(parties);
        assertFalse(squadPvpPoll.isReady());
        assertFalse(squadPvpPoll.isExpired());
        assertFalse(squadPvpPoll.isCanceled());
        assertEquals(GameModes.GM_SQUAD_PVP, squadPvpPoll.getGameMode().intValue());
    }


    @Test
    public void squadPvpPollManualExpirationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll squadPvpPoll = new SquadPvpPoll(parties);
        assertFalse(squadPvpPoll.isExpired());

        final Random random = new Random(System.currentTimeMillis());
        final Integer teamId = random.nextInt(DigitsPairIndices.PAIR_SIZE);
        final List<Integer> roleIdsList = new ArrayList<>(
                Objects.requireNonNull(squadPvpPoll.getParty(teamId)).getRoleIds());
        final Integer roleId = roleIdsList.get(random.nextInt(roleIdsList.size()));
        Objects.requireNonNull(squadPvpPoll.getPartyAnswers(teamId))
                .get(roleId).setStatus(Invitation.VS_EXPIRED);
        assertTrue(squadPvpPoll.isExpired());
    }

    @Test
    public void squadPvpPollLoopExpirationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll squadPvpPoll = new SquadPvpPoll(parties);
        assertFalse(squadPvpPoll.isExpired());

        for (Integer i = 0; i <= Invitation.TIMEOUT_LOOPS_COUNT; ++i) {
            squadPvpPoll.update();
        }
        assertTrue(squadPvpPoll.isExpired());
    }

    @Test
    public void squadPvpPollCancelTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll squadPvpPoll = new SquadPvpPoll(parties);
        assertFalse(squadPvpPoll.isCanceled());

        final Random random = new Random(System.currentTimeMillis());
        final Integer teamId = random.nextInt(DigitsPairIndices.PAIR_SIZE);
        final List<Integer> roleIdsList = new ArrayList<>(
                Objects.requireNonNull(squadPvpPoll.getParty(teamId)).getRoleIds());
        final Integer roleId = roleIdsList.get(random.nextInt(roleIdsList.size()));
        Objects.requireNonNull(squadPvpPoll.getPartyAnswers(teamId))
                .get(roleId).setStatus(Invitation.VS_CANCEL);
        assertTrue(squadPvpPoll.isCanceled());
    }

    @Test
    public void squadPvpPollReadyTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll squadPvpPoll = new SquadPvpPoll(parties);
        assertFalse(squadPvpPoll.isReady());

        for (Integer partyId : squadPvpPoll.getStatus().keySet()) {
            for (Integer roleId : squadPvpPoll.getStatus().get(partyId).keySet()) {
                Objects.requireNonNull(squadPvpPoll.getPartyAnswers(partyId))
                        .get(roleId).setStatus(Invitation.VS_CONFIRM);
            }
        }
        assertTrue(squadPvpPoll.isReady());
    }

    @Test
    public void coopPvpPollCreationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll coopPvpPoll = new CoopPvpPoll(parties);
        assertFalse(coopPvpPoll.isReady());
        assertFalse(coopPvpPoll.isCanceled());
        assertFalse(coopPvpPoll.isExpired());
        assertEquals(GameModes.GM_COOP_PVP, coopPvpPoll.getGameMode().intValue());
    }


    @Test
    public void coopPvpPollManualExpirationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll coopPvpPoll = new CoopPvpPoll(parties);
        assertFalse(coopPvpPoll.isExpired());

        final Random random = new Random(System.currentTimeMillis());
        final Integer teamId = random.nextInt(DigitsPairIndices.PAIR_SIZE);
        final List<Integer> roleIdsList = new ArrayList<>(
                Objects.requireNonNull(coopPvpPoll.getParty(teamId)).getRoleIds());
        final Integer roleId = roleIdsList.get(random.nextInt(roleIdsList.size()));
        Objects.requireNonNull(coopPvpPoll.getPartyAnswers(teamId))
                .get(roleId).setStatus(Invitation.VS_EXPIRED);
        assertTrue(coopPvpPoll.isExpired());
    }

    @Test
    public void coopPvpPollLoopExpirationTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll coopPvpPoll = new CoopPvpPoll(parties);
        assertFalse(coopPvpPoll.isExpired());

        for (Integer i = 0; i <= Invitation.TIMEOUT_LOOPS_COUNT; ++i) {
            coopPvpPoll.update();
        }
        assertTrue(coopPvpPoll.isExpired());
    }

    @Test
    public void coopPvpPollCancelTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll coopPvpPoll = new CoopPvpPoll(parties);
        assertFalse(coopPvpPoll.isCanceled());

        final Random random = new Random(System.currentTimeMillis());
        final Integer teamId = random.nextInt(DigitsPairIndices.PAIR_SIZE);
        final List<Integer> roleIdsList = new ArrayList<>(
                Objects.requireNonNull(coopPvpPoll.getParty(teamId)).getRoleIds());
        final Integer roleId = roleIdsList.get(random.nextInt(roleIdsList.size()));
        Objects.requireNonNull(coopPvpPoll.getPartyAnswers(teamId))
                .get(roleId).setStatus(Invitation.VS_CANCEL);
        assertTrue(coopPvpPoll.isCanceled());
    }

    @Test
    public void coopPvpPollReadyTest() {
        final Map<Integer, CharactersParty> parties = makeTestPartiesPair();
        final Poll coopPvpPoll = new CoopPvpPoll(parties);
        assertFalse(coopPvpPoll.isReady());

        for (Integer teamId : coopPvpPoll.getStatus().keySet()) {
            for (Integer roleId : Objects.requireNonNull(coopPvpPoll
                    .getPartyAnswers(teamId)).keySet()) {
                Objects.requireNonNull(coopPvpPoll.getPartyAnswers(teamId))
                        .get(roleId).setStatus(Invitation.VS_CONFIRM);
            }
        }
        assertTrue(coopPvpPoll.isReady());
    }

    private @NotNull Map<Integer, CharactersParty> makeTestPartiesPair() {
        final CharactersParty partyOne = new CharactersParty(pendingLootPool);
        final CharactersParty partyTwo = new CharactersParty(pendingLootPool);
        for (Integer i = 0; i < CharacterRoleIds.CR_SIZE; ++i) {
            final AliveEntity userCharacterOne =
                    dummiesFactory.makeNewDummy(
                            0, "characterOne" + i, "");
            assertTrue(partyOne.addMember(i, Objects.requireNonNull(userCharacterOne)));
            final AliveEntity userCharacterTwo =
                    dummiesFactory.makeNewDummy(
                            0, "characterTwo" + i, "");
            assertTrue(partyTwo.addMember(i, Objects.requireNonNull(userCharacterTwo)));
        }
        assertEquals(partyOne.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(partyOne.isFull());
        assertEquals(partyTwo.getPartySize().intValue(), CharacterRoleIds.CR_SIZE);
        assertTrue(partyTwo.isFull());
        final Map<Integer, CharactersParty> parties = new HashMap<>();
        parties.put(Squad.TEAM_ONE_SQUAD_ID, partyOne);
        parties.put(Squad.TEAM_TWO_SQUAD_ID, partyTwo);
        return parties;
    }
}
