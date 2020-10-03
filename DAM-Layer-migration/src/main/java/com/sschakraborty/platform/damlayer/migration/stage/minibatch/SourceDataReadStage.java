package com.sschakraborty.platform.damlayer.migration.stage.minibatch;

import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class SourceDataReadStage implements Stage {
    private final int batchSize;

    public SourceDataReadStage(int batchSize) {
        this.batchSize = (batchSize >= 1 && batchSize <= 100) ? batchSize : 50;
    }

    @Override
    public <S, D> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    ) {
        final SessionWrapper sourceSession = migrationContext.getSourceSession();
        final CriteriaQuery<S> criteriaQuery = sourceSession.createCriteriaQuery(entry.getSourceClass());
        criteriaQuery.select(criteriaQuery.from(entry.getSourceClass()));
        final List<S> sourceObjects = sourceSession.executeSelect(criteriaQuery, migrationContext.getFetchOffset(), batchSize);
        migrationContext.setSourceObjects(sourceObjects);
    }
}