package com.sschakraborty.platform.damlayer.core.service.tenant;

import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfigurationBean;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionResult;
import org.hibernate.Session;

public class TenantServiceImpl implements TenantService {
    private static final String CONFIG_KEY = "TENANT_CONFIG_KEY";
    private final TransactionManager transactionManager;

    public TenantServiceImpl(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public TenantConfiguration getTenantConfiguration(final String tenantId) {
        final TransactionResult result = transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            final TenantConfiguration tenantConfiguration = session.get(TenantConfigurationBean.class, tenantId);
            transactionResult.put(CONFIG_KEY, tenantConfiguration);
        });
        return result.hasKey(CONFIG_KEY) ? (TenantConfiguration) result.get(CONFIG_KEY) : null;
    }

    @Override
    public void saveTenantConfiguration(final TenantConfiguration tenantConfiguration) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            session.saveOrUpdate(tenantConfiguration);
        });
    }

    @Override
    public void deleteTenantConfiguration(TenantConfiguration tenantConfiguration) {
        transactionManager.executeStateful((transactionUnit, transactionResult) -> {
            final Session session = transactionUnit.getSession();
            session.delete(tenantConfiguration);
        });
    }
}