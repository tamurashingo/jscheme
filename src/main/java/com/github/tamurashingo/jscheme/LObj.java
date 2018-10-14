package com.github.tamurashingo.jscheme;

/**
 * ベースオブジェクト
 */
public abstract class LObj {

    /** NILインスタンスはシングルトンとして管理する */
    private static LObj NILInstance = new LObj(TYPE.NIL) {
        @Override
        public LObj car() throws JSchemeRuntimeException {
            throw new JSchemeRuntimeException("NIL doesn't support car()");
        }

        @Override
        public LObj cdr() throws JSchemeRuntimeException {
            throw new JSchemeRuntimeException("NIL doesn't support cdr()");
        }

        @Override
        public String toString() {
            return "NIL";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof LObj) {
                LObj lo = (LObj)obj;
                return lo.isNil();
            }
            else {
                return false;
            }
        }
    };

    /**
     *  NILインスタンスを取得する
     */
    public static LObj NIL() {
        return NILInstance;
    }

    protected TYPE type;

    public LObj(TYPE type) {
        this.type = type;
    }

    final public TYPE type() {
        return type;
    }

    public boolean isAtom() {
        return type == TYPE.SYMBOL
                || type == TYPE.STRING
                || type == TYPE.VALUE;
    }
    public boolean isPair() {
        return type == TYPE.PAIR
                || type == TYPE.NIL;
    }

    public boolean isNil() {
        return type == TYPE.NIL;
    }


    abstract public LObj car() throws JSchemeRuntimeException;
    abstract public LObj cdr() throws JSchemeRuntimeException;

    public enum TYPE {
        SYMBOL,
        STRING,
        VALUE,
        OTHER,
        PAIR,
        NIL,
    }
}
