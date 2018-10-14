package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class AppendCommandTest {

    @Test
    public void test() throws Exception {
        Pair dst = Pair.of(Atom.ofString("this"),
                           Pair.of(Atom.ofString("is"),
                                   Pair.of(Atom.ofString("a"),
                                           LObj.NIL())));
        Pair src = Pair.of(Atom.ofString("pen"),
                           LObj.NIL());

        Pair result = (Pair)AppendCommand.getCommand().operate(dst, src);

        assertThat(result).isNotSameAs(dst);

        assertThat(result.car().isAtom()).isTrue();
        assertThat((String)((Atom)result.car()).get()).isEqualTo("this");

        assertThat(result.cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().car()).get()).isEqualTo("is");

        assertThat(result.cdr().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().cdr().car()).get()).isEqualTo("a");

        assertThat(result.cdr().cdr().cdr().car().isAtom()).isTrue();
        assertThat((String)((Atom)result.cdr().cdr().cdr().car()).get()).isEqualTo("pen");

        assertThat(result.cdr().cdr().cdr().cdr().isNil()).isTrue();
    }
}
