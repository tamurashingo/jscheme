package com.github.tamurashingo.jscheme.eval;

import static com.github.tamurashingo.jscheme.LObj.TYPE.*;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.JSchemeRuntimeException;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;

import static com.github.tamurashingo.jscheme.util.ListUtil.taggedList;

public class Eval {

    public Apply apply = new Apply(this);

    public LObj eval(LObj exp, Environment env) throws JSchemeRuntimeException {
        if (selfEvaluating(exp)) {
            return exp;
        }
        else if (variable(exp)) {
            return env.lookup(((Atom)exp).get());
        }
        else if (quoted(exp)) {
            return exp.cdr().car();
        }
        else if (assignment(exp)) {
            return apply.evalAssignment(exp, env);
        }
        else if (definition(exp)) {
            return apply.evalDefinition(exp, env);
        }
        else if (ifexp(exp)) {
            return apply.evalIf(exp, env);
        }
        else if (lambda(exp)) {
            return apply.evalMakeProcedure(exp, env);
        }
        else if (begin(exp)) {
            return apply.evalSequence(exp.cdr(), env);
        }
        else if (cond(exp)) {
            return eval(CondConverter.condToIf(exp), env);
        }
        else if (application(exp)) {
            return apply.apply(eval(getOperator(exp), env),
                    getListOfValues(getOperands(exp), env));
        }
        else {
            throw new JSchemeRuntimeException("unknown expression:" + exp.toString());
        }
    }


    public boolean selfEvaluating(LObj exp) {
        return exp.type() == VALUE || exp.type() == STRING || exp.type() == NIL;
    }
    public boolean variable(LObj exp) {
        return exp.type() == SYMBOL;
    }
    public boolean quoted(LObj exp) {
        return taggedList(exp, "QUOTE");
    }
    public boolean assignment(LObj exp) {
        return taggedList(exp, "SET!");
    }
    public boolean definition(LObj exp) {
        return taggedList(exp, "DEFINE");
    }
    public boolean ifexp(LObj exp) {
        return taggedList(exp, "IF");
    }
    public boolean lambda(LObj exp) {
        return taggedList(exp, "LAMBDA");
    }
    public boolean begin(LObj exp) {
        return taggedList(exp, "BEGIN");
    }
    public boolean cond(LObj exp) {
        return taggedList(exp, "COND");
    }
    public boolean application(LObj exp) {
        return exp.isPair();
    }

    LObj getOperator(LObj exp) {
        return exp.car();
    }
    LObj getOperands(LObj exp) {
        return exp.cdr();
    }

    LObj getListOfValues(LObj exp, Environment env) {
        if (exp.isNil()) {
            return LObj.NIL();
        }
        else {
            return new Pair(eval(getFirstOperand(exp), env),
                            getListOfValues(getRestOperands(exp), env));
        }
    }

    LObj getFirstOperand(LObj ops) {
        return ops.car();
    }

    LObj getRestOperands(LObj ops) {
        return ops.cdr();
    }

}
