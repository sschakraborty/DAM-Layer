package com.sschakraborty.platform.damlayer.transformation;

import bsh.EvalError;
import bsh.Interpreter;
import org.junit.Assert;
import org.junit.Test;

public class TransformerImplTest {
    @Test
    public void testBeanShell() {
        Interpreter interpreter = new Interpreter();
        try {
            Class<TransformerImpl> transformerClass = TransformerImpl.class;
            String statements = "import " + transformerClass.getName() + ";";
            interpreter.eval(statements);
            interpreter.eval("transformer = new TransformerImpl();");
            TransformerImpl transformer = transformerClass.cast(interpreter.get("transformer"));
            Assert.assertNotNull(transformer);
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }
    }
}