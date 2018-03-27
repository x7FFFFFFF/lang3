package ru.lang3.lexer;

// A *lexical token* is simply a string tagged with the name of its
// lexical class.
// Typical example: new LexToken ("5", "num")

import java.util.Objects;

public class LexToken {
    private final String value, lexClass ;
    public LexToken(String value, String lexClass) {
        this.value = value ; this.lexClass = lexClass ;
    }
    public String value () {
        return this.value ;
    }
    public String lexClass () {
        return this.lexClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LexToken lexToken = (LexToken) o;
        return Objects.equals(value, lexToken.value) &&
                Objects.equals(lexClass, lexToken.lexClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, lexClass);
    }
}