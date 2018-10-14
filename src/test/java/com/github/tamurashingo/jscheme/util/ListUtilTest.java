package com.github.tamurashingo.jscheme.util;


import static org.assertj.core.api.Assertions.*;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

public class ListUtilTest {

    @Test
    public void testCopy() {
        Pair src = new Pair(new Atom(LObj.TYPE.STRING, "this"),
                            new Pair(new Atom(LObj.TYPE.STRING, "is"),
                                     new Pair(new Atom(LObj.TYPE.STRING, "a"),
                                              new Pair(new Atom(LObj.TYPE.STRING, "pen"),
                                                       LObj.NIL()))));
        Pair result = ListUtil.copy(src);

        assertThat(result).isNotSameAs(src);

        assertThat(result.car().isAtom()).isTrue();
        assertThat((String)((Atom)result.car()).get()).isEqualTo("this");

        assertThat(result.cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().car()).get()).isEqualTo("is");

        assertThat(result.cdr().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().cdr().car()).get()).isEqualTo("a");

        assertThat(result.cdr().cdr().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().cdr().cdr().car()).get()).isEqualTo("pen");
    }


    @Test
    public void testList() {
        Pair list = ListUtil.list(Atom.ofString("string1"), Atom.ofString("string2"), Atom.ofSymbol("symbol3"), Atom.ofValue(3.14));

        assertThat(list.isPair()).isTrue();

        assertThat(list.car().isAtom()).isTrue();
        assertThat(list.car().type()).isEqualTo(LObj.TYPE.STRING);
        assertThat((String)((Atom)list.car()).get()).isEqualTo("string1");

        assertThat(list.cdr().car().isAtom()).isTrue();
        assertThat(list.cdr().car().type()).isEqualTo(LObj.TYPE.STRING);
        assertThat((String)((Atom)list.cdr().car()).get()).isEqualTo("string2");

        assertThat(list.cdr().cdr().car().isAtom()).isTrue();
        assertThat(list.cdr().cdr().car().type()).isEqualTo(LObj.TYPE.SYMBOL);
        assertThat((String)((Atom)list.cdr().cdr().car()).get()).isEqualTo("SYMBOL3");

        assertThat(list.cdr().cdr().cdr().car().isAtom()).isTrue();
        assertThat(list.cdr().cdr().cdr().car().type()).isEqualTo(LObj.TYPE.VALUE);
        assertThat((Double)((Atom)list.cdr().cdr().cdr().car()).get()).isEqualTo(3.14, Offset.offset(0.01));

        assertThat(list.cdr().cdr().cdr().cdr().car().isNil()).isTrue();
    }

    @Test
    public void testList2() {
        Pair list = ListUtil.list(ListUtil.list(Atom.ofString("string1"), Atom.ofString("string2")),
                                  ListUtil.list(Atom.ofString("string3")));

        assertThat(list.isPair()).isTrue();

        assertThat(list.car().isPair()).isTrue();

        assertThat(list.car().car().isAtom()).isTrue();
        assertThat((String)((Atom)list.car().car()).get()).isEqualTo("string1");

        assertThat(list.car().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)list.car().cdr().car()).get()).isEqualTo("string2");

        assertThat(list.cdr().car().car().isAtom()).isTrue();
        assertThat((String)((Atom)list.cdr().car().car()).get()).isEqualTo("string3");

        assertThat(list.cdr().car().cdr().isNil()).isTrue();
    }

    @Test
    public void testList3() {
        Pair list = ListUtil.list();

        assertThat(list).isNotNull();
        assertThat(list.isNil()).isTrue();
    }

    @Test
    public void testEqualsAllString() {
        LObj o1 = Atom.ofString("STRING1");
        LObj o2 = Atom.ofString("STRING1");
        LObj o3 = Atom.ofString("string1");
        LObj o4 = Atom.ofSymbol("STRING1");

        // 別インスタンス
        assertThat(o1).isNotSameAs(o2);
        // 中身を見て同じか判断する
        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();

        assertThat(o1).isNotSameAs(o3);
        // STRINGは大文字小文字は区別する
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();

        assertThat(o1).isNotSameAs(o4);
        // シンボルは文字列が同じでもタイプが違うのでequalと判別されない
        assertThat(ListUtil.equalsAll(o1, o4)).isFalse();
    }

    @Test
    public void testEqualsAllSymbol() {
        LObj o1 = Atom.ofSymbol("SYMBOL1");
        LObj o2 = Atom.ofSymbol("symbol1");
        LObj o3 = Atom.ofString("STRING1");

        assertThat(o1).isNotSameAs(o2);
        // シンボルは大文字小文字を区別しない
        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();

        assertThat(o1).isNotSameAs(o3);
        // 文字列とは一致しない
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
    }


    @Test
    public void testEqualsAllValue() {
        LObj o1 = Atom.ofValue(1);
        LObj o2 = Atom.ofValue(1);
        LObj o3 = Atom.ofValue(1.0);

        // 別インスタンス
        assertThat(o1).isNotSameAs(o2);
        // 同じ値
        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();

        assertThat(o1).isNotSameAs(o3);
        // longとdoubleの違い
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
    }

    @Test
    public void testEqualsAllNil() {
        LObj o1 = LObj.NIL();
        LObj o2 = LObj.NIL();
        LObj o3 = Pair.of(LObj.NIL(), LObj.NIL());

        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();
        // PairのどちらもNILの場合、NILと判定する
        assertThat(ListUtil.equalsAll(o1, o3)).isTrue();

        // PairのNILとNILを比較
        assertThat(ListUtil.equalsAll(o3, o1)).isTrue();
    }

    @Test
    public void testEqualsAllPair() {
        LObj o1 = Pair.of(Atom.ofString("car"), LObj.NIL());
        LObj o2 = Pair.of(Atom.ofString("car"), LObj.NIL());
        LObj o3 = Pair.of(Atom.ofSymbol("car"), LObj.NIL());

        assertThat(o1).isNotSameAs(o2);
        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();

        assertThat(o1).isNotSameAs(o3);
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
    }

    @Test
    public void testEquqlsAllPair2() {
        LObj o1 = Pair.of(LObj.NIL(), Atom.ofString("cdr"));
        LObj o2 = Pair.of(LObj.NIL(), Atom.ofString("cdr"));
        LObj o3 = Pair.of(LObj.NIL(), Atom.ofSymbol("cdr"));

        assertThat(o1).isNotSameAs(o2);
        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();

        assertThat(o1).isNotSameAs(o3);
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
    }

    @Test
    public void testEqualsAllPair3() {
        LObj o1 = Pair.of(Pair.of(Atom.ofSymbol("s1"), Atom.ofSymbol("s2")), Pair.of(LObj.NIL(), Atom.ofString("s3")));
        LObj o2 = Pair.of(Pair.of(Atom.ofSymbol("s1"), Atom.ofSymbol("s2")), Pair.of(LObj.NIL(), Atom.ofString("s3")));
        LObj o3 = Pair.of(Pair.of(Atom.ofString("s1"), Atom.ofSymbol("s2")), Pair.of(LObj.NIL(), Atom.ofString("s3")));
        LObj o4 = Pair.of(Pair.of(Atom.ofSymbol("s1"), Atom.ofString("s2")), Pair.of(LObj.NIL(), Atom.ofString("s3")));
        LObj o5 = Pair.of(Pair.of(Atom.ofSymbol("s1"), Atom.ofSymbol("s2")), Pair.of(LObj.NIL(), Atom.ofSymbol("s3")));

        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o4)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o5)).isFalse();
    }

    @Test
    public void testEqualsAllList() {
        LObj o1 = ListUtil.list(Atom.ofString("s1"), Atom.ofString("s2"), Atom.ofString("s3"), Atom.ofString("s4"), Atom.ofString("s5"));
        LObj o2 = ListUtil.list(Atom.ofString("s1"), Atom.ofString("s2"), Atom.ofString("s3"), Atom.ofString("s4"), Atom.ofString("s5"));
        LObj o3 = ListUtil.list(Atom.ofSymbol("s1"), Atom.ofString("s2"), Atom.ofString("s3"), Atom.ofString("s4"), Atom.ofString("s5"));
        LObj o4 = ListUtil.list(Atom.ofString("s1"), Atom.ofSymbol("s2"), Atom.ofString("s3"), Atom.ofString("s4"), Atom.ofString("s5"));
        LObj o5 = ListUtil.list(Atom.ofString("s1"), Atom.ofString("s2"), Atom.ofSymbol("s3"), Atom.ofString("s4"), Atom.ofString("s5"));
        LObj o6 = ListUtil.list(Atom.ofString("s1"), Atom.ofString("s2"), Atom.ofString("s3"), Atom.ofSymbol("s4"), Atom.ofString("s5"));
        LObj o7 = ListUtil.list(Atom.ofString("s1"), Atom.ofString("s2"), Atom.ofString("s3"), Atom.ofString("s4"), Atom.ofSymbol("s5"));

        assertThat(ListUtil.equalsAll(o1, o2)).isTrue();
        assertThat(ListUtil.equalsAll(o1, o3)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o4)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o5)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o6)).isFalse();
        assertThat(ListUtil.equalsAll(o1, o7)).isFalse();
    }

    @Test
    public void testLength() {
        assertThat(ListUtil.length(LObj.NIL())).isEqualTo(0);
        assertThat(ListUtil.length(Pair.of(LObj.NIL(), LObj.NIL()))).isEqualTo(0);

        assertThat(ListUtil.length(Atom.ofString(""))).isEqualTo(1);

        assertThat(ListUtil.length(ListUtil.list(Atom.ofString(""), Atom.ofString(""), Atom.ofString("")))).isEqualTo(3);

    }

}
