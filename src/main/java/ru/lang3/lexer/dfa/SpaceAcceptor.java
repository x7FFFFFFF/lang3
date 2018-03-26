package ru.lang3.lexer.dfa;


// Example 3: Acceptor for just a space or linebreak character.
// Setting the lexical class as "" means these tokens will be discarded
// by the lexer.

import ru.lang3.lexer.GenAcceptor;

public class SpaceAcceptor extends GenAcceptor {

    public String lexClass() {return "" ;} ;
    public int numberOfStates() {return 3 ;} ;

    public int nextState (int state, char c) {
        switch (state) {
            case 0: if (c == ' ' || c=='\n' || c=='\r') return 1 ;
            else return 2 ;
            default: return 2 ;
        }
    }
    public boolean accepting (int state) {return (state == 1) ;}
    public boolean dead (int state) {return (state == 2) ;}
}