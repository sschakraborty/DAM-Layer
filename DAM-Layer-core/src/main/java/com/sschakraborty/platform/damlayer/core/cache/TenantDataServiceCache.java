package com.sschakraborty.platform.damlayer.core.cache;

import com.sschakraborty.platform.damlayer.core.service.DataService;

public interface TenantDataServiceCache {
    boolean exists(String tenantId);

    DataService get(String tenantId);

    void put(String tenantId, DataService dataService);
}