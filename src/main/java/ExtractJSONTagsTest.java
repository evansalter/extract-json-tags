import com.google.gson.JsonObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ExtractJSONTagsTest {

    public static class ExtractJSONTagTest {

        @Test
        public void testReturnsNullIfNoJSONTag() {
            String input = "no json tag";
            String actual = new ExtractJSONTags().extractJSONTag(input);
            assertNull(actual);
        }

        @Test
        public void testReturnsTagIfGivenSingleTag() {
            String input = "FirstName `json:\"first_name\"`";
            String expected = "first_name";
            String actual = new ExtractJSONTags().extractJSONTag(input);
            assertEquals(expected, actual);
        }

        @Test
        public void testReturnsTagIfGivenMultipleTags() {
            String input = "FirstName `other:\"something\" json:\"first_name\"`";
            String expected = "first_name";
            String actual = new ExtractJSONTags().extractJSONTag(input);
            assertEquals(expected, actual);
        }

        @Test
        public void testReturnsTagIfMultipleCommaSeparatedValuesInJSONTag() {
            String input = "FirstName `json:\"first_name,something\"`";
            String expected = "first_name";
            String actual = new ExtractJSONTags().extractJSONTag(input);
            assertEquals(expected, actual);
        }

        @Test
        public void testReturnsTagIfMultipleSpaceSeparatedValuesInJSONTag() {
            String input = "FirstName `json:\"first_name something\"`";
            String expected = "first_name";
            String actual = new ExtractJSONTags().extractJSONTag(input);
            assertEquals(expected, actual);
        }

    }

    public static class constructJSONObjectTest {

        @Test
        public void testReturnsEmptyObjectIfPassInEmptyArray() {
            ArrayList<String> input = new ArrayList<>();
            JsonObject expected = new JsonObject();
            JsonObject actual = new ExtractJSONTags().constructJSONObject(input);
            assertEquals(expected, actual);
        }

        @Test
        public void testReturnsObjectWithTagsAsKeysAndEmptyStringValues() {
            ArrayList<String> input = new ArrayList<>();
            input.add("FirstName   `json:\"first_name\"`");
            input.add("LastName    `json:\"last_name\"`");
            JsonObject expected = new JsonObject();
            expected.addProperty("first_name", "");
            expected.addProperty("last_name", "");
            JsonObject actual = new ExtractJSONTags().constructJSONObject(input);
            assertEquals(expected, actual);
        }

        @Test
        public void testHandlesDuplicateInput() {
            ArrayList<String> input = new ArrayList<>();
            input.add("FirstName   `json:\"first_name\"`");
            input.add("FirstName   `json:\"first_name\"`");
            JsonObject expected = new JsonObject();
            expected.addProperty("first_name", "");
            JsonObject actual = new ExtractJSONTags().constructJSONObject(input);
            assertEquals(expected, actual);
        }

    }
}
