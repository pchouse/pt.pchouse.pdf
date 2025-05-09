package pt.pchouse.pdf.api.auth;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AuthTest
{

    @Value("${client.allowIps:}")
    private String[] allowIps;

    @Test
    void testIsRemoteIpAllowed_NoConfiguredIps_Allowed() {
        Auth auth = new Auth();
        ReflectionTestUtils.setField(auth, "allowIps", new String[]{});

        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
            Mockito.when(mockRequest.getRemoteAddr()).thenReturn("192.168.0.1");
            mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                    .thenReturn(new ServletRequestAttributes(mockRequest));

            auth.catchRemoteIP();
            assertTrue(auth.isRemoteIpAllowed());
        }
    }

    @Test
    void testIsRemoteIpAllowed_AllowedIp_Matched() {
        Auth auth = new Auth();
        ReflectionTestUtils.setField(auth, "allowIps", new String[]{"192.168.0.1"});

        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
            Mockito.when(mockRequest.getRemoteAddr()).thenReturn("192.168.0.1");
            mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                    .thenReturn(new ServletRequestAttributes(mockRequest));

            auth.catchRemoteIP();
            assertTrue(auth.isRemoteIpAllowed());
        }
    }

    @Test
    void testIsRemoteIpAllowed_NotAllowedIp_NotMatched() {
        Auth auth = new Auth();
        ReflectionTestUtils.setField(auth, "allowIps", new String[]{"192.168.1.1"});

        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
            Mockito.when(mockRequest.getRemoteAddr()).thenReturn("192.168.0.1");
            mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                    .thenReturn(new ServletRequestAttributes(mockRequest));

            auth.catchRemoteIP();
            assertFalse(auth.isRemoteIpAllowed());
        }
    }

    @Test
    void testIsRemoteIpAllowed_LocalhostIp_Allowed() {
        Auth auth = new Auth();
        ReflectionTestUtils.setField(auth, "allowIps", new String[]{"localhost"});

        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
            Mockito.when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");
            mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                    .thenReturn(new ServletRequestAttributes(mockRequest));

            auth.catchRemoteIP();
            assertTrue(auth.isRemoteIpAllowed());
        }
    }

    @Test
    void testIsRemoteIpAllowed_LocalhostIpv6_Allowed() {
        Auth auth = new Auth();
        ReflectionTestUtils.setField(auth, "allowIps", new String[]{"ip6-localhost"});

        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = Mockito.mockStatic(RequestContextHolder.class)) {
            HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
            Mockito.when(mockRequest.getRemoteAddr()).thenReturn("::1");
            mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                    .thenReturn(new ServletRequestAttributes(mockRequest));

            auth.catchRemoteIP();
            assertTrue(auth.isRemoteIpAllowed());
        }
    }

}