package com.github.tamurashingo.jscheme.parser;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LexerTest {


    @Test
    public void testLexOpen() throws Exception {
        String line = "(";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);
        assertThat(result.getObject()).isEqualTo("(");
    }

    @Test
    public void testLexClose() throws Exception {
        String line = ")";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);
        assertThat(result.getObject()).isEqualTo(")");
    }

    @Test
    public void testLexQuote() throws Exception {
        String line = "'";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.QUOTE);
        assertThat(result.getObject()).isEqualTo("'");
    }

    @Test
    public void testLexString() throws Exception {
        String line = "\"this is a pen.\"";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.STRING);
        assertThat(result.getObject()).isEqualTo("this is a pen.");
    }

    /**
     * dot
     * @throws Exception
     */
    @Test
    public void testDot() throws Exception {
        String line = ".";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.DOT);
        assertThat(result.getObject()).isEqualTo(".");
    }

    /**
     * ex: (cons 1 .()) => (cons 1 . ()) => (1)
     * @throws Exception
     */
    @Test
    public void testDot2() throws Exception {
        String line = ".(";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.DOT);
        assertThat(result.getObject()).isEqualTo(".");
    }

    /**
     * minus operator
     * @throws Exception
     */
    @Test
    public void testMinus() throws Exception {
        String line = "-";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("-");
    }

    /**
     * minus variable
     * @throws Exception
     */
    @Test
    public void testMinus2() throws Exception {
        String line = "-a";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("-");
    }

    /**
     * integer
     * @throws Exception
     */
    @Test
    public void testNumber() throws Exception {
        String line = "1";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("1");
    }

    /**
     * float
     * @throws Exception
     */
    @Test
    public void testNumber2() throws Exception {
        String line = "1.0";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("1.0");
    }

    /**
     * float (omit leading zero)
     * @throws Exception
     */
    @Test
    public void testNumber3() throws Exception {
        String line = ".5";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo(".5");
    }

    /**
     * minus integer
     * @throws Exception
     */
    @Test
    public void testNumber4() throws Exception {
        String line = "-20";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("-20");
    }

    /**
     * minus float
     * @throws Exception
     */
    @Test
    public void testNumber5() throws Exception {
        String line = "-3.4";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("-3.4");
    }

    /**
     * minus float (omit leading zero)
     * @throws Exception
     */
    @Test
    public void testNumbert6() throws Exception {
        String line = "-.2";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("-.2");
    }

    @Test
    public void testSymbol() throws Exception {
        String line = "abc";
        Lexer lex = new Lexer(line);
        Lexer.LLType result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("abc");
    }

    @Test
    public void testSExp() throws Exception {
        String lines = "(let ((x 1)" + "\n"
                     + "      (y 2))" + "\n"
                     + "  (+ x y))"
                ;
        Lexer lex = new Lexer(lines);

        Lexer.LLType result;

        // (
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);

        // let
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("let");

        // (
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);

        // (
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);

        // x
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("x");

        // 1
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("1");

        // )
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);

        // (
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);

        // y
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("y");

        // 1
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.NUMBER);
        assertThat(result.getObject()).isEqualTo("2");

        // )
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);

        // )
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);

        // (
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.OPEN_PARENTHESIS);

        // +
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("+");

        // x
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("x");

        // y
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.SYMBOL);
        assertThat(result.getObject()).isEqualTo("y");

        // )
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);

        // )
        result = lex.lex();
        assertThat(result.getType()).isEqualTo(Lexer.LLType.TYPE.CLOSE_PARENTHESIS);
    }
}
