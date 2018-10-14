package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;

public class PlusCommand extends Command {

    private static final Command thisCommand = new PlusCommand();

    public static Command getCommand() {
        return thisCommand;
    }

    @Override
    public LObj operate(LObj... args) throws CommandException {
        if (lenArgs(args) != 2) {
            throw new CommandException("引数の指定が二つではありません");
        }

        if (args[0].type() != LObj.TYPE.VALUE) {
            throw new CommandException("第1引数がVALUEではありません:" + ((Atom)args[0]).get());
        }
        if (args[1].type() != LObj.TYPE.VALUE) {
            throw new CommandException("第2引数がVALUEではありません:" + ((Atom)args[1]).get());
        }

        if (isDouble(args[0])) {
            double d1 = getDouble(args[0]);
            if (isDouble(args[1])) {
                double d2 = getDouble(args[1]);
                return Atom.ofValue(d1 + d2);
            }
            else {
                long l2 = getLong(args[1]);
                return Atom.ofValue(d1 + l2);
            }
        }
        else {
            long l1 = getLong(args[0]);
            if (isDouble(args[1])) {
                double d2 = getDouble(args[1]);
                return Atom.ofValue(l1 + d2);
            } else {
                long l2 = getLong(args[1]);
                return Atom.ofValue(l1 + l2);
            }
        }
    }


    private boolean isDouble(LObj obj) {
        return ((Atom)obj).getSubtype() == Atom.SUBTYPE.DOUBLE;
    }

    private Double getDouble(LObj obj) {
        return ((Atom)obj).get();
    }

    private Long getLong(LObj obj) {
        return ((Atom)obj).get();
    }


}
