package com.sschakraborty.platform.damlayer.transformation.converter;

import java.util.List;

public interface OneToManyConverter<S, D> extends Converter {
    @Override
    default boolean isOneToOne() {
        return false;
    }

    List<D> convert(final S sourceObject) throws Exception;
}