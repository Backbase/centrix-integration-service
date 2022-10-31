package com.backbase.accelerators.centrix.client.impl;

import com.backbase.accelerators.centrix.client.CentrixClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "centrix", name = "enableMocks", havingValue = "true", matchIfMissing = false)
public class MockCentrixClientImpl implements CentrixClient {

    @Override
    public String getOneTimePassword(String userId, String systemIdCode) {
        log.info("Obtaining one-time password key from mock Centrix service");
        return "<otpwd>2142377673635265</otpwd>";
    }

}
