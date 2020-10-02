package com.sschakraborty.platform.damlayer.core.service.tenant;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;

public interface InternalTenantService {
    TenantConfiguration getTenantConfiguration(String tenantId);

    void saveTenantConfiguration(TenantConfiguration tenantConfiguration);

    void deleteTenantConfiguration(TenantConfiguration tenantConfiguration);
}