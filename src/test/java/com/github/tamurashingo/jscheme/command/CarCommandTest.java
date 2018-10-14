package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;


public class CarCommandTest {

    @Test
    public void testAtom() {
        Command cmd = CarCommand.getCommand();
        LObj arg1 = Atom.ofValue(1);

        assertThatThrownBy(() -> cmd.operate(arg1))
                .isInstanceOf(CommandException.class);
    }

    @Test
    public void testPair() throws Exception {
        Command cmd = CarCommand.getCommand();
        LObj arg1 = Pair.of(Atom.ofValue(1), Atom.ofValue(2));

        assertThat(cmd.operate(arg1)).isEqualTo(Atom.ofValue(1));
    }
}
