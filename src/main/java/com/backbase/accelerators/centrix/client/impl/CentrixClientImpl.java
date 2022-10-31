package com.backbase.accelerators.centrix.client.impl;

import com.backbase.accelerators.centrix.client.CentrixClient;
import com.backbase.accelerators.centrix.config.CentrixConfigurationProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "centrix", name = "enableMocks", havingValue = "false", matchIfMissing = true)
public class CentrixClientImpl implements CentrixClient {

    private static final String ONE_TIME_PASSWORD_ENDPOINT = "/otpwd.aspx?u=%s&s=%s";

    private final CentrixConfigurationProperties centrixConfigurationProperties;
    private final HttpClient httpClient;

    @Override
    @SneakyThrows
    public String getOneTimePassword(String userId, String systemIdCode) {
        log.info("Obtaining one-time password key from Centrix");

        HttpRequest httpRequest = HttpRequest.newBuilder(new URI(buildFinalUrl(userId, systemIdCode)))
                .GET()
                .build();

        return httpClient.send(httpRequest,  HttpResponse.BodyHandlers.ofString()).body();
    }

    private String buildFinalUrl(String userId, String systemIdCode) {
        String concatenatedUrl = centrixConfigurationProperties.getBaseUrl().concat(ONE_TIME_PASSWORD_ENDPOINT);
        return String.format(concatenatedUrl, userId, systemIdCode);
    }

}
