package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import com.github.tamurashingo.jscheme.command.Command;
import com.github.tamurashingo.jscheme.command.CommandException;
import com.github.tamurashingo.jscheme.util.ListUtil;

import static com.github.tamurashingo.jscheme.util.ListUtil.taggedList;


public class Apply {

    private Eval eval;

    public Apply(Eval eval) {
        this.eval = eval;
    }

    public LObj apply(LObj procedure, LObj arguments) {
        if (primitiveProcedure(procedure)) {
            return applyPrimitiveProcedure(((Atom)procedure.car()).get(), arguments);
        }
        return null;
    }

    public boolean primitiveProcedure(LObj procedure) {
        return taggedList(procedure, "PRIMITIVE");
    }

    /**
     * commandで定義したprimitive関数を実行する。
     *
     * @param proceddure
     * @param arguments
     * @return
     */
    public LObj applyPrimitiveProcedure(Command command, LObj arguments) {
        try {
            command.operate(arguments);
        }
        catch (CommandException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     *
     * @param exp
     * @param env
     * @return
     */
    public LObj evalAssignment(LObj exp, Environment env) {
        String symbol = getAssignmentVarialbe(exp);
        LObj value = getAssignmentValue(exp, env);

        // 既存の値が現在より上のフレームに存在するなら上書き
        Environment existsEnv = env.lookupEnvironment(symbol);
        if (existsEnv != null) {
            existsEnv.setVariable(symbol, value);
        }
        // 存在しないなら現在のフレームにセットする
        else {
            env.setVariable(symbol, value);
        }

        return new Atom(LObj.TYPE.STRING, "OK!");
    }

    String getAssignmentVarialbe(LObj exp) {
        return ((Atom)exp.cdr().car()).get().toString();
    }
    LObj getAssignmentValue(LObj exp, Environment env) {
        LObj value = exp.cdr().cdr().car();
        return eval.eval(value, env);
    }

    /**
     *
     * @param exp
     * @param env
     * @return
     */
    public LObj evalDefinition(LObj exp, Environment env) {
        String symbol = getDefinitionVariable(exp);
        LObj value = getDefinitionValue(exp);

        // 既存の値が現在より上のフレームに存在するなら上書き
        Environment existsEnv = env.lookupEnvironment(symbol);
        if (existsEnv != null) {
            existsEnv.setVariable(symbol, value);
        }
        // 存在しないなら現在のフレームにセットする
        else {
            env.setVariable(symbol, value);
        }

        return new Atom(LObj.TYPE.STRING, "OK!");
    }

    String getDefinitionVariable(LObj exp) {
        // (define func (lambda (x) (+ x 1)))
        if (exp.cdr().car().isAtom()) {
            return exp.cdr().car().toString();
        }
        // (define (func x) (+ x 1))
        else {
            return exp.cdr().car().car().toString();
        }
    }
    LObj getDefinitionValue(LObj exp) {

        // (define func (lambda (x) (+ x 1)))
        if (exp.cdr().car().isAtom()) {
            return exp.cdr().cdr().car();
        }
        // (define (func x) (+ x 1))
        else {
            return makeLambda(exp.cdr().car().cdr(), exp.cdr().cdr());
        }
    }

    LObj makeLambda(LObj parameters, LObj body) {
        return new Pair(new Atom(LObj.TYPE.SYMBOL, "LAMBDA"),
                        new Pair(parameters, body));
    }

    public LObj evalIf(LObj exp, Environment env) {
        LObj predicate = getIfPredicate(exp);

        if (!eval.eval(getIfPredicate(exp), env).isNil()) {
            return eval.eval(getIfConsequent(exp), env);
        }
        else {
            return eval.eval(getIfAlternative(exp), env);
        }
    }

    LObj getIfPredicate(LObj exp) {
        return exp.cdr().car();
    }

    LObj getIfConsequent(LObj exp) {
        return exp.cdr().cdr().car();
    }

    LObj getIfAlternative(LObj exp) {
        if (!exp.cdr().cdr().cdr().isNil()) {
            return exp.cdr().cdr().cdr().car();
        }
        else {
            return LObj.NIL();
        }
    }

    public LObj evalMakeProcedure(LObj exp, Environment env) {;
        return makeProcedure(getLambdaParameters(exp), getLambdaBody(exp), env);
    }

    LObj getLambdaParameters(LObj exp) {
        // (lambda (x y) (+ x y))
        return exp.cdr().car();
    }

    LObj getLambdaBody(LObj exp) {
        // (lambda (x y) (+ x y))
        return exp.cdr().cdr();
    }

    public LObj makeProcedure(LObj parameters, LObj body, Environment env) {
        return ListUtil.list(Atom.ofSymbol("PROCEDURE"), parameters, body, new Atom(LObj.TYPE.OTHER, env));
    }

    public LObj evalSequence(LObj exp, Environment env) {
        if (ListUtil.isLast(exp)) {
            return eval.eval(ListUtil.firstList(exp), env);
        }
        else {
            eval.eval(ListUtil.firstList(exp), env);
            return evalSequence(ListUtil.restList(exp), env);
        }
    }

}
