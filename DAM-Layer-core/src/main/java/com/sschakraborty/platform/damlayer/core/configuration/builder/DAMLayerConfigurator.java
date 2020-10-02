package com.sschakraborty.platform.damlayer.core.configuration.builder;

import com.sschakraborty.platform.damlayer.audit.configuration.AuditConfiguration;
import com.sschakraborty.platform.damlayer.audit.core.Auditor;
import com.sschakraborty.platform.damlayer.audit.core.engine.AuditEngine;
import com.sschakraborty.platform.damlayer.audit.core.engine.AuditEngineImpl;
import com.sschakraborty.platform.damlayer.audit.payload.AuditPayload;
import com.sschakraborty.platform.damlayer.core.DataManager;
import com.sschakraborty.platform.damlayer.core.DataManagerImpl;
import com.sschakraborty.platform.damlayer.core.TenantDetailsResolver;
import com.sschakraborty.platform.damlayer.core.audit.DefaultAuditor;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsCache;
import com.sschakraborty.platform.damlayer.core.cache.TenantDetailsMapCacheImpl;
import com.sschakraborty.platform.damlayer.core.configuration.ConnectorMetadata;
import com.sschakraborty.platform.damlayer.core.configuration.TenantConfiguration;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilder;
import com.sschakraborty.platform.damlayer.core.configuration.parser.ConfigurationBuilderImpl;
import com.sschakraborty.platform.damlayer.core.service.tenant.InternalTenantService;
import com.sschakraborty.platform.damlayer.core.service.tenant.InternalTenantServiceImpl;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManager;
import com.sschakraborty.platform.damlayer.core.session.transaction.TransactionManagerImpl;
import com.sschakraborty.platform.damlayer.core.util.BuilderUtil;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

/**
 * Implementation of a configurator in builder pattern for DAM Layer
 */
public class DAMLayerConfigurator {
    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilderImpl();
    private ConnectorMetadata primaryConnectorMetadata;
    private int cacheSize;
    private AuditEngine auditEngine = null;

    public final DAMLayerConfigurator withPrimaryConnectorMetadata(final ConnectorMetadata connectorMetadata) {
        this.primaryConnectorMetadata = connectorMetadata;
        return this;
    }

    public final DAMLayerConfigurator withAuditor(Auditor auditor) {
        this.auditEngine = new AuditEngineImpl(auditor, new AuditConfiguration());
        return this;
    }

    public final DAMLayerConfigurator withAuditorAndAuditConfiguration(Auditor auditor, AuditConfiguration auditConfiguration) {
        this.auditEngine = new AuditEngineImpl(auditor, auditConfiguration);
        return this;
    }

    public final DAMLayerConfigurator withCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public DataManager build() throws Exception {
        if (this.primaryConnectorMetadata == null) {
            throw new Exception("Primary connector metadata has not been defined!");
        }
        final TransactionManager tenantTransactionManager = createAuditEngineAndTenantTransactionManager();
        final InternalTenantService internalTenantService = new InternalTenantServiceImpl(tenantTransactionManager);
        final TenantDetailsCache tenantDetailsCache = new TenantDetailsMapCacheImpl(cacheSize);
        final TenantDetailsResolver tenantDetailsResolver = new TenantDetailsResolver(internalTenantService, tenantDetailsCache, configurationBuilder, auditEngine);
        return new DataManagerImpl(internalTenantService, tenantDetailsResolver);
    }

    private TransactionManager createAuditEngineAndTenantTransactionManager() {
        final TransactionManagerImpl transactionManager = (TransactionManagerImpl) buildTenantTransactionManagerWithoutAuditEngine(primaryConnectorMetadata);
        this.auditEngine = (this.auditEngine == null) ? new AuditEngineImpl(new DefaultAuditor(transactionManager), new AuditConfiguration()) : this.auditEngine;
        transactionManager.setAuditEngine(auditEngine);
        return transactionManager;
    }

    private TransactionManager buildTenantTransactionManagerWithoutAuditEngine(ConnectorMetadata connectorMetadata) {
        final Configuration configuration = configurationBuilder.build(
                connectorMetadata,
                Arrays.asList(TenantConfiguration.class, ConnectorMetadata.class, AuditPayload.class)
        );
        final TenantConfiguration tenantConfiguration = TenantConfiguration.createBean();
        tenantConfiguration.setId("DAMLayer-SYSTEM-USER");
        tenantConfiguration.setName("DAM-Layer-System-User-TENANT");
        tenantConfiguration.setConnectorMetadata(connectorMetadata);
        return BuilderUtil.buildTransactionManager(configuration, tenantConfiguration, null);
    }
}