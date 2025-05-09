package pt.pchouse.pdf.api.sign.multicert;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pt.pchouse.pdf.api.sign.multicert.model.*;
import pt.pchouse.pdf.api.sign.multicert.service.AuthenticateService;
import pt.pchouse.pdf.api.sign.multicert.service.SignStashDocumentSignService;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SignTest
{

    String signContent = "signContent";

    @Mock
    SignStashApiAuthentication mockSignStashApiAuthentication;

    @Mock
    SignStashApiClient mockSignStashApiClient;

    @InjectMocks
    @Autowired
    Sign sign;

    @BeforeEach
    public void setSignMocks() throws Exception {

        MockitoAnnotations.openMocks(this);

        AuthenticationResponse mockAuthenticationResponse = mock(AuthenticationResponse.class);
        when(mockAuthenticationResponse.getAccessToken()).thenReturn("testAccessToken");

        //noinspection rawtypes
        Response mockResponseAuth = mock(Response.class);
        when(mockResponseAuth.isSuccessful()).thenReturn(true);
        when(mockResponseAuth.body()).thenReturn(mockAuthenticationResponse);

        //noinspection rawtypes
        Call mockCallAuth = mock(Call.class);
        when(mockCallAuth.execute()).thenReturn(mockResponseAuth);

        AuthenticateService mockAuthenticateService = mock(AuthenticateService.class);

        //noinspection unchecked
        when(mockAuthenticateService.authenticate("client_credentials")).thenReturn(mockCallAuth);

        when(mockSignStashApiAuthentication.createService(
                any(String.class), any(String.class), any(Boolean.class)
        )).thenReturn(mockAuthenticateService);

        DocumentResponse mockDocumentResponse = mock(DocumentResponse.class);
        when(mockDocumentResponse.getSignStatus()).thenReturn(SigningStatus.SIGNED);
        when(mockDocumentResponse.getSignedContent()).thenReturn(signContent);

        SignOperation mockSignOperation = mock(SignOperation.class);
        when(mockSignOperation.getDocumentList()).thenReturn(new ArrayList<>()
        {
            {
                add(mockDocumentResponse);
            }
        });

        //noinspection rawtypes
        Response mockResponseSign = mock(Response.class);
        when(mockResponseSign.body()).thenReturn(mockSignOperation);

        //noinspection rawtypes
        Call mockCallSign = mock(Call.class);
        when(mockCallSign.execute()).thenReturn(mockResponseSign);

        SignStashDocumentSignService mockSignStashDocumentSignService = mock(SignStashDocumentSignService.class);

        //noinspection unchecked
        when(mockSignStashDocumentSignService.signDocumentBatchBase64(
                any(SignRequestBase64Document.class)
        )).thenReturn(mockCallSign);

        when(mockSignStashApiClient.createService(
                eq(SignStashDocumentSignService.class),
                any(String.class),
                any(Boolean.class)
        )).thenReturn(mockSignStashDocumentSignService);

    }

    @Test
    void testSignBase64() throws Exception {

        SignRequestBase64Document mockSignRequestBase64Document = mock(SignRequestBase64Document.class);

        assertEquals(
                signContent,
                sign.signBase64(
                        "username",
                        "password",
                        mockSignRequestBase64Document,
                        true
                )
        );
    }

}
