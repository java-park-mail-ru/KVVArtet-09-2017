package project.gamemechanics;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.gamemechanics.components.affectors.Affector;
import project.gamemechanics.components.properties.Property;
import project.gamemechanics.components.properties.PropertyCategories;
import project.gamemechanics.components.properties.SingleValueProperty;
import project.gamemechanics.globals.Constants;
import project.gamemechanics.resources.pcg.items.ItemPart;
import project.gamemechanics.resources.pcg.items.ItemPartAsset;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.*;

public class AssetsSerializationTest {
    @Test
    public void itemPartStringSerializationDeserialization() {
        final Random random = new Random(System.currentTimeMillis());
        final Map<Integer, Property> testProperty = new HashMap<>();
        testProperty.put(PropertyCategories.PC_LEVEL, new SingleValueProperty(Constants.START_LEVEL));
        final Map<Integer, Affector> testAffectors = new HashMap<>();
        final ItemPart itemPart = new ItemPartAsset(0,"test asset", "test description",
                ItemPart.FIRST_PART_ID, testAffectors, testProperty);
        final ObjectMapper mapper = new ObjectMapper();
        String jsonAsset = null;
        try {
            jsonAsset = mapper.writeValueAsString(itemPart);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertNotEquals("data shall be written", jsonAsset, null);
        assertFalse("serialized json shall contain data", jsonAsset.isEmpty());
        ItemPart deserialized = null;
        //noinspection TryWithIdenticalCatches
        try {
            deserialized = mapper.readValue(jsonAsset, ItemPartAsset.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String secondJson = null;
        try {
            secondJson = mapper.writeValueAsString(deserialized);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals("serialized data must be equal", jsonAsset, secondJson);

        assertEquals(itemPart.getName(), deserialized.getName());
        assertEquals(itemPart.getDescription(), deserialized.getDescription());
        assertEquals(itemPart.getAllAffectors(), deserialized.getAllAffectors());
        assertEquals(itemPart.getProperty(PropertyCategories.PC_LEVEL),
                deserialized.getProperty(PropertyCategories.PC_LEVEL));
        System.out.println(jsonAsset);
        System.out.println(secondJson);
    }
}
