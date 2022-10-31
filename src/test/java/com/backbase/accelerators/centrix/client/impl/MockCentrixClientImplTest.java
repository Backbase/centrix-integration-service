package com.backbase.accelerators.centrix.client.impl;

import com.backbase.accelerators.centrix.config.CentrixConfigurationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpClient;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MockCentrixClientImplTest {

    @Mock
    private CentrixConfigurationProperties centrixConfigurationProperties;

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private MockCentrixClientImpl centrixClient;

    @Test
    public void should_return_one_time_password() {
        String username = "fakeUser";
        String systemIdCode = "fakeSystemIdCode";

        String result = centrixClient.getOneTimePassword(username, systemIdCode);
        assertEquals("<otpwd>2142377673635265</otpwd>", result);

    }
}