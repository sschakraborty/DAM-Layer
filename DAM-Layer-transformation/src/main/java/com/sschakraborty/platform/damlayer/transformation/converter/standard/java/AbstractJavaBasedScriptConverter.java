package com.sschakraborty.platform.damlayer.transformation.converter.standard.java;

import bsh.Interpreter;
import com.sschakraborty.platform.damlayer.transformation.converter.standard.AbstractScriptConverter;

public abstract class AbstractJavaBasedScriptConverter extends AbstractScriptConverter {
    private final Interpreter interpreter;

    public AbstractJavaBasedScriptConverter() {
        interpreter = new Interpreter();
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }
}