package com.backbase.accelerators.centrix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("centrix")
public class CentrixConfigurationProperties {

    private String baseUrl;
    private String systemIdCode;
    private EncryptionProperties encryption;

    @Data
    public static class EncryptionProperties {

        private boolean enabled;
        private String encryptionKey;
        private String initializationVector;
    }
}
