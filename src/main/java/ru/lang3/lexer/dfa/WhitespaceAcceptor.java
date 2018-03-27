package ru.lang3.lexer.dfa;

import ru.lang3.lexer.CharTypes;
import ru.lang3.lexer.GenAcceptor;

public class WhitespaceAcceptor extends GenAcceptor
{
    public String lexClass()    { return ""; }
    public int numberOfStates() { return 3;  }

    public boolean accepting(int state) { return state == 1; }
    public boolean dead(int state)      { return state == 2; }

    public int nextState(int state, char c)
    {
        switch(state)
        {
            case 0: if   (CharTypes.isWhitespace(c))    return 1;
            else                                return 2;

            case 1: if   (CharTypes.isWhitespace(c))    return 1;
            else                                return 2;

            default:                                    return 2;
        }
    }
}
