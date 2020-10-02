package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsCache;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.processor.CallbackHandlerManager;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.core.service.DataServiceImpl;
import com.sschakraborty.platform.damlayer.core.service.tenant.InternalTenantService;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

public class TenantDetailsResolver {
    private final InternalTenantService internalTenantService;
    private final TenantDetailsCache tenantDetailsCache;
    private final ConfigurationBuilder configurationBuilder;
    private final CallbackHandlerManager callbackHandlerManager;

    public TenantDetailsResolver(
            InternalTenantService internalTenantService,
            TenantDetailsCache tenantDetailsCache,
            ConfigurationBuilder configurationBuilder,
            CallbackHandlerManager callbackHandlerManager) {
        this.internalTenantService = internalTenantService;
        this.tenantDetailsCache = tenantDetailsCache;
        this.configurationBuilder = configurationBuilder;
        this.callbackHandlerManager = callbackHandlerManager;
    }

    public final DataService resolveDataService(final String tenantId) throws Exception {
        if (tenantDetailsCache.exists(tenantId)) {
            return tenantDetailsCache.getDataService(tenantId);
        }
        final TenantConfiguration tenantConfiguration = internalTenantService.getTenantConfiguration(tenantId);
        if (tenantConfiguration == null) {
            throw new Exception("Provided tenant with id " + tenantId + " is not registered or invalid!");
        }
        final Configuration configuration = configurationBuilder.build(
                tenantConfiguration.getConnectorMetadata(),
                tenantConfiguration.getAnnotatedClasses()
        );
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(configuration, tenantConfiguration, callbackHandlerManager);
        final DataServiceImpl dataService = new DataServiceImpl(transactionManager);
        {
            tenantDetailsCache.put(tenantId, tenantConfiguration, dataService);
        }
        return dataService;
    }

    public final TenantConfiguration resolveConfiguration(final String tenantId) throws Exception {
        if (tenantDetailsCache.exists(tenantId)) {
            return tenantDetailsCache.getConfiguration(tenantId);
        }
        final TenantConfiguration tenantConfiguration = internalTenantService.getTenantConfiguration(tenantId);
        if (tenantConfiguration == null) {
            throw new Exception("Provided tenant with id " + tenantId + " is not registered or invalid!");
        }
        final Configuration configuration = configurationBuilder.build(
                tenantConfiguration.getConnectorMetadata(),
                tenantConfiguration.getAnnotatedClasses()
        );
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(configuration, tenantConfiguration, callbackHandlerManager);
        final DataServiceImpl dataService = new DataServiceImpl(transactionManager);
        {
            tenantDetailsCache.put(tenantId, tenantConfiguration, dataService);
        }
        return tenantConfiguration;
    }

    public final void refresh(String tenantId) {
        if (tenantDetailsCache.exists(tenantId)) {
            tenantDetailsCache.refresh(tenantId);
        }
    }
}