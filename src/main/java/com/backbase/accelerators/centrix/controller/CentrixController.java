package com.backbase.accelerators.centrix.controller;

import com.backbase.accelerators.centrix.client.api.CentrixApi;
import com.backbase.accelerators.centrix.client.model.CentrixSingleSignOnResponse;
import com.backbase.accelerators.centrix.service.CentrixService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CentrixController implements CentrixApi {

    private final CentrixService centrixService;

    @Override
    @PreAuthorize("checkPermission('Payments', 'Manage Positive Pay', {'view'})")
    public ResponseEntity<CentrixSingleSignOnResponse> doSingleSignOn(String keepAliveUrl) {
        log.info("Entering doSingleSignOn()");
        return ResponseEntity.ok(centrixService.doSingleSignOn(keepAliveUrl));
    }
}
