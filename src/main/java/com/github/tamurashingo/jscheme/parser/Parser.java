/*-
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 tamura shingo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.tamurashingo.jscheme.parser;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.JSchemeException;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import com.github.tamurashingo.jscheme.util.ListUtil;

import static com.github.tamurashingo.jscheme.parser.Lexer.LLType;
import static com.github.tamurashingo.jscheme.parser.Lexer.LLType.TYPE.*;

import java.io.BufferedReader;
import java.io.IOException;

public class Parser {

    private Lexer lexer;

    public Parser(String s) {
        this.lexer = new Lexer(s);
    }

    public LObj parse() throws JSchemeException {
        try {
            return parseBase();
        }
        catch (IOException ex) {
            throw new JSchemeException("読み込みエラーが発生しました", ex);
        }
    }

    /**
     * パース
     * ( -> リストのパースへ
     * STRING -> 文字列
     * SYMBOL -> シンボル
     * NUMBER -> 数字
     *
     * @return
     * @throws IOException
     */
    private LObj parseBase() throws JSchemeException, IOException {
        LLType type = lexer.lex();
        if (type.getType() == OPEN_PARENTHESIS) {
            return parseList();
        } else if (type.getType() == STRING) {
            return new Atom(LObj.TYPE.STRING, type.getObject());
        } else if (type.getType() == SYMBOL) {
            return new Atom(LObj.TYPE.SYMBOL, type.getObject());
        } else if (type.getType() == NUMBER) {
            String valStr = type.getObject();
            if (valStr.contains(".")) {
                return new Atom(LObj.TYPE.VALUE, Double.valueOf(valStr));
            } else {
                return new Atom(LObj.TYPE.VALUE, Long.valueOf(valStr));
            }
        } else if (type.getType() == QUOTE) {
            return parseQuote();
        } else {
            throw new JSchemeException("parse error:" + type.getObject());
        }
    }

    private LObj parseList() throws JSchemeException, IOException {
        LObj obj = new Pair(LObj.NIL(), LObj.NIL());
        for (;;) {
            LLType type = lexer.lex();
            if (type.getType() == CLOSE_PARENTHESIS) {
                return obj;
            }
            else {
                lexer.push(type);
                LObj o2 = parseBase();
                obj = ListUtil.append((Pair)obj, new Pair(o2, LObj.NIL()));
            }
        }
    }

    private LObj parseQuote() throws JSchemeException, IOException {
        LLType type = lexer.lex();
        if (type.getType() == STRING
                || type.getType() == SYMBOL
                || type.getType() == NUMBER) {

            // ATOMは (quote OBJ) の形式にする

            lexer.push(type);
            return new Pair(new Atom(LObj.TYPE.SYMBOL, "QUOTE"),
                            new Pair(parseBase(), LObj.NIL()));
        }
        else if (type.getType() == OPEN_PARENTHESIS) {
            // Pairは (quote (......)) の形式にする
            lexer.push(type);
            return new Pair(new Atom(LObj.TYPE.SYMBOL, "QUOTE"),
                    new Pair(parseBase(), LObj.NIL()));
        }
        else {
            // それ以外はパースエラー
            throw new JSchemeException("parse error in QUOTE: except ATOM or PAIR but " + type.getObject());
        }
    }
}

