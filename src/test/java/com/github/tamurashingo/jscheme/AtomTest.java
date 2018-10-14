package com.github.tamurashingo.jscheme;

import static org.assertj.core.api.Assertions.*;

import static com.github.tamurashingo.jscheme.LObj.*;

import com.github.tamurashingo.jscheme.Atom;
import com.github.tamurashingo.jscheme.LObj;
import com.github.tamurashingo.jscheme.Pair;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;


public class AtomTest {

    @Test
    public void testGetSymbol() {
        assertThat((String)new Atom(TYPE.SYMBOL, "SYMBOL").get()).isEqualTo("SYMBOL");
        assertThat((String)new Atom(TYPE.SYMBOL, "symbol").get()).isEqualTo("SYMBOL");
    }

    @Test
    public void testGetString() {
        assertThat((String)new Atom(TYPE.STRING, "string").get()).isEqualTo("string");
        assertThat((String)new Atom(TYPE.STRING, "STRING").get()).isEqualTo("STRING");
    }
    
    @Test
    public void testGetValue() {
        assertThat((Long)new Atom(TYPE.VALUE, 1).get()).isEqualTo(1L);
        assertThat((Double)new Atom(TYPE.VALUE, 1.5).get()).isEqualTo(1.5, Offset.offset(0.01));
        assertThat((Long)new Atom(TYPE.VALUE, 0).get()).isEqualTo(0L);
        assertThat((Long)new Atom(TYPE.VALUE, -10).get()).isEqualTo(-10L);
    }

    @Test
    public void testEqualsSymbol() {
        // symbol vs symbol
        assertThat(new Atom(TYPE.SYMBOL, "SYMBOL").equals(new Atom(TYPE.SYMBOL, "SYMBOL"))).isTrue();
        assertThat(new Atom(TYPE.SYMBOL, "SYMBOL").equals(new Atom(TYPE.SYMBOL, "symbol"))).isTrue();
        assertThat(new Atom(TYPE.SYMBOL, "symbol1").equals(new Atom(TYPE.SYMBOL, "symbol2"))).isFalse();
        
        // symbol vs string
        assertThat(new Atom(TYPE.SYMBOL, "SYMBOL").equals(new Atom(TYPE.STRING, "SYMBOL"))).isFalse();
        
        // symbol vs value
        assertThat(new Atom(TYPE.SYMBOL, "SYMBOL").equals(new Atom(TYPE.VALUE, 1))).isFalse();
    }

    @Test
    public void testToString() {
        assertThat(new Atom(TYPE.SYMBOL, "symbol").toString()).isEqualTo("SYMBOL");
        assertThat(new Atom(TYPE.STRING, "string").toString()).isEqualTo("\"string\"");
        assertThat(new Atom(TYPE.VALUE, 3.14).toString()).isEqualTo("3.14");
    }

}
