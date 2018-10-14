package com.github.tamurashingo.jscheme.parser;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;

/**
 * lexer
 */
public class Lexer {

    private final BufferedReader in;

    public Lexer(String s) {
        in = new BufferedReader(new StringReader(s));
    }

    private LLType buf = null;

    /**
     * 1トークン戻す
     * @param type
     */
    public void push(LLType type) {
        this.buf = type;
    }

    public LLType lex() throws IOException {
        // 戻されたトークンがあればそれを吐き出す
        if (this.buf != null) {
            LLType type = this.buf;
            buf = null;
            return type;
        }

        int ch;
        for (;;) {
            ch = in.read();
            if (ch == ' ' || ch == '\t' || ch == '\n') {
                continue;
            }
            else {
                break;
            }
        }

        if (ch == '(') {
            return lexOpenParenthesis(ch);
        }
        else if (ch == ')') {
            return lexCloseParenthesis(ch);
        }
        else if (ch == '\'') {
            return lexQuote(ch);
        }
        else if (ch == '"') {
            return lexString(ch);
        }
        else if (ch == '-') {
            return lexMinus(ch);
        }
        else if (isNumberChar(ch)) {
            return lexNumber(ch);
        }
        else {
            return lexSymbol(ch);
        }
    }

    /**
     * 開きかっこのlexer
     * @param ch
     * @return
     * @throws IOException
     */
    public LLType lexOpenParenthesis(int ch) throws IOException {
        return new LLType(LLType.TYPE.OPEN_PARENTHESIS, "(");
    }

    /**
     * 閉じかっこのlexer
     * @param ch
     * @return
     * @throws IOException
     */
    public LLType lexCloseParenthesis(int ch) throws IOException {
        return new LLType(LLType.TYPE.CLOSE_PARENTHESIS, ")");
    }

    /**
     * Quoteのlexer
     * @param ch
     * @return
     * @throws IOException
     */
    public LLType lexQuote(int ch) throws IOException {
        return new LLType(LLType.TYPE.QUOTE, "'");
    }

    /**
     * 文字列のlexer
     * @param ch
     * @return
     * @throws IOException
     */
    public LLType lexString(int ch) throws IOException {
        StringWriter buf = new StringWriter();
        BufferedWriter w = new BufferedWriter(buf);
        for (;;) {
            ch = in.read();
            if (ch == '"') {
                w.flush();
                return new LLType(LLType.TYPE.STRING, buf.toString());
            }
            w.write(ch);
        }
    }


    /**
     * マイナスのlexer。
     * 直後が数字だったらnumberLexerに移行
     * @param ch
     * @return
     * @throws IOException
     */
    public LLType lexMinus(int ch) throws IOException {
        in.mark(1);
        int c = in.read();
        if (isNumberChar(c)) {
            in.reset();
            return lexNumber(ch);
        }
        else {
            in.reset();
            return new LLType(LLType.TYPE.SYMBOL, "-");
        }
    }

    public LLType lexNumber(int ch) throws IOException {
        StringWriter buf = new StringWriter();
        BufferedWriter w = new BufferedWriter(buf);
        w.write(ch);

        if (ch == '.') {
            in.mark(1);
            ch = in.read();
            if (!isNumberChar(ch)) {
                in.reset();
                w.flush();
                return new LLType(LLType.TYPE.DOT, ".");
            } else {
                w.write(ch);
            }
        }


        for (;;) {
            in.mark(1);
            ch = in.read();
            if (!isNumberChar(ch)) {
                in.reset();
                w.flush();
                return new LLType(LLType.TYPE.NUMBER, buf.toString());
            }
            else {
                w.write(ch);
            }
        }
    }

    public LLType lexSymbol(int ch) throws IOException {

        StringWriter buf = new StringWriter();
        BufferedWriter w = new BufferedWriter(buf);
        w.write(ch);
        for (;;) {
            in.mark(1);
            ch = in.read();
            if (ch == -1
                    || ch == ' '
                    || ch == '\t'
                    || ch == '('
                    || ch == ')'
                    || ch == '\''
                    || ch == '"'
                    || ch == '\n') {
                in.reset();
                w.flush();
                return new LLType(LLType.TYPE.SYMBOL, buf.toString());
            }
            else {
                w.write(ch);
            }
        }
    }


    private boolean isNumberChar(int ch) {
        return ch == '.'
                || ch == '0'
                || ch == '1'
                || ch == '2'
                || ch == '3'
                || ch == '4'
                || ch == '5'
                || ch == '6'
                || ch == '7'
                || ch == '8'
                || ch == '9';
    }


    @Data
    @AllArgsConstructor
    public static class LLType {
        public enum TYPE {
            /**
             * (
             */
            OPEN_PARENTHESIS,
            /**
             * )
             */
            CLOSE_PARENTHESIS,
            /**
             * '
             */
            QUOTE,
            /**
             * .(dot)
             */
            DOT,
            /**
             * "xxxx"
             */
            STRING,
            /**
             * -?[0-9]*.?[0-9]*
             */
            NUMBER,
            /**
             * OTHER
             */
            SYMBOL,
        }
        private final TYPE type;
        private final String object;
    }
}
