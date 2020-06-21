package com.sschakraborty.platform.damlayer.migration;

import com.sschakraborty.platform.damlayer.migration.executor.EntityMigrationExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MigrationInstance {
    private final ExecutorService executor;
    private final MigrationConfiguration migrationConfiguration;

    public MigrationInstance(MigrationConfiguration migrationConfiguration) {
        this.executor = buildExecutor(migrationConfiguration.getMaxThreadCount());
        this.migrationConfiguration = migrationConfiguration;
    }

    private ExecutorService buildExecutor(int maxThreadCount) {
        int threadCount = 4;
        if (maxThreadCount >= 2 && maxThreadCount <= 8) {
            threadCount = maxThreadCount;
        }
        return Executors.newFixedThreadPool(threadCount);
    }

    public void dispatch() {
        migrationConfiguration.getTransformer().getAllEntries().forEach(entry -> {
            final EntityMigrationExecutor executor = new EntityMigrationExecutor(migrationConfiguration, entry, 0);
            this.executor.submit(executor);
        });
    }
}