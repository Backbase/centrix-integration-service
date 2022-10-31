package com.backbase.accelerators.centrix.service.impl;

import com.backbase.buildingblocks.backend.security.auth.config.SecurityContextUtil;
import com.backbase.buildingblocks.presentation.errors.BadRequestException;
import com.backbase.accelerators.centrix.client.CentrixClient;
import com.backbase.accelerators.centrix.client.model.CentrixSingleSignOnResponse;
import com.backbase.accelerators.centrix.config.CentrixConfigurationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CentrixServiceImplTest {

    @Mock
    private SecurityContextUtil securityContextUtil;

    @Mock
    private CentrixClient centrixClient;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CentrixConfigurationProperties centrixConfigurationProperties;

    @InjectMocks
    private CentrixServiceImpl centrixService;

    @Test
    public void should_return_centrix_sso_url() {

        when(centrixConfigurationProperties.getBaseUrl()).thenReturn("http://fake-url.com");
        when(centrixConfigurationProperties.getSystemIdCode()).thenReturn("fakeSystemIdCode");
        when(centrixConfigurationProperties.getEncryption().isEnabled()).thenReturn(true);
        when(centrixConfigurationProperties.getEncryption().getEncryptionKey()).thenReturn("1234567890ABCDEF1234567890ABCDEF");
        when(centrixConfigurationProperties.getEncryption().getInitializationVector()).thenReturn("1234567890ABCDEF");
        when(centrixClient.getOneTimePassword(anyString(), anyString())).thenReturn("<otpwd>999999999999999</otpwd>");
        when(securityContextUtil.getUserTokenClaim("sub", String.class)).thenReturn(Optional.of("fakeUsername"));

        CentrixSingleSignOnResponse result = centrixService.doSingleSignOn("http://fake-keep-alive-url.com");

        assertEquals("http://fake-url.com/loginSSO.aspx?u=2k2gwZvuVlMTe7g5b5qhJw%3D%3D&p=1vmqLgdyupydoqwMMSqTYw%3D%3D&i=http://fake-keep-alive-url.com", result.getRedirectUrl());
    }

    @Test(expected = BadRequestException.class)
    public void should_throw_exception_when_centrix_returns_error() {
        when(centrixConfigurationProperties.getSystemIdCode()).thenReturn("fakeSystemIdCode");
        when(centrixConfigurationProperties.getEncryption().isEnabled()).thenReturn(true);
        when(centrixConfigurationProperties.getEncryption().getEncryptionKey()).thenReturn("1234567890ABCDEF1234567890ABCDEF");
        when(centrixConfigurationProperties.getEncryption().getInitializationVector()).thenReturn("1234567890ABCDEF");
        when(centrixClient.getOneTimePassword(anyString(), anyString())).thenReturn("errorcode>1001</errorcode><errormessage>Invalid User ID Code</errormessage>");
        when(securityContextUtil.getUserTokenClaim("sub", String.class)).thenReturn(Optional.of("fakeUsername"));

        centrixService.doSingleSignOn("http://fake-keep-alive-url.com");
    }

}