package com.backbase.accelerators.centrix.service;

import com.backbase.accelerators.centrix.client.model.CentrixSingleSignOnResponse;

public interface CentrixService {

    CentrixSingleSignOnResponse doSingleSignOn(String keepAliveUrl);
}
