package ru.lang3.lexer;


// Generic implementation of DFAs with explicit "dead" states
public interface DFA {
    String lexClass() ;
    int numberOfStates() ;
    void reset() ;
    void processChar (char c) throws StateOutOfRange;
    boolean isAccepting () ;
    boolean isDead () ;
}