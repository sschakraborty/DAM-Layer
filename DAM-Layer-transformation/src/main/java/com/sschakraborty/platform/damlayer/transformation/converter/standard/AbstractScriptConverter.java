package com.sschakraborty.platform.damlayer.transformation.converter.standard;

import com.sschakraborty.platform.damlayer.transformation.converter.ScriptConverter;

public abstract class AbstractScriptConverter implements ScriptConverter {
    private String script = "";

    @Override
    public final String getScript() {
        return this.script;
    }

    @Override
    public final void setScript(String script) {
        this.script = (script == null) ? this.script : script;
    }
}