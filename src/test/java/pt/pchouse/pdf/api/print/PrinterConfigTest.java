package pt.pchouse.pdf.api.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "name=TestPrinter",
        "init=1,2,3",
        "feed=10,20,30",
        "cut=100,110",
        "cash_drawer=5,55"
})
public class PrinterConfigTest
{

    @Autowired
    private PrinterConfig printerConfig;

    @BeforeEach
    void setUp() {
        printerConfig.setInit("1,2,3");
        printerConfig.setFeed("10,20,30");
        printerConfig.setCut("100,110");
        printerConfig.setCashDrawer("5,55");
    }


    /**
     * Test case to validate the correct transformation of `init` property into bytes
     */
    @Test
    void testGetInitBytesValidInput() {
        byte[] expectedBytes = {1, 2, 3};
        byte[] actualBytes   = printerConfig.getInitBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The byte array for 'init' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate behavior with malformed `init` property value that is non-numeric
     */
    @Test
    void testGetInitBytesInvalidInput() {
        printerConfig.setInit("abc,2,3");
        assertThrows(NumberFormatException.class, printerConfig::getInitBytes,
                "Expected NumberFormatException when parsing non-numeric init values.");
    }

    /**
     * Test case to validate behavior with an empty `init` property
     */
    @Test
    void testGetInitBytesEmptyInput() {
        printerConfig.setInit("");
        byte[] expectedBytes = {};
        byte[] actualBytes   = printerConfig.getInitBytes();
        assertArrayEquals(expectedBytes, actualBytes, "The byte array for empty 'init' does not match expected result.");
    }

    /**
     * Test case to validate `getInitBytes` with a single byte
     */
    @Test
    void testGetInitBytesSingleByteInput() {
        printerConfig.setInit("42");
        byte[] expectedBytes = {42};
        byte[] actualBytes   = printerConfig.getInitBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The single byte array for 'init' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }
    

    /**
     * Test case to validate the correct transformation of `feed` property into bytes
     */
    @Test
    void testGetFeedBytesValidInput() {
        byte[] expectedBytes = {10, 20, 30};
        byte[] actualBytes   = printerConfig.getFeedBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The byte array for 'feed' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate behavior with malformed `feed` property value that is non-numeric
     */
    @Test
    void testGetFeedBytesInvalidInput() {
        printerConfig.setFeed("abc,20,30");
        assertThrows(NumberFormatException.class, printerConfig::getFeedBytes,
                "Expected NumberFormatException when parsing non-numeric feed values.");
    }

    /**
     * Test case to validate behavior with an empty `feed` property
     */
    @Test
    void testGetFeedBytesEmptyInput() {
        printerConfig.setFeed("");
        byte[] expectedBytes = {};
        byte[] actualBytes   = printerConfig.getFeedBytes();
        assertArrayEquals(expectedBytes, actualBytes, "The byte array for empty 'feed' does not match expected result.");
    }

    /**
     * Test case to validate `getFeedBytes` with a single byte
     */
    @Test
    void testGetFeedBytesSingleByteInput() {
        printerConfig.setFeed("42");
        byte[] expectedBytes = {42};
        byte[] actualBytes   = printerConfig.getFeedBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The single byte array for 'feed' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate the correct transformation of `cut` property into bytes
     */
    @Test
    void testGetCutBytesValidInput() {
        byte[] expectedBytes = {100, 110};
        byte[] actualBytes   = printerConfig.getCutBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The byte array for 'cut' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate behavior with malformed `cut` property value that is non-numeric
     */
    @Test
    void testGetCutBytesInvalidInput() {
        printerConfig.setCut("abc,110");
        assertThrows(NumberFormatException.class, printerConfig::getCutBytes,
                "Expected NumberFormatException when parsing non-numeric cut values.");
    }

    /**
     * Test case to validate behavior with an empty `cut` property
     */
    @Test
    void testGetCutBytesEmptyInput() {
        printerConfig.setCut("");
        byte[] expectedBytes = {};
        byte[] actualBytes   = printerConfig.getCutBytes();
        assertArrayEquals(expectedBytes, actualBytes, "The byte array for empty 'cut' does not match expected result.");
    }

    /**
     * Test case to validate `getCutBytes` with a single byte
     */
    @Test
    void testGetCutBytesSingleByteInput() {
        printerConfig.setCut("42");
        byte[] expectedBytes = {42};
        byte[] actualBytes   = printerConfig.getCutBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The single byte array for 'cut' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate the correct transformation of `cash_drawer` property into bytes
     */
    @Test
    void testGetCashDrawerBytesValidInput() {
        byte[] expectedBytes = {5, 55};
        byte[] actualBytes   = printerConfig.getCashDrawerBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The byte array for 'cash_drawer' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

    /**
     * Test case to validate behavior with malformed `cash_drawer` property value that is non-numeric
     */
    @Test
    void testGetCashDrawerBytesInvalidInput() {
        printerConfig.setCashDrawer("abc,55");
        assertThrows(NumberFormatException.class, printerConfig::getCashDrawerBytes,
                "Expected NumberFormatException when parsing non-numeric cash_drawer values.");
    }

    /**
     * Test case to validate behavior with an empty `cash_drawer` property
     */
    @Test
    void testGetCashDrawerBytesEmptyInput() {
        printerConfig.setCashDrawer("");
        byte[] expectedBytes = {};
        byte[] actualBytes   = printerConfig.getCashDrawerBytes();
        assertArrayEquals(expectedBytes, actualBytes, "The byte array for empty 'cash_drawer' does not match expected result.");
    }

    /**
     * Test case to validate `getCashDrawerBytes` with a single byte
     */
    @Test
    void testGetCashDrawerBytesSingleByteInput() {
        printerConfig.setCashDrawer("42");
        byte[] expectedBytes = {42};
        byte[] actualBytes   = printerConfig.getCashDrawerBytes();
        assertArrayEquals(
                expectedBytes,
                actualBytes,
                String.format(
                        "The single byte array for 'cash_drawer' %s does not match expected result %s.",
                        Arrays.toString(actualBytes),
                        Arrays.toString(expectedBytes)
                )
        );
    }

}