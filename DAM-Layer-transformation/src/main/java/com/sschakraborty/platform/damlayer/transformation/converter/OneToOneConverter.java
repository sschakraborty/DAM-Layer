package com.sschakraborty.platform.damlayer.transformation.converter;

public interface OneToOneConverter<S, D> extends Converter {
    @Override
    default boolean isOneToOne() {
        return true;
    }

    D convert(final S sourceObject) throws Exception;
}