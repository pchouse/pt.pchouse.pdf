package pt.pchouse.pdf.api.print;

import javax.print.DocFlavor;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;

public class CustomPrintServiceLookup extends PrintServiceLookup
{
    private final PrintService[] printServices;

    public CustomPrintServiceLookup(PrintService[] printServices) {
        this.printServices = printServices;
    }

    @Override
    public PrintService[] getPrintServices(DocFlavor flavor, AttributeSet attributes) {
        return new PrintService[0];
    }

    @Override
    public PrintService[] getPrintServices() {
        return printServices;
    }

    @Override
    public MultiDocPrintService[] getMultiDocPrintServices(DocFlavor[] flavors, AttributeSet attributes) {
        return new MultiDocPrintService[0];
    }

    @Override
    public PrintService getDefaultPrintService() {
        return (printServices.length > 0) ? printServices[0] : null;
    }

}
