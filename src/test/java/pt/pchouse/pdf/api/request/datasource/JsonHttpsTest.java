package pt.pchouse.pdf.api.request.datasource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JsonHttpsTest
{

    /**
     * Tests for the equals method in the JsonHttps class.
     * The equals method is overridden to compare JsonHttps instances.
     * It ensures that two objects are considered equal only when they belong to the same class
     * and have the same properties as the superclass.
     */

    @Test
    void testEquals_SameInstance() {
        // Arrange
        JsonHttps jsonHttps = new JsonHttps();

        // Act & Assert
        assertEquals(jsonHttps, jsonHttps, "An object should be equal to itself.");
    }

    @Test
    void testEquals_NullObject() {
        // Arrange
        JsonHttps jsonHttps = new JsonHttps();

        // Act & Assert
        assertNotEquals(null, jsonHttps, "A JsonHttps object should not be equal to null.");
    }

    @Test
    void testEquals_DifferentClass() {
        // Arrange
        JsonHttps jsonHttps   = new JsonHttps();
        Object    otherObject = new Object();

        // Act & Assert
        assertNotEquals(jsonHttps, otherObject, "A JsonHttps object should not be equal to an instance of a different class.");
    }

    @Test
    void testEquals_DifferentInstanceSameClass() {
        // Arrange
        JsonHttps jsonHttps1 = new JsonHttps();
        JsonHttps jsonHttps2 = new JsonHttps();

        // Act & Assert
        assertEquals(jsonHttps1, jsonHttps2, "Two distinct JsonHttps objects should be equal if superclass equality is satisfied.");
    }

    @Test
    void testEquals_ChildClassInstance() {
        // Arrange
        JsonHttps         jsonHttps     = new JsonHttps();
        IRemoteDatasource childInstance = new JsonHttps();

        // Act & Assert
        assertEquals(jsonHttps, childInstance, "JsonHttps should equal its child instances when properties match.");
    }

    @Test
    public void testHashCode_DifferentInstanceWithDifferentData() {
        // Given
        JsonHttps jsonHttps1 = new JsonHttps();
        jsonHttps1.setUrl("http://www.example.com");
        JsonHttps jsonHttps2 = new JsonHttps();
        jsonHttps2.setUrl("http://www.example.com/different");

        assertNotEquals(
                jsonHttps1.hashCode(),
                jsonHttps2.hashCode(),
                "The hashCode method should return false for different instances with potentially different data."
        );
    }

}