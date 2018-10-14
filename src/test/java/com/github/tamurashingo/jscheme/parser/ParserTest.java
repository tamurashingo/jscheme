package com.github.tamurashingo.jscheme.parser;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;


public class ParserTest {

    // 文字列
    @Test
    public void testString() throws Exception {
        Parser parser = new Parser("\"this is a pen\"");
        LObj obj = parser.parse();

        assertThat(obj.isAtom()).isTrue();
        assertThat((String)((Atom)obj).get()).isEqualTo("this is a pen");
    }

    // 整数
    @Test
    public void testLong() throws Exception {
        Parser parser = new Parser("100");
        LObj obj = parser.parse();

        assertThat(obj.isAtom()).isTrue();
        assertThat((Long)((Atom)obj).get()).isEqualTo(100L);
    }

    // 実数
    @Test
    public void testDouble() throws Exception {
        Parser parser = new Parser("-3.14");
        LObj obj = parser.parse();

        assertThat(obj.isAtom()).isTrue();
        assertThat((Double)((Atom)obj).get()).isEqualTo(-3.14, Offset.offset(0.01));
    }

    // シンボル
    @Test
    public void testSymbol1() throws Exception {
        Parser parser = new Parser("VARIABLE");
        LObj obj = parser.parse();

        assertThat(obj.isAtom()).isTrue();
        assertThat((String)((Atom)obj).get()).isEqualTo("VARIABLE");
    }

    // シンボル（小文字は大文字になっているよね確認）
    @Test
    public void testSymbol2() throws Exception {
        Parser parser = new Parser("variable");
        LObj obj = parser.parse();

        assertThat(obj.isAtom()).isTrue();
        assertThat((String)((Atom)obj).get()).isEqualTo("VARIABLE");
    }

    @Test
    public void testList() throws Exception {
        Parser parser = new Parser("(+ x y)");
        LObj obj = parser.parse();

        // toString()
        assertThat(obj.toString()).isEqualTo("(+ X Y)");


        assertThat(obj.isPair()).isTrue();

        assertThat(obj.car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.car()).get()).isEqualTo("+");

        assertThat(obj.cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.cdr().car()).get()).isEqualTo("X");

        assertThat(obj.cdr().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.cdr().cdr().car()).get()).isEqualTo("Y");
    }

    @Test
    public void testList2() throws Exception {
        Parser parser = new Parser("((x 1) (y 2))");
        LObj obj = parser.parse();

        assertThat(obj.toString()).isEqualTo("((X 1) (Y 2))");


        // ((x 1) (y 2))
        assertThat(obj.isPair()).isTrue();

        // (x 1)
        assertThat(obj.car().isPair()).isTrue();
        // x
        assertThat(obj.car().car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.car().car()).get()).isEqualTo("X");
        // 1
        assertThat(obj.car().cdr().car().isAtom()).isTrue();
        assertThat((Long)((Atom)obj.car().cdr().car()).get()).isEqualTo(1L);

        // (y 2)
        assertThat(obj.cdr().car().isPair()).isTrue();
        // y
        assertThat(obj.cdr().car().car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.cdr().car().car()).get()).isEqualTo("Y");
        // 2
        assertThat(obj.cdr().car().cdr().car().isAtom()).isTrue();
        assertThat((Long)((Atom)obj.cdr().car().cdr().car()).get()).isEqualTo(2L);

    }

    @Test
    public void testList3() throws Exception {
        String s = "(let ((x 1)" + "\n"
                 + "      (y 2))" + "\n"
                 + "  (+ x y))"
                ;

        Parser parser = new Parser(s);
        LObj obj = parser.parse();

        assertThat(obj.toString()).isEqualTo("(LET ((X 1) (Y 2)) (+ X Y))");


        // (let ((x 1) (y 2)) (+ x y))
        assertThat(obj.isPair()).isTrue();

        // let
        assertThat(obj.car().isAtom()).isTrue();
        assertThat((String)((Atom)obj.car()).get()).isEqualTo("LET");

        /* -
         * ((x 1) (y 2))
         */
        LObj varDeclare = obj.cdr().car();

        assertThat(varDeclare.isPair()).isTrue();

        // (x 1)
        {
            LObj letx = varDeclare.car();
            assertThat(letx.isPair()).isTrue();
            assertThat(letx.car().isAtom()).isTrue();
            assertThat((String)((Atom)letx.car()).get()).isEqualTo("X");

            assertThat(letx.cdr().car().isAtom()).isTrue();
            assertThat((Long)((Atom)letx.cdr().car()).get()).isEqualTo(1L);
        }
        // (y 2)
        {
            LObj lety = varDeclare.cdr().car();
            assertThat(lety.isPair()).isTrue();
            assertThat(lety.car().isAtom()).isTrue();
            assertThat((String)((Atom)lety.car()).get()).isEqualTo("Y");

            assertThat(lety.cdr().car().isAtom()).isTrue();
            assertThat((Long)((Atom)lety.cdr().car()).get()).isEqualTo(2L);

        }

        LObj forms = obj.cdr().cdr();

        // (+ x y)
        {
            LObj form = forms.car();
            assertThat(form.isPair()).isTrue();

            assertThat((String)((Atom)form.car()).get()).isEqualTo("+");
            assertThat((String)((Atom)form.cdr().car()).get()).isEqualTo("X");
            assertThat((String)((Atom)form.cdr().cdr().car()).get()).isEqualTo("Y");
        }
    }

    @Test
    public void testQuote() throws Exception {
        Parser parser = new Parser("'a");
        LObj obj = parser.parse();

        assertThat(obj.isPair()).isTrue();

        assertThat((String)((Atom)obj.car()).get()).isEqualTo("QUOTE");
        assertThat((String)((Atom)obj.cdr().car()).get()).isEqualTo("A");
    }

    @Test
    public void testQuote2() throws Exception {
        Parser parser = new Parser("'3.14");
        LObj obj = parser.parse();

        assertThat(obj.isPair()).isTrue();

        assertThat((String)((Atom)obj.car()).get()).isEqualTo("QUOTE");
        assertThat((Double)((Atom)obj.cdr().car()).get()).isEqualTo(3.14, Offset.offset(0.01));
    }

    @Test
    public void testQuote3() throws Exception {
        Parser parser = new Parser("'(a b c)");
        LObj obj = parser.parse();

        assertThat(obj.isPair()).isTrue();

        assertThat((String)((Atom)obj.car()).get()).isEqualTo("QUOTE");

        assertThat(obj.cdr().car().isPair()).isTrue();
        assertThat((String)((Atom)obj.cdr().car().car()).get()).isEqualTo("A");
        assertThat((String)((Atom)obj.cdr().car().cdr().car()).get()).isEqualTo("B");
        assertThat((String)((Atom)obj.cdr().car().cdr().cdr().car()).get()).isEqualTo("C");
    }
}
