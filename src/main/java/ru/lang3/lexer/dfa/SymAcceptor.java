package ru.lang3.lexer.dfa;

import ru.lang3.lexer.CharTypes;
import ru.lang3.lexer.GenAcceptor;

public class SymAcceptor extends GenAcceptor
{
    public String lexClass()    { return "SYM"; }
    public int numberOfStates() { return 3;     }

    public boolean accepting(int state) { return state == 1; }
    public boolean dead(int state)      { return state == 2; }

    public int nextState(int state, char c)
    {
        switch(state)
        {
            case 0: if     (CharTypes.isSymbolic(c))    return 1;
            else                                return 2;

            case 1: if     (CharTypes.isSymbolic(c))    return 1;
            else                                return 2;

            default:                                    return 2;
        }
    }
}