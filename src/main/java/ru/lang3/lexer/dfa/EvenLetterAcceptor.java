package ru.lang3.lexer.dfa;

import ru.lang3.lexer.CharTypes;
import ru.lang3.lexer.GenAcceptor;

 // Example 1: Tokens consisting of an even number of letters (and nothing else)

public class EvenLetterAcceptor extends GenAcceptor  {

    public String lexClass() {return "EVEN" ;}
    public int numberOfStates() {return 3 ;} ;

    public int nextState (int state, char c) {
        switch (state) {
            case 0: if (CharTypes.isLetter(c)) return 1 ; else return 2 ;
            case 1: if (CharTypes.isLetter(c)) return 0 ; else return 2 ;
            default: return 2 ; // garbage state, declared "dead" below
        }
    }

    public boolean accepting (int state) {return (state == 0) ;}
    public  boolean dead (int state) {return (state == 2) ;}
}