package pt.pchouse.pdf.api.print;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import pt.pchouse.pdf.api.generator.Util;

/**
 * Configuration class for printer settings.
 * This class loads configurations such as printer commands for operations like initialization,
 * line feeding, paper cutting, and operating the cash drawer from external property files.
 * <p>
 * Property sources:
 * - `classpath:defaultprinter.properties`: A default printer configuration file located in the classpath.
 * - `file:${apphome}/printer.properties`: An optional printer configuration file located in an external directory
 *   defined by the `app.home` property. This file is ignored if not found.
 * <p>
 * The class provides methods to access and manage the ESC/POS commands specified in configuration files.
 * It also facilitates converting these commands from string representation into byte arrays.
 *
 * @since 1.0.0
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:defaultprinter.properties"),
        @PropertySource(value = "file:${apphome}/printer.properties", ignoreResourceNotFound = true)
})
public class PrinterConfig
{

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The ESC/POS byte separator int the config file
     * @since 1.0.0
     */
    public final static String SEPARATOR = ",";

    /**
     * The printer name
     * @since 1.0.0
     */
    @Value("${name}")
    private String name;

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     * @since 1.0.0
     */
    @Value("${init}")
    private String init;

    /**
     * The printer initialization ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _init;

    /**
     * The ESC/POS command from configuration file
     * To line feed
     * @since 1.0.0
     */
    @Value("${feed}")
    private String feed;

    /**
     * The line feed ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _feed;

    /**
     * The ESC/POS command from configuration file
     * To cut paper
     * @since 1.0.0
     */
    @Value("${cut}")
    private String cut;

    /**
     * The cut paper ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _cut;

    /**
     * The ESC/POS command from configuration file
     * To cash drawer
     * @since 1.0.0
     */
    @Value("${cash_drawer}")
    private String cashDrawer;

    /**
     * The cash drawer ESC/POS command as byte array
     * @since 1.0.0
     */
    private static byte[] _cashDrawer;

    /**
     * The default constructor for the PrinterConfig class.
     * Initializes a new instance of the PrinterConfig class
     * and logs the creation of the instance for debugging purposes.
     *
     * @since 1.0.0
     */
    public PrinterConfig() {
        logger.debug("New instance of {}", getClass().getName());
    }

    /**
     * Get the get printer name
     *
     * @return The printer name
     * @since 1.0.0
     */
    public String getName() {
        return name;
    }

    /**
     * Set the printer name
     *
     * @param name The printer name
     * @since 1.0.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    public String getInit() {
        return init;
    }

    /**
     * The ESC/POS command from configuration file
     * To initialize the printer
     *
     * @param init The codes separated by a comma
     * @since 1.0.0
     */
    public void setInit(String init) {
        _init = null;
        this.init = init;
    }

    /**
     * The ESC/POS command from configuration file
     * To line feed
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public String getFeed() {
        return feed;
    }

    /**
     * The ESC/POS command from configuration file
     * To line feed
     *
     * @param feed The codes separated by a comma
     * @since 1.0.0
     */
    public void setFeed(String feed) {
        _feed = null;
        this.feed = feed;
    }

    /**
     * The ESC/POS command from configuration file
     * To cut the paper
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public String getCut() {
        return cut;
    }

    /**
     * The ESC/POS command from configuration file
     * To cut the paper
     *
     * @param cut The codes separated by a comma
     * @since 1.0.0
     */
    public void setCut(String cut) {
        _cut = null;
        this.cut = cut;
    }

    /**
     * The ESC/POS command from configuration file
     * To open cash drawer
     *
     * @return The codes separated by a comma
     * @since 1.0.0
     */
    @SuppressWarnings("unused")
    public String getCashDrawer() {
        return cashDrawer;
    }

    /**
     * The ESC/POS command from configuration file
     * To open the cash drawer
     *
     * @param cashDrawer The codes separated by a comma
     * @since 1.0.0
     */
    public void setCashDrawer(String cashDrawer) {
        _cashDrawer = null;
        this.cashDrawer = cashDrawer;
    }

    /**
     * The printer initialization ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getInitBytes() {
        if (_init == null) {

            if(Util.isNullOrBlank(init)){
                return new byte[0];
            }

            String[] initSplit = init.split(SEPARATOR);
            _init = new byte[initSplit.length];
            int index = 0;
            for (String code : initSplit) {
                _init[index++] = Byte.parseByte(code);
            }
        }
        return _init;
    }

    /**
     * The line feed ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getFeedBytes() {
        if (_feed == null) {

            if(Util.isNullOrBlank(feed)){
                return new byte[0];
            }

            String[] feedSplit = feed.split(SEPARATOR);
            _feed = new byte[feedSplit.length];
            int index = 0;
            for (String code : feedSplit) {
                _feed[index++] = Byte.parseByte(code);
            }
        }
        return _feed;
    }

    /**
     * The paper cut ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getCutBytes() {
        if (_cut == null) {

            if(Util.isNullOrBlank(cut)){
                return new byte[0];
            }

            String[] cutSplit = cut.split(SEPARATOR);
            _cut = new byte[cutSplit.length];
            int index = 0;
            for (String code : cutSplit) {
                _cut[index++] = Byte.parseByte(code);
            }
        }
        return _cut;
    }

    /**
     * The cash drawer ESC/POS command as byte array
     *
     * @return The code
     * @since 1.0.0
     */
    public byte[] getCashDrawerBytes() {
        if (_cashDrawer == null) {

            if(Util.isNullOrBlank(cashDrawer)){
                return new byte[0];
            }

            String[] cashDrawerSplit = cashDrawer.split(SEPARATOR);
            _cashDrawer = new byte[cashDrawerSplit.length];
            int index = 0;
            for (String code : cashDrawerSplit) {
                _cashDrawer[index++] = Byte.parseByte(code);
            }
        }
        return _cashDrawer;
    }

}
