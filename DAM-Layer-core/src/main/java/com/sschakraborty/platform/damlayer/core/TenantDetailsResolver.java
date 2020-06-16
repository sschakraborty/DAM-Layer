package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.audit.auditor.Auditor;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsCache;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.marker.Model;
import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.core.service.DataServiceImpl;
import com.sschakraborty.platform.damlayer.core.service.tenant.TenantService;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class TenantDetailsResolver {
    private final TenantService tenantService;
    private final TenantDetailsCache tenantDetailsCache;
    private final ConfigurationBuilder configurationBuilder;
    private final List<Class<? extends Model>> classes;
    private final Auditor auditor;

    public TenantDetailsResolver(
            TenantService tenantService,
            TenantDetailsCache tenantDetailsCache,
            ConfigurationBuilder configurationBuilder,
            List<Class<? extends Model>> classes,
            Auditor auditor
    ) {
        this.tenantService = tenantService;
        this.tenantDetailsCache = tenantDetailsCache;
        this.configurationBuilder = configurationBuilder;
        this.classes = classes;
        this.auditor = auditor;
    }

    public final DataService resolveDataService(final String tenantId) throws Exception {
        if (tenantDetailsCache.exists(tenantId)) {
            return tenantDetailsCache.getDataService(tenantId);
        }
        final TenantConfiguration tenantConfiguration = tenantService.getTenantConfiguration(tenantId);
        if (tenantConfiguration == null) {
            throw new Exception("Provided tenant with id " + tenantId + " is not registered or invalid!");
        }
        final Configuration configuration = configurationBuilder.build(
                tenantConfiguration.getConnectorMetadata(),
                classes
        );
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(
                configuration,
                tenantConfiguration,
                auditor
        );
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
        final TenantConfiguration tenantConfiguration = tenantService.getTenantConfiguration(tenantId);
        if (tenantConfiguration == null) {
            throw new Exception("Provided tenant with id " + tenantId + " is not registered or invalid!");
        }
        final Configuration configuration = configurationBuilder.build(
                tenantConfiguration.getConnectorMetadata(),
                classes
        );
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(
                configuration,
                tenantConfiguration,
                auditor
        );
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