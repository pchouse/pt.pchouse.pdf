package pt.pchouse.pdf.api.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pt.pchouse.pdf.api.request.Metadata;
import pt.pchouse.pdf.api.request.Parameter;
import pt.pchouse.pdf.api.request.RequestReport;
import pt.pchouse.pdf.api.request.datasource.Database;
import pt.pchouse.pdf.api.request.datasource.IDataSource;
import pt.pchouse.pdf.api.sign.ISignPDF;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

@SpringBootTest
public class ReportTest
{

    @Autowired
    protected ApplicationContext appContext;

    @Test
    void testPdfGeneratorWithDatabaseAsDatasource() throws Exception
    {

        Database database = appContext.getBean(Database.class);
        database.setProvider(Database.Provider.SQLITE);
        database.setServerAddress(
                Paths.get("target/test-classes/MyReports/sakila_master.db").toAbsolutePath().toString()
        );

        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_STRING, "P_STRING", "Parameter String"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BOOLEAN, "P_BOOLEAN", "true"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DOUBLE, "P_DOUBLE", "999.49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_FLOAT, "P_FLOAT", "9.09"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_INTEGER, "P_INTEGER", "9"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_LONG, "P_LONG", "49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SHORT, "P_SHORT", "109"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BIGDECIMAL, "P_BIG_DECIMAL", "1999.2999"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_DATE, "P_SQL_DATE", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_TIME, "P_SQL_TIME", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_TIMESTAMP, "P_TIMESTAMPT", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DATE, "P_DATE", "1969-10-05", "yyyy-MM-dd"));

        Metadata metadata = appContext.getBean(Metadata.class);
        metadata.setAuthor("rebelo");
        metadata.setTitle("My Report");
        metadata.setSubject("My Report UNIT test");
        metadata.setKeywords("My Report, UNIT test");
        metadata.setCreator("rebelo");

        RequestReport<IDataSource, ISignPDF> request = new RequestReport<>(database, null);
        request.setReportType(RequestReport.ReportType.PDF);

        request.setReport("Flower_Landscape.jasper");
        request.setPrinterName(null);
        request.setParameters(parameters);
        request.setMetadata(metadata);

        request.setCopies(4);

        Report report   = appContext.getBean(Report.class, request);
        report.setReportBasePath(Paths.get("target/test-classes/MyReports").toAbsolutePath().toString());

        byte[] response = report.exportAsByteArrayAsync().get();
        Assertions.assertNotNull(response);

        Path path = Paths.get("target/test_report_Flower_Landscape.pdf");

        if( Files.exists(path)){
            Files.delete(path);
        }

        Files.write(path, response);

        request.setReport("sakila.jasper");

        response = report.exportAsByteArrayAsync().get();

        Assertions.assertNotNull(response);

        path = Paths.get("target/sakila.pdf");

        if( Files.exists(path)){
            Files.delete(path);
        }

        Files.write(path, response);
    }

    @Test
    void testPdfGeneratorPrintWithDatabaseAsDatasource() throws Exception
    {

        Database database = appContext.getBean(Database.class);
        database.setProvider(Database.Provider.SQLITE);
        database.setServerAddress(
                Paths.get("target/test-classes/MyReports/sakila_master.db").toAbsolutePath().toString()
        );

        ArrayList<Parameter> parameters = new ArrayList<>();
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_STRING, "P_STRING", "Parameter String"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BOOLEAN, "P_BOOLEAN", "true"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DOUBLE, "P_DOUBLE", "999.49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_FLOAT, "P_FLOAT", "9.09"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_INTEGER, "P_INTEGER", "9"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_LONG, "P_LONG", "49"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SHORT, "P_SHORT", "109"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_BIGDECIMAL, "P_BIG_DECIMAL", "1999.2999"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_DATE, "P_SQL_DATE", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_SQL_TIME, "P_SQL_TIME", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_TIMESTAMP, "P_TIMESTAMPT", "1738856468813"));
        parameters.add(appContext.getBean(Parameter.class, Parameter.Types.P_DATE, "P_DATE", "1969-10-05", "yyyy-MM-dd"));

        Metadata metadata = appContext.getBean(Metadata.class);
        metadata.setAuthor("rebelo");
        metadata.setTitle("My Report");
        metadata.setSubject("My Report UNIT test");
        metadata.setKeywords("My Report, UNIT test");
        metadata.setCreator("rebelo");

        RequestReport<IDataSource, ISignPDF> request = new RequestReport<>(database, null);
        request.setReportType(RequestReport.ReportType.PRINT);

        request.setReport("Flower_Landscape.jasper");
        request.setPrinterName("PDF");
        request.setParameters(parameters);
        request.setMetadata(metadata);

        request.setCopies(4);

        Report report   = appContext.getBean(Report.class, request);
        report.setReportBasePath(Paths.get("target/test-classes/MyReports").toAbsolutePath().toString());

        String result = report.exportAsync().get();
        Assertions.assertNull(result);
    }

}
