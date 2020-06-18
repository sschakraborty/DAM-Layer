package com.sschakraborty.platform.damlayer.transformation.converter.standard;

import com.sschakraborty.platform.damlayer.transformation.converter.OneToOneConverter;

public class IdentityOneToOneConverter<S> implements OneToOneConverter<S, S> {
    @Override
    public S convert(final S sourceObject) {
        return sourceObject;
    }
}