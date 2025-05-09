package pt.pchouse.pdf.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.swing.*;

@Configuration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource(value = "file:${apphome}/application.properties", ignoreResourceNotFound = true)
})
public class HttpsConfig
{
    @Value("${server.port}")
    private int httpsPort;// Default HTTPS port

    @Value("${server.ssl.key-store}")
    private String keyStorePath;// Path to the keystore

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> sslCustomizer() {
        return factory -> {
            factory.setPort(httpsPort); // HTTPS port

            Ssl ssl = new Ssl();
            ssl.setEnabled(true);
            ssl.setKeyStore(keyStorePath);
            ssl.setKeyStoreType("PKCS12");
            ssl.setKeyStorePassword("password");
            ssl.setKeyPassword("password");

            factory.setSsl(ssl);
        };
    }
}
