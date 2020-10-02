package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;

public class DataManagerImpl implements DataManager {
    private final TenantService tenantService;
    private final TenantDetailsResolver tenantDetailsResolver;

    public DataManagerImpl(TenantService tenantService, TenantDetailsResolver tenantDetailsResolver) {
        this.tenantService = tenantService;
        this.tenantDetailsResolver = tenantDetailsResolver;
    }

    @Override
    public void registerTenant(TenantConfiguration tenantConfiguration) {
        this.tenantDetailsResolver.refresh(tenantConfiguration.getId());
        this.tenantService.saveTenantConfiguration(tenantConfiguration);
    }

    @Override
    public void unregisterTenant(String tenantId) {
        try {
            final TenantConfiguration tenantConfiguration = this.resolveConfiguration(tenantId);
            this.tenantService.deleteTenantConfiguration(tenantConfiguration);
        } catch (Exception e) {
            // TODO: Log exception if required
        } finally {
            this.tenantDetailsResolver.refresh(tenantId);
        }
    }

    @Override
    public TenantConfiguration resolveConfiguration(String tenantId) throws Exception {
        return this.tenantDetailsResolver.resolveConfiguration(tenantId);
    }

    @Override
    public DataService resolveDataService(String tenantId) throws Exception {
        return this.tenantDetailsResolver.resolveDataService(tenantId);
    }
}