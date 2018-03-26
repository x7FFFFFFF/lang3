package ru.lang3.lexer.dfa;

import ru.lang3.lexer.CharTypes;
import ru.lang3.lexer.GenAcceptor;

public class NumAcceptor extends GenAcceptor
{
    public String lexClass()    { return "NUM"; }
    public int numberOfStates() { return 4;     }

    public boolean accepting(int state) { return state == 1 || state == 2;  }
    public boolean dead(int state)      { return state == 3;                }

    public int nextState(int state, char c)
    {
        switch(state)
        {
            case 0:    if      (c == '0')                    return 1;
            else if (c >= '1' && c <= '9')        return 2;
            else                                  return 3;

            case 1:                                          return 3;

            case 2: if         (CharTypes.isDigit(c))        return 2;
            else                                     return 3;

            default:                                         return 3;
        }
    }
}