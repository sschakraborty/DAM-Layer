package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;

public interface TenantDetailsCache {
    boolean exists(String tenantId);

    DataService getDataService(String tenantId);

    TenantConfiguration getConfiguration(String tenantId);

    void put(String tenantId, TenantConfiguration configuration, DataService dataService);

    void refresh(String tenantId);
}