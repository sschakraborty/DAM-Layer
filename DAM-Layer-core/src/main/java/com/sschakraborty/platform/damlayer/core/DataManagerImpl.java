package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.core.service.tenant.InternalTenantService;

public class DataManagerImpl implements DataManager {
    private final InternalTenantService internalTenantService;
    private final TenantDetailsResolver tenantDetailsResolver;

    public DataManagerImpl(InternalTenantService internalTenantService, TenantDetailsResolver tenantDetailsResolver) {
        this.internalTenantService = internalTenantService;
        this.tenantDetailsResolver = tenantDetailsResolver;
    }

    @Override
    public void saveTenant(TenantConfiguration tenantConfiguration) {
        this.tenantDetailsResolver.refresh(tenantConfiguration.getId());
        this.internalTenantService.saveTenantConfiguration(tenantConfiguration);
    }

    @Override
    public void deleteTenant(String tenantId) {
        try {
            final TenantConfiguration tenantConfiguration = this.getTenant(tenantId);
            this.internalTenantService.deleteTenantConfiguration(tenantConfiguration);
        } catch (Exception e) {
            // TODO: Log exception if required
        } finally {
            this.tenantDetailsResolver.refresh(tenantId);
        }
    }

    @Override
    public TenantConfiguration getTenant(String tenantId) throws Exception {
        return this.tenantDetailsResolver.resolveConfiguration(tenantId);
    }

    @Override
    public DataService getDataService(String tenantId) throws Exception {
        return this.tenantDetailsResolver.resolveDataService(tenantId);
    }
}