package com.github.tamurashingo.jscheme.eval;

import com.github.tamurashingo.jscheme.JSchemeRuntimeException;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.parser.Parser;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;



public class CondConverterTest {

    @Test
    public void testCond() throws Exception {
        String source = "(cond ((> x 1) x)"
                      + "      ((= x 0) (display 'zero) 0)"
                      + "      (else (- x 0)))"
                ;
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        LObj result = CondConverter.condToIf(exp);

        assertThat(result.toString()).isEqualTo("(IF (> X 1) X (IF (= X 0) (BEGIN (DISPLAY (QUOTE ZERO)) 0) (- X 0)))");
    }

    @Test
    public void testCond1() throws Exception {
        String source = "(cond (TRUE x)"
                      + "      (FALSE (display 'zero) 0)"
                      + "      (else (- x 0)))"
                ;
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        LObj result = CondConverter.condToIf(exp);

        assertThat(result.toString()).isEqualTo("(IF TRUE X (IF FALSE (BEGIN (DISPLAY (QUOTE ZERO)) 0) (- X 0)))");
    }

    @Test
    public void testCondError() throws Exception {
        String source = "(cond ((> x 1) x)"
                      + "      (else (- x 0)) "
                      + "      ((= x 0) (display 'zero) 0))"
                ;
        Parser parser = new Parser(source);

        LObj exp = parser.parse();

        assertThatThrownBy(() -> CondConverter.condToIf(exp))
                .isInstanceOf(JSchemeRuntimeException.class);
    }
}
