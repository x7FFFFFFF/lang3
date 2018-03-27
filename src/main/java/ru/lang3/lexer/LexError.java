package ru.lang3.lexer;

public class LexError extends Exception {
    public LexError (String nonToken) {
        super ("Can't make lexical token from input \"" +
                nonToken + "\"") ;
    }
}