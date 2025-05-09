package pt.pchouse.pdf.api.request.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class JsonHttpTest
{

    /**
     * Tests for the equals method in JsonHttp class.
     * The equals method checks for equality between JsonHttp objects.
     * It compares the current instance with the provided object based on the inherited attributes from
     * the AServer class since JsonHttp itself does not add new properties.
     */

    @Test
    public void testEquals_SameInstance() {
        // Given
        JsonHttp jsonHttp = new JsonHttp();

        // When & Then
        assertEquals(jsonHttp, jsonHttp, "The equals method should return true for the same instance.");
    }

    @Test
    public void testEquals_NullComparison() {
        // Given
        JsonHttp jsonHttp = new JsonHttp();

        // When & Then
        assertNotEquals(null, jsonHttp, "The equals method should return false when comparing with null.");
    }

    @Test
    public void testEquals_DifferentClass() {
        // Given
        JsonHttp jsonHttp    = new JsonHttp();
        Object   otherObject = new Object();

        // When & Then
        assertNotEquals(jsonHttp, otherObject, "The equals method should return false when comparing with an object of a different class.");
    }

    @Test
    public void testEquals_DifferentInstanceWithSameData() {
        // Given
        JsonHttp jsonHttp1 = new JsonHttp();
        JsonHttp jsonHttp2 = new JsonHttp();

        // When & Then
        assertEquals(jsonHttp1, jsonHttp2, "The equals method should return true for different instances with the same data.");
    }

    @Test
    public void testEquals_DifferentInstanceWithDifferentData() {
        // Given
        JsonHttp jsonHttp1 = new JsonHttp();
        jsonHttp1.setUrl("http://www.example.com");
        JsonHttp jsonHttp2 = new JsonHttp();
        jsonHttp2.setUrl("http://www.example.com/different");

        // When & Then
        assertNotEquals(jsonHttp1, jsonHttp2, "The equals method should return false for different instances with potentially different data.");
    }

    @Test
    public void testHashCode_DifferentInstanceWithDifferentData() {
        // Given
        JsonHttp jsonHttp1 = new JsonHttp();
        jsonHttp1.setUrl("http://www.example.com");
        JsonHttp jsonHttp2 = new JsonHttp();
        jsonHttp2.setUrl("http://www.example.com/different");

        assertNotEquals(
                jsonHttp1.hashCode(),
                jsonHttp2.hashCode(),
                "The hashCode method should return false for different instances with potentially different data."
        );
    }

}