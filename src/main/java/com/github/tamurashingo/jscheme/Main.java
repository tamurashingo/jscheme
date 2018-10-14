package com.github.tamurashingo.jscheme;

import com.github.tamurashingo.jscheme.eval.Environment;
import com.github.tamurashingo.jscheme.eval.Eval;
import com.github.tamurashingo.jscheme.eval.Initialize;
import com.github.tamurashingo.jscheme.parser.Parser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String...args) throws Exception {
        Eval eval = new Eval();
        Environment env = Initialize.initEnvironment();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            for (;;) {
                System.out.print("> ");
                String s = new String(in.readLine());
                Parser parser = new Parser(s);
                LObj exp = parser.parse();
                LObj result = eval.eval(exp, env);
                System.out.println(result.toString());
            }
        }
    }
}
