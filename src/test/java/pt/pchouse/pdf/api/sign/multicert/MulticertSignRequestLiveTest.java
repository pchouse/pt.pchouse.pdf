package pt.pchouse.pdf.api.sign.multicert;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.pchouse.pdf.api.sign.multicert.model.SignatureInfo;
import pt.pchouse.pdf.api.sign.multicert.model.VisibleImageType;
import pt.pchouse.pdf.api.sign.multicert.model.VisibleSignatureInfo;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

@SpringBootTest
public class MulticertSignRequestLiveTest
{
    String multicertService = "*******";
    String multicertPassword = "******";

    @Autowired
    MulticertSignRequest multicertSignRequest;

    @Test
    void testSignPDF() throws Exception {
        // Read pdf from file

        var forlder  = Path.of("src/test/resources/sign").toAbsolutePath();

        var pdfPathIn = Path.of(forlder.toString(), "test.pdf");
        var pdfPathOut = Path.of(forlder.toString(), "test_signed.pdf");
        var imgPath = Path.of(forlder.toString(), "casa.png");

        if(!Files.exists(pdfPathIn)){
            throw new IOException("File not found: " + pdfPathIn);
        }

        if(Files.exists(pdfPathOut)){
            Files.delete(pdfPathOut);
        }

        var pdf = Files.readAllBytes(pdfPathIn);
        // Convert pdf to base64
        var pdfBase64 = java.util.Base64.getEncoder().encodeToString(pdf);

        var visibleSignatureInfo = new VisibleSignatureInfo();
        visibleSignatureInfo.setLocationType(VisibleSignatureInfo.LocationTypeEnum.COORDINATE);
        visibleSignatureInfo.areaBackgroundColor("transparent");
        visibleSignatureInfo.setArea("width=300,height=200");
        visibleSignatureInfo.setLocationValue(
                String.format("page=%s,x=%s,y=%s", 1, 100, 500)
        );
        visibleSignatureInfo.setOneOffTemplate("Signed by King Sejong of Joseon");
        visibleSignatureInfo.setTemplateType(VisibleSignatureInfo.TemplateTypeEnum.ONE_OFF);
        visibleSignatureInfo.setImageType(VisibleImageType.PNG);
        visibleSignatureInfo.setImageValue(
                java.util.Base64.getEncoder().encodeToString(
                        Files.readAllBytes(imgPath)
                )
        );

        var signatureInfo = new SignatureInfo();
        signatureInfo.setSignatureType(SignatureInfo.SignatureTypeEnum.PADES);
        signatureInfo.setReason("Test API");
        signatureInfo.setLocation("Lisbon");
        signatureInfo.setVisibleSignatureInfo(visibleSignatureInfo);


        multicertSignRequest.setTest(true);
        multicertSignRequest.setUsername(multicertService);
        multicertSignRequest.setPassword(multicertPassword);
        multicertSignRequest.setExternalReference(
                "testExternalRef_" + (new Random()).nextInt(99999)
        );
        multicertSignRequest.setDescription("API test");
        multicertSignRequest.setFileName(
                multicertSignRequest.getExternalReference() + ".pdf"
        );
        multicertSignRequest.setSignatureInfo(signatureInfo);


        var result = multicertSignRequest.signPDF(pdfBase64, null);

        Assertions.assertNotNull(result);
        
        Files.write(
                pdfPathOut,
                java.util.Base64.getDecoder().decode(result),
                StandardOpenOption.CREATE_NEW
        );
    }

}
