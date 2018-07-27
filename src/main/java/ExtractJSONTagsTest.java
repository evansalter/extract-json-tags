import org.junit.Test;
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

}
