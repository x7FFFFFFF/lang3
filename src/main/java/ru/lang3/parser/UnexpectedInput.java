package ru.lang3.parser;

import ru.lang3.lexer.LexToken;

public class UnexpectedInput extends Exception {
    public UnexpectedInput (String expected, String found) {
        super ("Parse error: " + found + " encountered where " +
                expected + " expected.") ;
    }

    public UnexpectedInput (String expected, LexToken found) {
        super (String.format("Parse error: line #%d  '%s' encountered where '%s'  expected. ",
                found.getRow(),
                 found.lexClass(),
                expected)) ;
    }
}