package com.sschakraborty.platform.damlayer.migration.stage.minibatch;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;
import com.sschakraborty.platform.damlayer.migration.MigrationConfiguration;
import com.sschakraborty.platform.damlayer.migration.context.MigrationContext;
import com.sschakraborty.platform.damlayer.migration.stage.Stage;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.List;

public class DestinationDataWriteStage implements Stage {
    @Override
    public <S extends AuditModel, D extends AuditModel> void runStage(
            MigrationConfiguration migrationConfiguration,
            MigrationContext migrationContext,
            Entry<S, D> entry
    ) {
        final SessionWrapper destinationSession = migrationContext.getDestinationSession();
        final List<D> destinationObjects = migrationContext.getDestinationObjects();
        destinationObjects.forEach(destinationObject -> {
            destinationSession.save("Operation performed by Migration-Engine-SYS-USER", destinationObject);
        });
    }
}