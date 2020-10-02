package com.sschakraborty.platform.damlayer.migration.executor;

import com.sschakraborty.platform.damlayer.core.service.DataService;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContextImpl;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.List;

public abstract class AbstractMigrationExecutor<S extends Model, D extends Model> implements Executor {
    private final List<Stage> stages = getStageSequence();
    private final MigrationContext migrationContext = new MigrationContextImpl();
    private final MigrationConfiguration migrationConfiguration;
    private final Entry<S, D> currentEntry;

    public AbstractMigrationExecutor(MigrationConfiguration migrationConfiguration, Entry<S, D> currentEntry) {
        this.migrationConfiguration = migrationConfiguration;
        this.currentEntry = currentEntry;
    }

    @Override
    public void run() {
        try {
            final DataService sourceDataService = getSourceDataService();
            final DataService destinationDataService = getDestinationDataService();
            sourceDataService.transactionManager().executeStateful((sourceUnit, sourceResult) -> {
                destinationDataService.transactionManager().executeStateful((destinationUnit, destinationResult) -> {
                    migrationContext.setSourceSession(sourceUnit.getSession());
                    migrationContext.setDestinationSession(destinationUnit.getSession());
                    while (migrationContext.shouldIterate()) {
                        stages.forEach(stage -> {
                            stage.runStage(migrationConfiguration, migrationContext, currentEntry);
                        });
                    }
                });
            });
        } catch (Exception e) {
            // TODO: Log if required
        }
    }

    private DataService getDestinationDataService() throws Exception {
        final String destinationTenantId = migrationConfiguration.getDestinationTenantId();
        return migrationConfiguration.getGenericDAO().getDataService(destinationTenantId);
    }

    private DataService getSourceDataService() throws Exception {
        final String sourceTenantId = migrationConfiguration.getSourceTenantId();
        return migrationConfiguration.getGenericDAO().getDataService(sourceTenantId);
    }

    protected abstract List<Stage> getStageSequence();
}