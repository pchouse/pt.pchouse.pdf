package pt.pchouse.pdf.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParameterTest
{

    /**
     * Tests for the getValue() method of the Parameter class. The method
     * is responsible for returning the correct value based on the type
     * of the parameter. Special conditions apply for types P_BOOL and
     * P_BOOLEAN which translate specific string inputs to "true".
     */

    @Test
    void testGetValueReturnsOriginalValueForNonBooleanType() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_STRING);
        parameter.setValue("someValue");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("someValue", result);
    }

    @Test
    void testGetValueReturnsTrueForBooleanTypeWithValueOne() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_BOOL);
        parameter.setValue("1");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("true", result);
    }

    @Test
    void testGetValueReturnsTrueForBooleanTypeWithValueYes() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_BOOLEAN);
        parameter.setValue("Yes");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("true", result);
    }

    @Test
    void testGetValueReturnsTrueForBooleanTypeWithValueOn() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_BOOL);
        parameter.setValue("on");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("true", result);
    }

    @Test
    void testGetValueReturnsOriginalValueForBooleanTypeWithOtherValues() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_BOOLEAN);
        parameter.setValue("false");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("false", result);
    }

    @Test
    void testGetValueReturnsOriginalValueWhenTypeIsNull() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setValue("123");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("123", result);
    }

    @Test
    void testGetValueReturnsOriginalValueForEmptyString() {
        // Arrange
        Parameter parameter = new Parameter();
        parameter.setType(Parameter.Types.P_STRING);
        parameter.setValue("");

        // Act
        String result = parameter.getValue();

        // Assert
        assertEquals("", result);
    }

}