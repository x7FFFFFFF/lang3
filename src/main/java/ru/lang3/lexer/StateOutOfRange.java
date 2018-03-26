package ru.lang3.lexer;

public class StateOutOfRange extends Exception {
    public StateOutOfRange (String lexClassName, int state) {
        super ("Illegal state " + Integer.toString(state) +
                " in acceptor for " + lexClassName) ;
    }
}
