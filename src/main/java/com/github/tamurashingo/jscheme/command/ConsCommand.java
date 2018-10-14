/*-
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 tamura shingo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.tamurashingo.jscheme.command;

import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;

public class ConsCommand extends Command {

    private static final Command thisCommand = new ConsCommand();

    public static Command getCommand() {
        return thisCommand;
    }

    @Override
    public LObj operate(LObj...args) throws CommandException {
        if (lenArgs(args) != 2) {
            throw new CommandException("引数の指定が二つではありません");
        }
        return new Pair(args[0], args[1]);
    }
}
