package com.backbase.accelerators.centrix.service.impl;

import com.backbase.buildingblocks.backend.security.auth.config.SecurityContextUtil;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.accelerators.centrix.client.CentrixClient;
import com.backbase.accelerators.centrix.client.model.CentrixSingleSignOnResponse;
import com.backbase.accelerators.centrix.config.CentrixConfigurationProperties;
import com.backbase.accelerators.centrix.constants.CentrixErrorCode;
import com.backbase.accelerators.centrix.service.CentrixService;
import com.backbase.accelerators.centrix.util.AESEncrypter;
import com.backbase.accelerators.centrix.util.XPathUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Service
@RequiredArgsConstructor
public class CentrixServiceImpl implements CentrixService {

    private static final String SINGLE_SIGN_ON_ENDPOINT = "/loginSSO.aspx?u=%s&p=%s";
    private static final String ONE_TIME_PASSWORD_XPATH = "//otpwd/text()";
    private final SecurityContextUtil securityContextUtil;
    private final CentrixClient centrixClient;
    private final CentrixConfigurationProperties centrixConfigurationProperties;

    @Override
    public CentrixSingleSignOnResponse doSingleSignOn(String keepAliveUrl) {
        String oneTimePassword = getOneTimePassword();

        return new CentrixSingleSignOnResponse()
                .redirectUrl(buildRedirectUrl(extractUsernameFromToken(), oneTimePassword, keepAliveUrl));
    }

    private String buildRedirectUrl(String username, String oneTimePassword, String keepAliveUrl) {
        log.info("Constructing Centrix redirect single-sign-one URL");
        String concatenatedUrl = centrixConfigurationProperties.getBaseUrl().concat(SINGLE_SIGN_ON_ENDPOINT);

        if (isNotBlank(keepAliveUrl)) {
            concatenatedUrl = concatenatedUrl.concat("&i=").concat(keepAliveUrl);
        }

        return String.format(concatenatedUrl, encode(encrypt(username)), encode(encrypt(oneTimePassword)));
    }

    private String getOneTimePassword() {
        String response = centrixClient.getOneTimePassword(
                encode(encrypt(extractUsernameFromToken())),
                encode(encrypt(centrixConfigurationProperties.getSystemIdCode())));

        checkForErrors(response);
        return extractOneTimePasswordFromResponse(response);
    }

    private String extractUsernameFromToken() {
        return securityContextUtil.getUserTokenClaim("sub", String.class)
                .orElseThrow(() -> new BadRequestException("Username not found in InternalJwt claims."));
    }

    private void checkForErrors(String response) {
        log.info("Inspecting Centrix response for errors");
        if (!response.contains("<errormessage>")) {
            return;
        }

        log.error("Centrix response: {}", response);
        String errorCode = extractErrorFromResponse(response);
        CentrixErrorCode centrixErrorCode = CentrixErrorCode.fromCode(errorCode);

        String message = String.format("Centrix API returned error response. ErrorCode=%s", centrixErrorCode.getCode());
        log.error(message);
        throw new BadRequestException(message);
    }

    private String extractOneTimePasswordFromResponse(String response) {
        return XPathUtils.getStringValue(response, ONE_TIME_PASSWORD_XPATH);
    }

    private String extractErrorFromResponse(String response) {
        Pattern pattern = Pattern.compile("\\d{4}"); // 4-digit error code pattern
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            String errorCode = matcher.group();
            log.info("Extracted Centrix error code from response: {}", errorCode);
            return errorCode;
        }

        return null;
    }

    private String encrypt(String value) {
        if (!centrixConfigurationProperties.getEncryption().isEnabled()) {
            log.info("Encryption disabled. Returning un-encrypted value.");
            return value;
        }

        return AESEncrypter.encrypt(
                value,
                centrixConfigurationProperties.getEncryption().getEncryptionKey(),
                centrixConfigurationProperties.getEncryption().getInitializationVector());
    }

    @SneakyThrows
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
