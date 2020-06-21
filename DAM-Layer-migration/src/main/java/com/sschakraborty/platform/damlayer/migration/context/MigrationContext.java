package com.sschakraborty.platform.damlayer.migration.context;

import com.sschakraborty.platform.damlayer.core.marker.Model;
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

    <S extends Model> List<S> getSourceObjects();

    <S extends Model> void setSourceObjects(List<S> sourceObjects);

    <D extends Model> List<D> getDestinationObjects();

    <D extends Model> void setDestinationObjects(List<D> destinationObjects);
}