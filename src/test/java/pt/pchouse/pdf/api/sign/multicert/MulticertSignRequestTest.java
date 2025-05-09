package pt.pchouse.pdf.api.sign.multicert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.pchouse.pdf.api.sign.multicert.model.SignRequestBase64Document;
import pt.pchouse.pdf.api.sign.multicert.model.SignatureInfo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class MulticertSignRequestTest
{

    String expectedOutput = "signedPdfBase64Content";

    @Mock
    Sign mockSign;

    @InjectMocks
    @Autowired
    MulticertSignRequest multicertSignRequest;

    @BeforeEach
    public void setSignMocks() throws Exception {
        MockitoAnnotations.openMocks(this);
        when(
                mockSign.signBase64(
                        any(String.class),
                        any(String.class),
                        any(SignRequestBase64Document.class),
                        any(Boolean.class)
                )
        ).thenReturn(expectedOutput);
    }

    @Test
    void testSignPDFSuccess() throws Exception {
        // Arrange

        String inputPdfBase64 = "inputPdfBase64";

        multicertSignRequest.setUsername("testUser");
        multicertSignRequest.setPassword("testPassword");
        multicertSignRequest.setExternalReference("testExternalRef");
        multicertSignRequest.setDescription("testDescription");
        multicertSignRequest.setFileName("testFileName.pdf");
        multicertSignRequest.setTest(true);

        SignatureInfo signatureInfo = new SignatureInfo();
        multicertSignRequest.setSignatureInfo(signatureInfo);

        when(mockSign.signBase64(
                eq("testUser"),
                eq("testPassword"),
                any(SignRequestBase64Document.class),
                eq(true)
        )).thenReturn(expectedOutput);

        String result = multicertSignRequest.signPDF(inputPdfBase64, null);

        assertEquals(expectedOutput, result);

        verify(mockSign, times(1)).signBase64(
                eq("testUser"),
                eq("testPassword"),
                any(SignRequestBase64Document.class),
                eq(true)
        );
    }

}