package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.parser.Parser;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class EvalTest {

    @Test
    public void testNumber() throws Exception {
        String source = "1";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat(result.toString()).isEqualTo("1");
    }

    @Test
    public void testString() throws Exception {
        String source = "\"This is a pen\"";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat(result.toString()).isEqualTo("\"This is a pen\"");
    }

    @Test
    public void testVariable() throws Exception {
        String source = "x";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();
        env.setVariable("X", new Atom(LObj.TYPE.VALUE, 1L));

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat((Long)((Atom)result).get()).isEqualTo(1L);
    }

    @Test
    public void testQuote() throws Exception {
        String source = "'x";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat(result.type()).isEqualTo(LObj.TYPE.SYMBOL);
        assertThat((String)((Atom)result).get()).isEqualTo("X");
    }

    @Test
    public void testQuote2() throws Exception {
        String source = "'(x y)";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isPair()).isTrue();

        assertThat(result.car().isAtom()).isTrue();
        assertThat((String)((Atom)result.car()).get()).isEqualTo("X");

        assertThat(result.cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().car()).get()).isEqualTo("Y");
    }

    @Test
    public void testAssign() throws Exception {
        String source = "(set! x 2)";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        String source2 = "x";
        parser = new Parser(source2);

        exp = parser.parse();
        result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat((Long)((Atom)result).get()).isEqualTo(2L);
    }


    @Test
    public void testIf() throws Exception {
        String source = "(if 1 \"OK\" \"NG\")";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat((String)((Atom)result).get()).isEqualTo("OK");
    }

    @Test
    public void testIf2() throws Exception {
        String source = "(if '() \"OK\" \"NG\")";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        assertThat(result.isAtom()).isTrue();
        assertThat((String)((Atom)result).get()).isEqualTo("NG");
    }

    @Test
    public void testLambda() throws Exception {
        String source = "(lambda (x) (+ x 1))";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);

        // procedure tag
        assertThat((String)((Atom)result.car()).get()).isEqualTo("PROCEDURE");


        // parameter
        {
            LObj parameter = result.cdr().car();
            assertThat(parameter.isPair()).isTrue();
            assertThat(parameter.car().type()).isEqualTo(LObj.TYPE.SYMBOL);
            assertThat((String)((Atom)parameter.car()).get()).isEqualTo("X");
            assertThat(parameter.cdr().isNil()).isTrue();
        }

        // body
        {
            LObj body = result.cdr().cdr().car();
            assertThat(body.isPair()).isTrue();

            LObj body1 = body.car();
            assertThat(body1.car().type()).isEqualTo(LObj.TYPE.SYMBOL);
            assertThat((String)((Atom)body1.car()).get()).isEqualTo("+");

            assertThat(body1.cdr().car().type()).isEqualTo(LObj.TYPE.SYMBOL);
            assertThat((String)((Atom)body1.cdr().car()).get()).isEqualTo("X");

            assertThat(body1.cdr().cdr().car().type()).isEqualTo(LObj.TYPE.VALUE);
            assertThat((Long)((Atom)body1.cdr().cdr().car()).get()).isEqualTo(1L);

            LObj body2 = body.cdr();
            assertThat(body2.isNil()).isTrue();
        }
    }

    @Test
    public void testBegin() throws Exception {
        String source = "(begin 1)";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);
        assertThat(result.isAtom()).isTrue();
        assertThat((Long)((Atom)result).get()).isEqualTo(1L);
    }

    @Test
    public void testBegin2() throws Exception {
        String source = "(begin 1 \"OK\")";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Environment.createGlobal();

        LObj result = eval.eval(exp, env);
        assertThat(result.isAtom()).isTrue();
        assertThat((String)((Atom)result).get()).isEqualTo("OK");
    }

    @Test
    public void testApply() throws Exception {
        String source = "(+ 1 2)";
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        Eval eval = new Eval();
        Environment env = Initialize.initEnvironment();

        LObj result = eval.eval(exp, env);
        assertThat(result.isAtom()).isTrue();
        assertThat(result.type()).isEqualTo(LObj.TYPE.VALUE);
        assertThat((Long)((Atom)result).get()).isEqualTo(3);
    }



}
