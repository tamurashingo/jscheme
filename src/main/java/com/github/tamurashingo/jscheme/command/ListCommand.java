package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.util.ListUtil;

public class ListCommand extends Command {

    private static final Command thisCommand = new ListCommand();

    public static Command getCommand() {
        return thisCommand;
    }

    @Override
    public LObj operate(LObj...args) throws CommandException {
        return ListUtil.list(args);
    }
}
