package com.backbase.accelerators.centrix.controller;

import com.backbase.accelerators.centrix.client.model.CentrixSingleSignOnResponse;
import com.backbase.accelerators.centrix.service.CentrixService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CentrixControllerTest {

    @Mock
    private CentrixService centrixService;

    @InjectMocks
    private CentrixController centrixController;

    @Test
    public void should_return_centrix_sso_url() {
        when(centrixService.doSingleSignOn(anyString())).thenReturn(centrixSingleSignOnResponse());

        ResponseEntity<CentrixSingleSignOnResponse> result = centrixController.doSingleSignOn("http://fake-keep-alive-url.com");
        assertEquals("http://fake-url.com/loginSSO.aspx?u=2k2gwZvuVlMTe7g5b5qhJw%3D%3D&p=1vmqLgdyupydoqwMMSqTYw%3D%3D&i=http://fake-keep-alive-url.com", result.getBody().getRedirectUrl());
    }

    private CentrixSingleSignOnResponse centrixSingleSignOnResponse() {
        return new CentrixSingleSignOnResponse()
                .redirectUrl("http://fake-url.com/loginSSO.aspx?u=2k2gwZvuVlMTe7g5b5qhJw%3D%3D&p=1vmqLgdyupydoqwMMSqTYw%3D%3D&i=http://fake-keep-alive-url.com");
    }
}