package pt.pchouse.pdf.api.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.print.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PrinterTest
{

    private final String init = "A999";
    private final String feed = "B999";
    private final String cut  = "C999";
    private final String cashDrawer = "D999";

    @Mock
    PrinterConfig mockPrinterConfig;

    @InjectMocks()
    @Autowired
    Printer printer;

    @BeforeEach
    public void resetPrinterConfig() {
        MockitoAnnotations.openMocks(this);
        when(mockPrinterConfig.getInitBytes()).thenReturn(init.getBytes());
        when(mockPrinterConfig.getFeedBytes()).thenReturn(feed.getBytes());
        when(mockPrinterConfig.getCutBytes()).thenReturn(cut.getBytes());
        when(mockPrinterConfig.getCashDrawerBytes()).thenReturn(cashDrawer.getBytes());
    }

    @Test
    void testGetPrintService_WithValidPrinterName() throws PrinterException {

        String       printerName      = "PDF";
        PrintService mockPrintService = mock(PrintService.class);
        when(mockPrintService.getName()).thenReturn(printerName);

        PrintServiceLookup.registerServiceProvider(
                new CustomPrintServiceLookup(new PrintService[]{mockPrintService})
        );

        PrintService result = printer.getPrintService(printerName);

        assertNotNull(result);
        assertEquals(printerName, result.getName());
    }

    @Test
    void testFeedPaper() throws IllegalArgumentException, PrintException, IOException {

        PrintService mockPrintService = mock(PrintService.class);
        DocPrintJob  mockDocPrintJob  = mock(DocPrintJob.class);
        when(mockPrintService.createPrintJob()).thenReturn(mockDocPrintJob);

        Doc expectedDoc = new SimpleDoc((init + feed).getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

        printer.feedPaper(mockPrintService);

        verify(mockPrintService).createPrintJob();

        ArgumentCaptor<Doc> docCaptor = ArgumentCaptor.forClass(Doc.class);
        verify(mockDocPrintJob).print(docCaptor.capture(), isNull());

        assertEquals(expectedDoc.getDocFlavor(), docCaptor.getValue().getDocFlavor());
        assertArrayEquals((byte[]) expectedDoc.getPrintData(), (byte[]) docCaptor.getValue().getPrintData());
        assertEquals(expectedDoc.getAttributes(), docCaptor.getValue().getAttributes());
    }

    @Test
    void testCutPaper() throws IllegalArgumentException, PrintException, IOException {

        PrintService mockPrintService = mock(PrintService.class);
        DocPrintJob  mockDocPrintJob  = mock(DocPrintJob.class);
        when(mockPrintService.createPrintJob()).thenReturn(mockDocPrintJob);

        Doc expectedDoc = new SimpleDoc((init + cut).getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

        printer.cutPaper(mockPrintService);

        verify(mockPrintService).createPrintJob();

        ArgumentCaptor<Doc> docCaptor = ArgumentCaptor.forClass(Doc.class);
        verify(mockDocPrintJob).print(docCaptor.capture(), isNull());

        assertEquals(expectedDoc.getDocFlavor(), docCaptor.getValue().getDocFlavor());
        assertArrayEquals((byte[]) expectedDoc.getPrintData(), (byte[]) docCaptor.getValue().getPrintData());
        assertEquals(expectedDoc.getAttributes(), docCaptor.getValue().getAttributes());
    }

    @Test
    void testCashDrawer() throws IllegalArgumentException, PrintException, IOException {

        PrintService mockPrintService = mock(PrintService.class);
        DocPrintJob  mockDocPrintJob  = mock(DocPrintJob.class);
        when(mockPrintService.createPrintJob()).thenReturn(mockDocPrintJob);

        Doc expectedDoc = new SimpleDoc((init + cashDrawer).getBytes(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);

        printer.cashDrawer(mockPrintService);

        verify(mockPrintService).createPrintJob();

        ArgumentCaptor<Doc> docCaptor = ArgumentCaptor.forClass(Doc.class);
        verify(mockDocPrintJob).print(docCaptor.capture(), isNull());

        assertEquals(expectedDoc.getDocFlavor(), docCaptor.getValue().getDocFlavor());
        assertArrayEquals((byte[]) expectedDoc.getPrintData(), (byte[]) docCaptor.getValue().getPrintData());
        assertEquals(expectedDoc.getAttributes(), docCaptor.getValue().getAttributes());
    }

}
