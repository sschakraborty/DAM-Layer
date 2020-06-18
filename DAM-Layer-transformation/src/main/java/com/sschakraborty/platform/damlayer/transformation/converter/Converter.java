package com.sschakraborty.platform.damlayer.transformation.converter;

import java.io.Serializable;

public interface Converter extends Serializable {
    boolean isOneToOne();
}