package com.sschakraborty.platform.damlayer.core.service.tenant;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionResult;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

public class TenantServiceImpl implements TenantService {
    private static final String FETCH_EXTERNAL_TEXT = "Fetch operation performed by Tenant Service";
    private static final String CONFIG_KEY = "TENANT_CONFIG_KEY";
    private final TransactionManager transactionManager;

    public TenantServiceImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public TenantConfiguration getTenantConfiguration(final String tenantId) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            final TenantConfiguration tenantConfiguration = session.fetch(FETCH_EXTERNAL_TEXT, TenantConfigurationBean.class, tenantId);
            transactionResult.put(CONFIG_KEY, tenantConfiguration);
        });
        return result.hasKey(CONFIG_KEY) ? (TenantConfiguration) result.get(CONFIG_KEY) : null;
    }

    @Override
    public void saveTenantConfiguration(final TenantConfiguration tenantConfiguration) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            session.save(buildExternalText(tenantConfiguration), tenantConfiguration);
        });
    }

    @Override
    public void deleteTenantConfiguration(TenantConfiguration tenantConfiguration) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final SessionWrapper session = transactionUnit.getSession();
            session.delete(buildExternalText(tenantConfiguration), tenantConfiguration);
        });
    }

    private String buildExternalText(TenantConfiguration tenantConfiguration) {
        return String.format(
                "Operation performed for %s tenant with ID %s by Tenant Service",
                tenantConfiguration.getName(),
                tenantConfiguration.getId()
        );
    }
}