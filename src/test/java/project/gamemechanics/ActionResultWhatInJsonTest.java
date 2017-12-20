package project.gamemechanics;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.gamemechanics.aliveentities.AbstractAliveEntity;
import project.gamemechanics.aliveentities.UserCharacter;
import project.gamemechanics.battlefield.Tile;
import project.gamemechanics.battlefield.actionresults.ActionResult;
import project.gamemechanics.battlefield.actionresults.BattleActionResult;
import project.gamemechanics.battlefield.actionresults.events.EventsFactory;
import project.gamemechanics.battlefield.actionresults.events.TurnEvent;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.effects.IngameEffect;
import project.gamemechanics.flyweights.CharacterClass;
import project.gamemechanics.flyweights.CharacterRace;
import project.gamemechanics.flyweights.PerkBranch;
import project.gamemechanics.flyweights.abilities.AbilityBehaviors;
import project.gamemechanics.flyweights.abilities.IngameAbility;
import project.gamemechanics.interfaces.Ability;
import project.gamemechanics.interfaces.Bag;
import project.gamemechanics.interfaces.MapNode;
import project.gamemechanics.items.containers.CharacterDoll;
import project.gamemechanics.items.containers.StorageBag;
import project.server.dao.UserDao;
import project.server.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings({"MagicNumber", "RedundantSuppression"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class ActionResultWhatInJsonTest {
    @Autowired
    private UserDao dao;
    @SuppressWarnings({"unused", "ConstantConditions"})
    @Test
    public void actionResultInJson() {
        final User newUser1 = new User("testname1", "testemail1@mail.ru", "testpassword");
        final User user1 = dao.setUser(newUser1);
        final User newUser2 = new User("testname2", "testemail2@mail.ru", "testpassword");
        final User user2 = dao.setUser(newUser2);

        final List<Bag> mockBags = new ArrayList<>();
        final StorageBag.EmptyBagModel emptyBagModel = new StorageBag.EmptyBagModel("testbag", "something inside", 10);
        final Bag storageBag = new StorageBag(emptyBagModel);
        mockBags.add(storageBag);

        final Map<Integer, Ability> abilityMap = new HashMap<>();
        final Map<Integer, PerkBranch> perkBranchMap = new HashMap<>();
        final Map<Integer, Property> propertys = new HashMap<>();
        final CharacterClass.CharacterClassModel class1 = new CharacterClass.CharacterClassModel(0, "Warrior", "Cool man", abilityMap, perkBranchMap, propertys);
        final CharacterClass characterClass = new CharacterClass(class1);

        final Map<Integer, Affector> affectorMap = new HashMap<>();
        final CharacterRace.CharacterRaceModel humanModel = new CharacterRace.CharacterRaceModel(0, "Human", "Humans better then everyone. Always.", affectorMap);
        final CharacterRace human = new CharacterRace(humanModel);

        final CharacterDoll characterDoll = new CharacterDoll();
        final Map<Integer, Map<Integer, Integer>> perkRanks = new HashMap<>();
        final AbstractAliveEntity.UserCharacterModel user1Model = new AbstractAliveEntity.UserCharacterModel(user1.getId(),"name1", "this is name1!",propertys, mockBags, characterClass, human, characterDoll, perkRanks);
        final UserCharacter char1 = new UserCharacter(user1Model);

        final AbstractAliveEntity.UserCharacterModel user2Model = new AbstractAliveEntity.UserCharacterModel(user2.getId(), "name2", "this is name2!",propertys, mockBags, characterClass, human, characterDoll, perkRanks);
        final UserCharacter char2 = new UserCharacter(user2Model);

        final MapNode tile1 = new Tile(true, 1, 1);
        tile1.occupy(char1);

        final MapNode tile2 = new Tile(true, 2, 2);
        tile2.occupy(char2);

        final List<IngameEffect.EffectModel>  effectModels= new ArrayList<>();

        final IngameAbility.AbilityModel abilityModel = new IngameAbility.AbilityModel(0, "Strong strike", "Really strong!!", propertys, affectorMap, effectModels,AbilityBehaviors.getBehavior(null));
        final Ability ability = new IngameAbility(abilityModel);

        final List<TurnEvent> turnEvents = new ArrayList<>();
        turnEvents.add(EventsFactory.makeHitpointsChangeEvent(tile2, 50));

        final ActionResult actionResult = new BattleActionResult(0, tile1, tile2, ability, turnEvents);

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            System.out.println(objectMapper.writeValueAsString(actionResult));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
