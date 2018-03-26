package ru.lang3.parser;

public class UnexpectedInput extends Exception {
    public UnexpectedInput (String expected, String found) {
        super ("Parse error: " + found + " encountered where " +
                expected + " expected.") ;
    }
}