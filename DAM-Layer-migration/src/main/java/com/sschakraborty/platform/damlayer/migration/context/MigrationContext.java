package com.sschakraborty.platform.damlayer.migration.context;

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

    <S> List<S> getSourceObjects();

    <S> void setSourceObjects(List<S> sourceObjects);

    <D> List<D> getDestinationObjects();

    <D> void setDestinationObjects(List<D> destinationObjects);
}