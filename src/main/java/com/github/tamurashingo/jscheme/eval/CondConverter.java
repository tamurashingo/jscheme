package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.JSchemeRuntimeException;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import com.github.tamurashingo.jscheme.util.ListUtil;

/**
 * cond式をif式に変換する。
 *
 * <pre>
 * {@code
 * (cond ((> x 0) x)
 *       ((= x 0) (begin (display 'zero)
 *                       0))
 *       (else (- x)))
 *  =>
 * (if (> x 0)
 *     x
 *     (if (= x 0)
 *         (begin (display 'zero)
 *                0)
 *         (- x)))
 * }
 * </pre>
 *
 */
public class CondConverter {

    public static LObj condToIf(LObj exp) {
        return expandClauses(getCondClauses(exp));
    }

    static LObj getCondClauses(LObj clause) {
        return clause.cdr();
    }


    static LObj getCondPredicate(LObj clause) {
        return clause.car();
    }
    static LObj getCondActions(LObj clause) {
        return clause.cdr();
    }

    static boolean isCondElseClause(LObj clause) {
        LObj result = getCondPredicate(clause);
        return result.isAtom() && result.type().equals(LObj.TYPE.SYMBOL) && ((Atom)result).get().equals("ELSE");
    }

    static LObj expandClauses(LObj clauses) {
        if (clauses.isNil()) {
            return LObj.NIL();
        }
        else {
            LObj first = ListUtil.firstList(clauses);
            LObj rest = ListUtil.restList(clauses);
            if (isCondElseClause(first)) {
                if (rest.isNil()) {
                    return sequenceToExp(getCondActions(first));
                }
                else {
                    throw new JSchemeRuntimeException("cond parse error: else must be last");
                }
            }
            else {
                return makeIf(getCondPredicate(first), sequenceToExp(getCondActions(first)), expandClauses(rest));
            }
        }
    }

    static LObj makeIf(LObj predicate, LObj consequence, LObj alternative) {
        return new Pair(new Atom(LObj.TYPE.SYMBOL, "IF"),
                        new Pair(predicate,
                                 new Pair(consequence,
                                          new Pair(alternative, LObj.NIL()))));
    }

    static boolean isLastExp(LObj seq) {
        return seq.cdr().isNil();
    }

    static LObj getFirstExp(LObj seq) {
        return seq.car();
    }

    static LObj sequenceToExp(LObj seq) {
        if (seq.isNil()) {
            return seq;
        }
        else if (isLastExp(seq)) {
            return getFirstExp(seq);
        }
        else {
            // (cons 'begin seq)
            return new Pair(new Atom(LObj.TYPE.SYMBOL, "BEGIN"), seq);
        }
    }
}
