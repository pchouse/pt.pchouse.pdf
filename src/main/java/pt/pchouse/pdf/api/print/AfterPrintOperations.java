package pt.pchouse.pdf.api.print;

/**
 * Enumeration representing the operations that can be performed
 * immediately after printing.
 *
 * @since 1.0.0
 */
public enum AfterPrintOperations
{
    /**
     * Represents an operation to cut the paper immediately after the completion of a print job.
     * This operation is typically performed by devices such as receipt or label printers
     * to prepare the printer for the next print job.
     *
     * @since 1.0.0
     */
    AFTER_PRINT_CUT_PAPER,

    /**
     * Represents an operation to open the cash drawer immediately after the completion of a print job.
     * This operation is typically performed by devices that support cash drawer functionality,
     * such as point-of-sale systems, to facilitate transactions.
     *
     * @since 1.0.0
     */
    AFTER_PRINT_OPEN_CASH_DRAWER,
}
