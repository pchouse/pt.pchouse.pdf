package pt.pchouse.pdf.api.generator;

/**
 * Represents an exception that occurs during the parsing process.
 * This exception is intended to signal errors when attempting to convert
 * or interpret a given input into a valid format or representation.
 * <p>
 * The message passed to this exception provides additional context
 * regarding the specific parsing error that occurred.
 *
 * @since 1.0.0
 */
public class ParseException extends Exception
{
    /**
     * Constructs a new ParseException with the specified detail message.
     * The message provides details about the specific parsing error encountered.
     *
     * @param message the detail message explaining the cause of the parsing error
     *
     * @since 1.0.0
     */
    public ParseException(String message)
    {
        super(message);
    }
}
