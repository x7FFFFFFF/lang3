package ru.lang3.lexer.dfa;

import ru.lang3.lexer.DFA;
import ru.lang3.lexer.GenAcceptor;

public class TokAcceptor extends GenAcceptor implements DFA
{
    String tok;
    int tokLen;
    private char[] string;
    private int garb;

    public TokAcceptor (String tok)
    {
        this.tok    = tok ;
        this.tokLen = tok.length();
        this.string = tok.toCharArray();
        this.garb   = tokLen+1;
    }

    public String lexClass()    { return tok;      }
    public int numberOfStates() { return tokLen+2; }

    public boolean accepting(int state) { return state == tokLen; }
    public boolean dead(int state)      { return state == garb;   }

    public int nextState(int state, char c)
    {
        if (state < string.length)
        {
            if (c == string[state])     return state+1;
            else                        return garb;
        }
        else                            return garb;
    }
}