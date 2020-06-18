package com.sschakraborty.platform.damlayer.transformation.converter.standard.java;

import bsh.Interpreter;
import com.sschakraborty.platform.damlayer.transformation.converter.OneToOneConverter;

import java.util.Scanner;

public class JavaOneToOneScriptConverter<S, D> extends AbstractJavaBasedScriptConverter implements OneToOneConverter<S, D> {
    @Override
    @SuppressWarnings("unchecked")
    public D convert(final S sourceObject) throws Exception {
        final Interpreter interpreter = getInterpreter();
        final String script = getScript().trim();
        if (script.length() > 0) {
            try (final Scanner scanner = new Scanner(script)) {
                interpreter.set("sourceObject", sourceObject);
                while (scanner.hasNextLine()) {
                    interpreter.eval(scanner.nextLine());
                }
            }
            return (D) interpreter.get("convertedObject");
        } else {
            return null;
        }
    }
}