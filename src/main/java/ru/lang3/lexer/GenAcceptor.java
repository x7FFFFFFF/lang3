package ru.lang3.lexer;

public abstract class GenAcceptor implements DFA {

    // Stubs for methods specific to a particular DFA
    public abstract String lexClass() ;
    public abstract int numberOfStates() ;
    public abstract int nextState (int state, char input) ;
    public abstract boolean accepting (int state) ;
    public abstract boolean dead (int state) ;

    // General DFA machinery
    private int currState = 0 ;         // the initial state is always 0
    public void reset () {currState = 0 ;}

    public void processChar (char c) throws StateOutOfRange {
        // performs the state transition determined by c
        currState = nextState (currState,c) ;
        if (currState >= numberOfStates()) {
            throw new StateOutOfRange (lexClass(), currState) ;
        }
    }

    public boolean isAccepting () {return accepting (currState) ;}
    public boolean isDead () {return dead (currState) ;}
}
