package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;

public class GenericDAOImpl implements GenericDAO {
    private final TenantService tenantService;
    private final TenantDataServiceResolver tenantDataServiceResolver;

    public GenericDAOImpl(TenantService tenantService, TenantDataServiceResolver tenantDataServiceResolver) {
        this.tenantService = tenantService;
        this.tenantDataServiceResolver = tenantDataServiceResolver;
    }
}