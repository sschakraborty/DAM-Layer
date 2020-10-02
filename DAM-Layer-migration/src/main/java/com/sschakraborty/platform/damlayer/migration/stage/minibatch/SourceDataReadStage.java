package com.sschakraborty.platform.damlayer.migration.stage.minibatch;

import com.sschakraborty.platform.damlayer.core.Model;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class SourceDataReadStage implements Stage {
    private final int batchSize;

    public SourceDataReadStage(int batchSize) {
        this.batchSize = (batchSize >= 1 && batchSize <= 100) ? batchSize : 50;
    }

    @Override
    public <S extends Model, D extends Model> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    ) {
        final SessionWrapper sourceSession = migrationContext.getSourceSession();
        final CriteriaBuilder criteriaBuilder = sourceSession.criteriaBuilder();
        final CriteriaQuery<S> criteriaQuery = criteriaBuilder.createQuery(entry.getSourceClass());
        criteriaQuery.select(criteriaQuery.from(entry.getSourceClass()));
        TypedQuery<S> sourceObjectsQuery = sourceSession.createQuery(criteriaQuery);
        sourceObjectsQuery.setMaxResults(batchSize);
        sourceObjectsQuery.setFirstResult(migrationContext.getFetchOffset());
        final List<S> sourceObjects = sourceObjectsQuery.getResultList();
        migrationContext.setSourceObjects(sourceObjects);
    }
}