package pt.pchouse.pdf.api.generator;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;

@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class Util
{

    /**
     * Parses a string representation of a boolean value into its corresponding boolean representation.
     * Recognized values for true are: "1", "on", "true", and "yes" (case-insensitive).
     * Recognized values for false are: "0", "off", "false", and "no" (case-insensitive).
     * Throws an exception if the string cannot be parsed.
     *
     * @param bool the string representation of the boolean value to parse
     * @return the boolean representation of the input string
     * @throws Exception if the input string is null or cannot be parsed as a boolean
     */
    public static boolean parseBool(String bool) throws Exception {
        if (bool == null) {
            throw new Exception("A null value is not parseble to boolean");
        }
        return switch (bool.toLowerCase()) {
            case "0", "off", "false", "no" -> false;
            case "1", "on", "true", "yes" -> true;
            default -> throw new Exception("Value '" + bool + "' is not parseble to boolean");
        };
    }

    /**
     * Convert enum to String[]
     *
     * @param e Enumeration
     * @return Enumeration name as string array
     * @see <a href="https://stackoverflow.com/questions/13783295/getting-all-names-in-an-enum-as-a-string/13783744#13783744">...</a>
     */
    public static String[] enumToArray(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    /**
     * Checks if a given string is either null or blank.
     *
     * @param str the string to check for null or blank
     * @return true if the string is null or contains only whitespace; false otherwise
     */
    public static boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

}
