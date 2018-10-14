package com.github.tamurashingo.jscheme;

import java.util.Objects;

public class Atom extends LObj {

    private final Object value;
    private SUBTYPE subtype;

    public Atom(TYPE type, Object value) {
        super(type);
        if (type == TYPE.SYMBOL) {
            this.value = ((String)value).toUpperCase();
        }
        else if (type == TYPE.STRING) {
            this.value = value;
        }
        else if (type == TYPE.VALUE){
            if (value instanceof Long || value instanceof Integer) {
                this.subtype = SUBTYPE.LONG;
                this.value = Long.valueOf(String.valueOf(value));
            }
            else {
                this.subtype = SUBTYPE.DOUBLE;
                this.value = Double.valueOf(String.valueOf(value));
            }
        }
        else {
            this.value = value;
        }
    }

    public static Atom ofString(Object value) {
        return new Atom(TYPE.STRING, value);
    }

    public static Atom ofSymbol(Object value) {
        return new Atom(TYPE.SYMBOL, ((String)value).toUpperCase());
    }

    public static Atom ofValue(Object value) {
        return new Atom(TYPE.VALUE, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return (T)value;
    }

    public SUBTYPE getSubtype() {
        return subtype;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Atom) {
            Atom atom = (Atom)obj;
            return atom.type == this.type && Objects.equals(atom.value, this.value);
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public LObj car() throws JSchemeRuntimeException {
        throw new JSchemeRuntimeException("Atom car doesn't support car()");
    }

    @Override
    public LObj cdr() throws JSchemeRuntimeException {
        throw new JSchemeRuntimeException("Atom doesn't support cdr()");
    }

    @Override
    public String toString() {
        if (this.type == TYPE.STRING) {
            return String.format("\"%s\"", this.value);
        }
        else {
            return value.toString();
        }
    }

    public enum SUBTYPE {
        LONG,
        DOUBLE,
    }
}
