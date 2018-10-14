package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.command.*;
import com.github.tamurashingo.jscheme.util.ListUtil;


public class Initialize {

    public static Environment initEnvironment() {
        Environment env = Environment.createGlobal();

        env.setVariable("true", Atom.ofValue(1));
        env.setVariable("false", Atom.ofValue(0));
        env.setVariable("car", ListUtil.list(Atom.ofSymbol("primitive"), new Atom(LObj.TYPE.OTHER, CarCommand.getCommand())));
        env.setVariable("cdr", ListUtil.list(Atom.ofSymbol("primitive"), new Atom(LObj.TYPE.OTHER, CdrCommand.getCommand())));
        env.setVariable("cons", ListUtil.list(Atom.ofSymbol("primitive"), new Atom(LObj.TYPE.OTHER, ConsCommand.getCommand())));
        env.setVariable("+", ListUtil.list(Atom.ofSymbol("primitive"), new Atom(LObj.TYPE.OTHER, PlusCommand.getCommand())));
        env.setVariable("-", ListUtil.list(Atom.ofSymbol("primitive"), new Atom(LObj.TYPE.OTHER, MinusCommand.getCommand())));

        return env;
    }


}
