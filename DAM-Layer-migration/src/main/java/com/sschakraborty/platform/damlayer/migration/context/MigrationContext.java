package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.audit.core.AuditModel;
import com.sschakraborty.platform.damlayer.core.session.wrapper.SessionWrapper;

import java.util.List;

public interface MigrationContext {
    SessionWrapper getSourceSession();

    void setSourceSession(SessionWrapper sessionWrapper);

    SessionWrapper getDestinationSession();

    void setDestinationSession(SessionWrapper sessionWrapper);

    boolean shouldIterate();

    void setShouldIterate(boolean shouldIterate);

    int getFetchOffset();

    void setFetchOffset(int offset);

    <S extends AuditModel> List<S> getSourceObjects();

    <S extends AuditModel> void setSourceObjects(List<S> sourceObjects);

    <D extends AuditModel> List<D> getDestinationObjects();

    <D extends AuditModel> void setDestinationObjects(List<D> destinationObjects);
}