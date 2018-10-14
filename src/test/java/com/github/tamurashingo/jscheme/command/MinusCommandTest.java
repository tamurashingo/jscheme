package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


public class MinusCommandTest {
    @Test
    public void testLongLong() throws Exception {
        Atom l1 = Atom.ofValue(2);
        Atom l2 = Atom.ofValue(1);

        Command command = MinusCommand.getCommand();
        LObj result = command.operate(l1, l2);

        assertThat(result).isInstanceOf(Atom.class);
        assertThat((Long)((Atom)result).get()).isEqualTo(1L);
    }

    @Test
    public void testLongDouble() throws Exception {
        Atom l1 = Atom.ofValue(5);
        Atom d2 = Atom.ofValue(3.14);

        Command command = MinusCommand.getCommand();
        LObj result = command.operate(l1, d2);

        assertThat(result).isInstanceOf(Atom.class);
        assertThat((Double)((Atom)result).get()).isEqualTo(1.86, Offset.offset(0.01));
    }

    @Test
    public void testDoubleLong() throws Exception {
        Atom d1 = Atom.ofValue(2.71828);
        Atom l2 = Atom.ofValue(-2);

        Command command = MinusCommand.getCommand();
        LObj result = command.operate(d1, l2);

        assertThat(result).isInstanceOf(Atom.class);
        assertThat((Double)((Atom)result).get()).isEqualTo(4.71828, Offset.offset(0.01));
    }

    @Test
    public void testDoubleDouble() throws Exception {
        Atom d1 = Atom.ofValue(1.732);
        Atom d2 = Atom.ofValue(1.414);

        Command command = MinusCommand.getCommand();
        LObj result = command.operate(d1, d2);

        assertThat(result).isInstanceOf(Atom.class);
        assertThat((Double)((Atom)result).get()).isEqualTo(0.316, Offset.offset(0.01));
    }

    @Test
    public void testArg0() {
        Command command = MinusCommand.getCommand();
        assertThatThrownBy(() -> command.operate())
                .isInstanceOf(CommandException.class);
    }

    @Test
    public void testArg1() throws Exception {
        Atom d1 = Atom.ofValue(1.414);

        Command command = MinusCommand.getCommand();
        LObj result = command.operate(d1);

        assertThat(result).isInstanceOf(Atom.class);
        assertThat((Double)((Atom)result).get()).isEqualTo(-1.414, Offset.offset(0.01));
    }

    @Test
    public void testArg3() {
        Atom d1 = Atom.ofValue(1.414);
        Atom d2 = Atom.ofValue(1.732);
        Atom d3 = Atom.ofValue(2.236);
        Command command = MinusCommand.getCommand();

        assertThatThrownBy(() -> command.operate(d1, d2, d3))
                .isInstanceOf(CommandException.class);
    }
}
