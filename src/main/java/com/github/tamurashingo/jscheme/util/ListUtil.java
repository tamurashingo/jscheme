package com.github.tamurashingo.jscheme.util;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;

public class ListUtil {

    private ListUtil() {
    }

    /**
     * 与えられたリストから新しいリストを作成する。
     * shallow copyを行う。
     *
     * @param list リスト
     * @return 新しいリスト
     */
    public static Pair copy(Pair list) {
        LObj src = list;
        Pair start = new Pair(LObj.NIL(), LObj.NIL());
        Pair current = start;
        for (;;) {
            if (src.isNil()) {
                return start;
            }
            else {
                current.setCar(src.car());
                Pair cdr = new Pair(LObj.NIL(), LObj.NIL());
                current.setCdr(cdr);
                src = src.cdr();
                current = cdr;
            }
        }
    }

    /**
     * l1の最後にl2を追加した新しいリストを返す。
     *
     * @param l1 追加先のリスト
     * @param l2 追加するリスト
     * @return
     */
    public static Pair append(Pair l1, Pair l2) {
        if (l1.isNil()) {
            return l2;
        }
        else {
            Pair start = ListUtil.copy(l1);
            Pair p = start;
            for (;;) {
                if (p.cdr().isNil()) {
                    p.setCdr(l2);
                    break;
                }
                else {
                    p = (Pair)p.cdr();
                }
            }
            return start;
        }
    }

    public static Pair list(LObj...objs) {
        Pair pair = Pair.of(LObj.NIL(), LObj.NIL());
        Pair start = pair;
        for (LObj o: objs) {
            pair.setCar(o);
            Pair r = Pair.of(LObj.NIL(), LObj.NIL());
            pair.setCdr(r);
            pair = r;
        }
        return start;
    }

    /**
     * リストが指定されたタグで開始しているかをチェックする。
     *
     * @param exp リスト(Pair)
     * @param tag タグ(Symbol)
     * @return リストの先頭が指定されたタグの場合true
     */
    public static boolean taggedList(LObj exp, String tag) {
        return exp.isPair() && exp.car().isAtom() && ((Atom)exp.car()).get().equals(tag);
    }

    /**
     * リストの末尾かどうかをチェックする。
     * リストのcdr()がNILなら末尾と判定する。
     *
     * @param exp リスト
     * @return 末尾の場合true
     */
    public static boolean isLast(LObj exp) {
        return exp.cdr().isNil();
    }

    /**
     * リストの先頭の要素を取得する。
     *
     * @param exp リスト
     * @return 先頭の要素
     */
    public static LObj firstList(LObj exp) {
        return exp.car();
    }

    /**
     * リストの2番目以降を取得する。
     * x
     * @param exp リスト
     * @return 2番目以降
     */
    public static LObj restList(LObj exp) {
        return exp.cdr();
    }


    public static boolean equalsAll(LObj o1, LObj o2) {
        if (o1.isNil()) {
            return o2.isNil();
        }
        else if (o1 instanceof Pair) {
            if (o2 instanceof Pair) {
                return equalsAll(o1.car(), o2.car()) && equalsAll(o1.cdr(), o2.cdr());
            }
        }
        else {
            return o1.equals(o2);
        }
        return false;
    }

    public static int length(LObj lst) {
        if (lst.isNil()) {
            return 0;
        }
        else if (lst.isAtom()) {
            return 1;
        }
        else {
            int num = 0;
            for (; lst.isNil() == false; num = num + 1) {
                lst = lst.cdr();
            }
            return num;
        }

    }

    public static LObj[] toArray(LObj args) {
        if (args == null || args.isNil()) {
            return new LObj[]{};
        }
        else if (args.isAtom()) {
            return new LObj[]{ args };
        }
        else {
            int length = ListUtil.length(args);
            LObj[] ret = new LObj[length];
            for (int ix = 0; ix < length; ix++) {
                ret[ix] = args.car();
                args = args.cdr();
            }
            return ret;
        }
    }


    public static void expand(LObj exp) {

    }
}
