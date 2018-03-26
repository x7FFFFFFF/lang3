package ru.lang3.lexer.dfa;

import ru.lang3.lexer.CharTypes;
import ru.lang3.lexer.GenAcceptor;

public class CommentAcceptor extends GenAcceptor
{
    public String lexClass()    { return ""; }
    public int numberOfStates() { return 6;  }

    public boolean accepting(int state) { return state == 4; }
    public boolean dead(int state)      { return state == 5; }

    public int nextState(int state, char c)
    {
        switch(state)
        {
            case 0: if      (c == '-')                    return 1;
            else                                  return 5;

            case 1: if      (c == '-')                    return 2;
            else                                  return 5;

            case 2: if      (c == '-')                    return 2;
            else if (!CharTypes.isNewline(c) &&
                    !CharTypes.isSymbolic(c))    return 3;
            else                                  return 5;

            case 3: if      (!CharTypes.isNewline(c))     return 3;
            else if (CharTypes.isNewline(c))      return 4;
            else                                  return 5;

            default:                                      return 5;
        }
    }
}