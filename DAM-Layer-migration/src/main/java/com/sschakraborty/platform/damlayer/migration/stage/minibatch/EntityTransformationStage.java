package com.sschakraborty.platform.damlayer.migration.stage.minibatch;

import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.shared.core.marker.Model;
import com.sschakraborty.platform.damlayer.transformation.Transformer;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.ArrayList;
import java.util.List;

public class EntityTransformationStage implements Stage {
    @Override
    public <S extends Model, D extends Model> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    ) {
        final List<D> destinationObjects = new ArrayList<>();
        final Transformer transformer = migrationConfiguration.getTransformer();
        migrationContext.getSourceObjects().forEach(sourceObject -> {
            try {
                if (transformer.isOneToOne(sourceObject)) {
                    destinationObjects.add(transformer.transformOneToOne(sourceObject));
                } else {
                    destinationObjects.addAll(transformer.transformOneToMany(sourceObject));
                }
            } catch (Exception e) {
                // TODO: Log if required
            }
        });
        migrationContext.setDestinationObjects(destinationObjects);
    }
}