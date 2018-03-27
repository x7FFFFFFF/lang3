package ru.lang3.lexer.dfa;

// Example 2: Acceptor for just the token "&&"

import ru.lang3.lexer.GenAcceptor;

public class AndAcceptor extends GenAcceptor {

    public String lexClass() {return "&&" ;} ;
    public int numberOfStates() {return 4 ;} ;

    public int nextState (int state, char c) {
        switch (state) {
            case 0: if (c=='&') return 1 ; else return 3 ;
            case 1: if (c=='&') return 2 ; else return 3 ;
            case 2: return 3 ;
            default: return 3 ;
        }
    }
    public boolean accepting (int state) {return (state == 2) ;}
    public boolean dead (int state) {return (state == 3) ;}
}