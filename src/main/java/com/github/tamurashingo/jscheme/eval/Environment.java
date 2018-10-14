package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.JSchemeException;
import com.github.tamurashingo.jscheme.JSchemeRuntimeException;
import com.github.tamurashingo.jscheme.LObj;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private Environment parent;

    private Map<String, LObj> variables = new HashMap<>();

    public static Environment createGlobal() {
        return new Environment();
    }


    private Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void setVariable(String symbol, LObj value) {
        Environment env = lookupEnvironment(symbol);
        if (env == null) {
            this.variables.put(symbol, value);
        }
        else {
            env.variables.put(symbol, value);
        }
    }

    public LObj getVariable(String symbol) {
        return variables.get(symbol);
    }

    public LObj lookup(String symbol) throws JSchemeRuntimeException {
        if (variables.containsKey(symbol)) {
            return getVariable(symbol);
        }
        else {
            if (parent != null) {
                return parent.lookup(symbol);
            }
            else {
                throw new JSchemeRuntimeException("not found variable:" + symbol);
            }
        }
    }

    public Environment lookupEnvironment(String symbol) {
        if (variables.containsKey(symbol)) {
            return this;
        }
        else {
            if (parent != null) {
                return parent.lookupEnvironment(symbol);
            }
            else {
                return null;
            }
        }
    }

}
