package com.sschakraborty.platform.damlayer.core.service.tenant;

public interface InternalTenantService {
    TenantConfiguration getTenantConfiguration(String tenantId);

    void saveTenantConfiguration(TenantConfiguration tenantConfiguration);

    void deleteTenantConfiguration(TenantConfiguration tenantConfiguration);
}