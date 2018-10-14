package com.github.tamurashingo.jscheme;

public class Pair extends LObj {

    private LObj p1;
    private LObj p2;

    public Pair(LObj p1, LObj p2) {
        super(TYPE.PAIR);
        this.p1 = p1;
        this.p2 = p2;
    }

    public static Pair of(LObj p1, LObj p2) {
        return new Pair(p1, p2);
    }

    /**
     * どちらも空オブジェクトの場合はNILと判断する
     * @return
     */
    @Override
    public boolean isNil() {
        return p1.isNil() &&p2.isNil();
    }

    @Override
    public LObj car() throws JSchemeRuntimeException {
        return p1;
    }

    @Override
    public LObj cdr() throws JSchemeRuntimeException {
        return p2;
    }

    public void setCar(LObj p1) {
        this.p1 = p1;
    }

    public void setCdr(LObj p2) {
        this.p2 = p2;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("(");
        toStringInternal(buf, this);
        buf.append(")");
        return buf.toString();
    }

    private StringBuilder toStringInternal(StringBuilder buf, LObj obj) {
        if (obj.isNil()) {
            buf.append(obj.toString());
        }
        if (obj.isPair()) {
            buf.append(obj.car().toString());

            if (obj.cdr().isNil()) {
                // cdrがNILなら表示しない
            }
            else if (obj.cdr().isAtom()) {
                // cdrがAtomならドットで表示する
                buf.append(" . ");
                toStringInternal(buf, obj.cdr());
            }
            else {
                // cdrがPairならリストで表示する
                buf.append(" ");
                toStringInternal(buf, obj.cdr());
            }
        }
        else if (obj.isAtom()) {
            buf.append(obj.toString());
        }
        else {
            // 将来、TYPEが増えた時にここに追加する
            buf.append(obj.toString());
        }

        return buf;
    }
}

