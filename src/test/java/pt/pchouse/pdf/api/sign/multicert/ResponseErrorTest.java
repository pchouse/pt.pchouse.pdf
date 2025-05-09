package pt.pchouse.pdf.api.sign.multicert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseErrorTest
{

    @Test
    void TestParse()
    {
        var json = """
                {
                 	"errorCode":"2.2.1",
                 	"errorCodeDescription":"Visible elements not present for PAdES Signature.",
                 	"causeMessage":"Document testExternalRef_65297.pdf signature. The visible area is smaller than the uploaded image."
                 }
                """;
        var responseError = ResponseError.Parse(json);
        assert responseError != null;
        Assertions.assertEquals("2.2.1", responseError.getErrorCode());
        Assertions.assertEquals(
                "Visible elements not present for PAdES Signature.",
                responseError.getErrorCodeDescription()
        );
        Assertions.assertEquals(
                "Document testExternalRef_65297.pdf signature. The visible area is smaller than the uploaded image.",
                responseError.getCauseMessage()
        );
    }

    @Test
    void TestParseError()
    {
        var json = """
                {
                 	"errorCodes":"2.2.1",
                 	"errorCodeDescriptions":"Visible elements not present for PAdES Signature.",
                 	"causeMessages":"Document testExternalRef_65297.pdf signature. The visible area is smaller than the uploaded image."
                 }
                """;
        var responseError = ResponseError.Parse(json);
        Assertions.assertNull(responseError.getErrorCode());
    }

    @Test
    void TestParseErrorNull()
    {
        var responseError = ResponseError.Parse(null);
        Assertions.assertNull(responseError);
    }

}
