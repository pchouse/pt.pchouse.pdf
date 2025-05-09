/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package pt.pchouse.pdf.api.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pt.pchouse.pdf.api.generator.Util;

import javax.print.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class Printer
{

    /**
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @since 1.0.0
     */
    @Autowired
    private PrinterConfig printerConfig;

    /**
     * Retrieves a {@code PrintService} instance corresponding to the given printer name.
     * If the printer name provided is null or blank, the method will use the default printer name
     * from the configuration.
     *
     * @param printerName The name of the printer to retrieve the print service for. If null or blank,
     *                    the default printer configured will be used.
     * @return The {@code PrintService} associated with the specified printer name.
     * @throws PrinterException If the specified printer is not found or unavailable in the system.
     *
     * @since 1.0.0
     */
    public PrintService getPrintService(String printerName) throws PrinterException {

        String printer = Util.isNullOrBlank(printerName) ? printerConfig.getName() : printerName;

        Optional<PrintService> optional = Arrays.stream(PrintServiceLookup
                        .lookupPrintServices(null, null))
                .filter(prtService -> prtService.getName().equals(printer))
                .findFirst();

        if (optional.isEmpty()) {
            String msg = String.format(
                    "Printer with name '%s' not exist, if exists please restart the APi Reports service",
                    printer
            );
            logger.error(msg);
            throw new PrinterException(msg);
        }

        logger.debug("Printer service set to printer name '{}'", printerConfig.getName());

        return optional.get();

    }

    /**
     * Sends the initialization and line feed ESC/POS commands to the specified printer
     * to feed the paper.
     *
     * @param printService The print service representing the target printer.
     * @throws PrintException If an error occurs during the printing process.
     */
    public void feedPaper(PrintService printService) throws PrintException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getFeedBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );
        printService.createPrintJob().print(doc, null);
    }

    /**
     * Sends the paper cut command to the specified printer service.
     *
     * @param printService The print service representing the target printer.
     * @throws PrintException   If an error occurs during the printing process.
     *
     * @since 1.0.0
     */
    public void cutPaper(PrintService printService) throws PrintException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCutBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );
        printService.createPrintJob().print(doc, null);
    }

    /**
     * Sends a command to the specified printer to trigger the cash drawer opening via ESC/POS commands.
     *
     * @param printService The print service representing the target printer.
     * @throws PrintException   If an error occurs during the printing process.
     *
     * @since 1.0.0
     */
    public void cashDrawer(PrintService printService) throws PrintException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCashDrawerBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );

        printService.createPrintJob().print(doc, null);
    }

    /**
     * Sends a combined sequence of ESC/POS commands to the specified printer service, which includes:
     * - Initialization commands
     * - Paper cut commands
     * - Cash drawer open commands
     *
     * @param printService The system print service representing the target printer.
     * @throws PrintException   If an error occurs during the printing process.
     * @throws PrinterException If the specified printer cannot be found or the printer service is unavailable.
     *
     * @since 1.0.0
     */
    public void cutAndCashDrawer(PrintService printService) throws PrintException, PrinterException {
        String esc = new String(printerConfig.getInitBytes());
        esc += new String(printerConfig.getCutBytes());
        esc += new String(printerConfig.getCashDrawerBytes());
        Doc doc = new SimpleDoc(
                esc.getBytes(),
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );

        printService.createPrintJob().print(doc, null);
    }

    /**
     * Sends a byte array of data to the specified printer service for printing.
     *
     * @param printService The system print service.
     * @param bytes       The byte array containing the data to print.
     * @throws PrintException   If an error occurs during the printing process.
     * @throws PrinterException If the specified printer cannot be found or the printer service is unavailable.
     * @since 1.0.0
     */
    public void print(PrintService printService, byte[] bytes) throws PrintException, PrinterException {
        Doc doc = new SimpleDoc(
                bytes,
                DocFlavor.BYTE_ARRAY.AUTOSENSE,
                null
        );

        printService.createPrintJob().print(doc, null);
    }

    /**
     * Sends a base64-encoded string of data to the specified printer service for printing.
     *
     * @param printService The system print service.
     * @param base64      The base64-encoded string containing the data to print.
     * @throws PrintException   If an error occurs during the printing process.
     * @throws PrinterException If the specified printer cannot be found or the printer service is unavailable.
     * @since 1.0.0
     */
    public void print(PrintService printService, String base64) throws PrintException, PrinterException {
        print(
                printService,
                java.util.Base64.getDecoder().decode(base64)
        );
    }
}
