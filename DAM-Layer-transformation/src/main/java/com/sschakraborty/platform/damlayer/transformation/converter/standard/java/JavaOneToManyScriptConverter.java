package com.sschakraborty.platform.damlayer.transformation.converter.standard.java;

import bsh.Interpreter;
import com.sschakraborty.platform.damlayer.transformation.converter.OneToManyConverter;

import java.util.*;
import java.util.stream.Collectors;

public class JavaOneToManyScriptConverter<S, D> extends AbstractJavaBasedScriptConverter implements OneToManyConverter<S, D> {
    private static final String CONVERTED_OBJECTS = "convertedObjects";

    @Override
    public List<D> convert(final S sourceObject) throws Exception {
        final Interpreter interpreter = getInterpreter();
        final String script = getScript().trim();
        if (script.length() > 0) {
            try (final Scanner scanner = new Scanner(script)) {
                interpreter.set("sourceObject", sourceObject);
                interpreter.set(CONVERTED_OBJECTS, new ArrayList<>());
                while (scanner.hasNextLine()) {
                    interpreter.eval(scanner.nextLine());
                }
            }
            final Object convertedObjects = interpreter.get(CONVERTED_OBJECTS);
            if (convertedObjects == null) {
                throw new Exception("Converted objects collection was not set in the script!");
            }
            return processResponse(convertedObjects);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private List<D> processResponse(Object convertedObjects) {
        if (convertedObjects instanceof List) {
            return (List<D>) convertedObjects;
        } else if (convertedObjects instanceof Set) {
            return new ArrayList<>(((Set<D>) convertedObjects));
        } else if (convertedObjects instanceof Map) {
            final Set<Map.Entry<Object, D>> entries = ((Map<Object, D>) convertedObjects).entrySet();
            return entries.stream().map(Map.Entry::getValue).collect(Collectors.toList());
        } else {
            return null;
        }
    }
}