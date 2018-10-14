package com.github.tamurashingo.jscheme.command;


import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class ListCommandTest {

    @Test
    public void testList() throws Exception {
        Command command = ListCommand.getCommand();

        LObj result = command.operate(Atom.ofString("string1"), Atom.ofString("string2"), Atom.ofSymbol("symbol3"));

        assertThat(result).isNotNull();
        assertThat((String)((Atom)result.car()).get()).isEqualTo("string1");
        assertThat((String)((Atom)result.cdr().car()).get()).isEqualTo("string2");
        assertThat((String)((Atom)result.cdr().cdr().car()).get()).isEqualTo("SYMBOL3");
        assertThat(result.cdr().cdr().cdr().cdr().isNil()).isTrue();
    }

    @Test
    public void testList2() throws Exception {
        Command command = ListCommand.getCommand();

        LObj result = command.operate();

        assertThat(result).isNotNull();
        assertThat(result.isNil()).isTrue();
    }
}
