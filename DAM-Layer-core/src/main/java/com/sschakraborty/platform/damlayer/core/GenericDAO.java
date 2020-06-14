package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;

public interface GenericDAO {
    void registerTenant(TenantConfiguration tenantConfiguration);

    DataService resolveFor(String tenantId) throws Exception;
}