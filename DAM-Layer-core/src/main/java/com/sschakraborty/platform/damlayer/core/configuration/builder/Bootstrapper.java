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
import com.sschakraborty.platform.damlayer.core.processor.CallbackHandler;
import com.sschakraborty.platform.damlayer.core.processor.CallbackHandlerManager;
import com.sschakraborty.platform.damlayer.core.processor.DefaultCallbackHandlerWrapper;
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
public class Bootstrapper {
    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilderImpl();
    private CallbackHandler defaultCallbackHandler = null;
    private ConnectorMetadata primaryConnectorMetadata;
    private int cacheSize;
    private AuditEngine auditEngine;
    private CallbackHandlerManager callbackHandlerManager;

    public final Bootstrapper withPrimaryConnectorMetadata(final ConnectorMetadata connectorMetadata) {
        this.primaryConnectorMetadata = connectorMetadata;
        return this;
    }

    public final Bootstrapper withDefaultCallbackHandler(CallbackHandler defaultCallbackHandler) {
        this.defaultCallbackHandler = defaultCallbackHandler;
        return this;
    }

    public final Bootstrapper withAuditor(Auditor auditor) {
        this.auditEngine = new AuditEngineImpl(auditor, new AuditConfiguration());
        return this;
    }

    public final Bootstrapper withAuditorAndAuditConfiguration(Auditor auditor, AuditConfiguration auditConfiguration) {
        this.auditEngine = new AuditEngineImpl(auditor, auditConfiguration);
        return this;
    }

    public final Bootstrapper withCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
        return this;
    }

    public DataManager build() throws Exception {
        if (this.primaryConnectorMetadata == null) {
            throw new Exception("Primary connector metadata has not been defined!");
        }
        final TransactionManager tenantTransactionManager = createCallbackHandlerManagerAndTenantTransactionManager();
        final InternalTenantService internalTenantService = new InternalTenantServiceImpl(tenantTransactionManager);
        final TenantDetailsCache tenantDetailsCache = new TenantDetailsMapCacheImpl(cacheSize);
        final TenantDetailsResolver tenantDetailsResolver = new TenantDetailsResolver(internalTenantService, tenantDetailsCache, configurationBuilder, callbackHandlerManager);
        return new DataManagerImpl(internalTenantService, tenantDetailsResolver, callbackHandlerManager);
    }

    private TransactionManager createCallbackHandlerManagerAndTenantTransactionManager() {
        final TransactionManagerImpl transactionManager = (TransactionManagerImpl) buildTenantTransactionManagerWithoutAuditEngine(primaryConnectorMetadata);
        this.auditEngine = (this.auditEngine == null) ? new AuditEngineImpl(new DefaultAuditor(transactionManager), new AuditConfiguration()) : this.auditEngine;
        this.callbackHandlerManager = new CallbackHandlerManager(new DefaultCallbackHandlerWrapper(defaultCallbackHandler, auditEngine));
        transactionManager.setCallbackHandlerManager(callbackHandlerManager);
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