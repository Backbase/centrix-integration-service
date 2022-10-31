package com.backbase.accelerators.centrix.client.impl;

import com.backbase.accelerators.centrix.config.CentrixConfigurationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CentrixClientImplTest {

    @Mock
    private CentrixConfigurationProperties centrixConfigurationProperties;

    @Mock
    private HttpClient httpClient;

    @InjectMocks
    private CentrixClientImpl centrixClient;

    @Test
    public void should_return_one_time_password() throws Exception {
        String username = "fakeUser";
        String systemIdCode = "fakeSystemIdCode";
        HttpResponse<String> httpResponse = mock(HttpResponse.class);

        when(centrixConfigurationProperties.getBaseUrl()).thenReturn("http://fake-url.com");
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn("<otpwd>999999999999999</otpwd>");

        String result = centrixClient.getOneTimePassword(username, systemIdCode);
        assertEquals("<otpwd>999999999999999</otpwd>", result);

        verify(centrixConfigurationProperties).getBaseUrl();
        verify(httpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        verify(httpResponse).body();
    }
}