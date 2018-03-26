package ru.lang3.lexer.dfa;

import ru.lang3.lexer.GenAcceptor;

public  class BooleanAcceptor extends GenAcceptor
{
    public String lexClass()    { return "BOOLEAN"; }
    public int numberOfStates() { return 9;         }

    public  boolean accepting(int state) { return state == 4; }
    public boolean dead(int state)      { return state == 8; }

    public int nextState(int state, char c)
    {
        switch(state)
        {
            case 0:    if      (c == 'T')                  return 1;
            else if (c == 'F')                  return 5;
            else                                return 8;

            case 1:    if      (c == 'r')                  return 2;
            else                                return 8;

            case 2:    if      (c == 'u')                  return 3;
            else                                return 8;

            case 3:    if      (c == 'e')                  return 4;
            else                                return 8;

            case 4:                                        return 8;

            case 5:    if      (c == 'a')                  return 6;
            else                                return 8;

            case 6:    if      (c == 'l')                  return 7;
            else                                return 8;

            case 7:    if      (c == 's')                  return 3;
            else                                return 8;

            default:                                       return 8;
        }
    }
}