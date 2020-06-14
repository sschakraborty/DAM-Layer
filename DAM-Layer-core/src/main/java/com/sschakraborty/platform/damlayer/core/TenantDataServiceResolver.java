package com.sschakraborty.platform.damlayer.core;

import com.sschakraborty.platform.damlayer.core.cache.TenantDataServiceCache;
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

public class TenantDataServiceResolver {
    private final TenantService tenantService;
    private final TenantDataServiceCache tenantDataServiceCache;
    private final ConfigurationBuilder configurationBuilder;
    private final List<Class<? extends Model>> classes;

    public TenantDataServiceResolver(
            TenantService tenantService,
            TenantDataServiceCache tenantDataServiceCache,
            ConfigurationBuilder configurationBuilder,
            List<Class<? extends Model>> classes
    ) {
        this.tenantService = tenantService;
        this.tenantDataServiceCache = tenantDataServiceCache;
        this.configurationBuilder = configurationBuilder;
        this.classes = classes;
    }

    public final DataService resolve(final String tenantId) {
        if (tenantDataServiceCache.exists(tenantId)) {
            return tenantDataServiceCache.get(tenantId);
        }
        final TenantConfiguration tenantConfiguration = tenantService.getTenantConfiguration(tenantId);
        final Configuration configuration = configurationBuilder.build(
                tenantConfiguration.getConnectorMetadata(),
                classes
        );
        final TransactionManager transactionManager = BuilderUtil.buildTransactionManager(configuration);
        final DataServiceImpl dataService = new DataServiceImpl(transactionManager);
        {
            tenantDataServiceCache.put(tenantId, dataService);
        }
        return dataService;
    }
}