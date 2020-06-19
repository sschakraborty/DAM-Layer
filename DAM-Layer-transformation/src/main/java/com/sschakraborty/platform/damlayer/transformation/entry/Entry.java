package com.sschakraborty.platform.damlayer.transformation.entry;

import com.sschakraborty.platform.damlayer.transformation.converter.Converter;

import java.util.Objects;

public class Entry<S, D> {
    private final Class<S> sourceClass;
    private final Class<D> destinationClass;
    private final Converter converter;

    public Entry(Class<S> sourceClass, Class<D> destinationClass, Converter converter) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
        this.converter = converter;
    }

    public Class<S> getSourceClass() {
        return sourceClass;
    }

    public Class<D> getDestinationClass() {
        return destinationClass;
    }

    public Converter getConverter() {
        return converter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return getSourceClass().equals(entry.getSourceClass()) &&
                getDestinationClass().equals(entry.getDestinationClass()) &&
                getConverter().equals(entry.getConverter());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceClass(), getDestinationClass(), getConverter());
    }
}