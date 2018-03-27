package ru.lang3.lexer.demo;

// A silly example of a lexer using the DFAs constructed earlier

import ru.lang3.lexer.DFA;
import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.LexTokenStream;
import ru.lang3.lexer.dfa.AndAcceptor;
import ru.lang3.lexer.dfa.EvenLetterAcceptor;
import ru.lang3.lexer.dfa.SpaceAcceptor;

import java.io.Reader;

public class DemoLexer extends GenLexer implements LexTokenStream {

    static DFA evenLetterAcc = new EvenLetterAcceptor() ;
    static DFA andAcc = new AndAcceptor() ;
    static DFA spaceAcc = new SpaceAcceptor() ;
    static DFA[] acceptors =
            new DFA[] {evenLetterAcc, andAcc, spaceAcc} ;
    public DemoLexer (Reader reader) {
        super(reader,acceptors) ;
    }
}
