package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;

public interface GenericDAO {
    void registerTenant(TenantConfiguration tenantConfiguration);

    void unregisterTenant(String tenantId);

    TenantConfiguration resolveConfiguration(String tenantId) throws Exception;

    DataService resolveDataService(String tenantId) throws Exception;
}