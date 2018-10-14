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
import com.github.tamurashingo.jscheme.util.ListUtil;

public class AppendCommand extends Command {

    public static final Command thisCommand = new AppendCommand();

    public static Command getCommand() {
        return thisCommand;
    }

    @Override
    public LObj operate(LObj... args) throws CommandException {
        if (lenArgs(args) != 2) {
            throw new CommandException("引数の指定が二つではありません");
        }
        if (!args[0].isPair()) {
            throw new CommandException("引数1がPairではありません");
        }
        if (!args[1].isPair()) {
            throw new CommandException("引数2がPairではありません");
        }

        // (define append (p1 p2)
        //   (if (null? p1)
        //       p2
        //       (cons (car p1)
        //             (append (cdr p1) p2)))
        return ListUtil.append((Pair)args[0], (Pair)args[1]);

//        if (args[0].isNil()) {
//            return args[1];
//        }
//        else {
//            Pair start = ListUtil.copy((Pair)args[0]);
//            Pair p = start;
//            for (;;) {
//                if (p.cdr().isNil()) {
//                    p.setCdr(args[1]);
//                    break;
//                }
//                else {
//                    p = (Pair)p.cdr();
//                }
//            }
//            return start;
//        }
    }
}
