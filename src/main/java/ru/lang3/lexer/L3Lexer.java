package ru.lang3.lexer;

import ru.lang3.lexer.dfa.BooleanAcceptor;
import ru.lang3.lexer.dfa.CommentAcceptor;
import ru.lang3.lexer.dfa.NumAcceptor;
import ru.lang3.lexer.dfa.SymAcceptor;
import ru.lang3.lexer.dfa.TokAcceptor;
import ru.lang3.lexer.dfa.VarAcceptor;
import ru.lang3.lexer.dfa.WhitespaceAcceptor;

import java.io.Reader;

public class L3Lexer extends GenLexer implements LexTokenStream
{

    public L3Lexer(Reader reader)
    {
        super(reader,  new DFA[] {
                new TokAcceptor("Integer"),
                new TokAcceptor("Bool"),
                new TokAcceptor("if"),
                new TokAcceptor("then"),
                new TokAcceptor("else"),
                new TokAcceptor("("),
                new TokAcceptor(")"),
                new TokAcceptor(";"),
                new VarAcceptor(),
                new BooleanAcceptor(),
                new NumAcceptor(),
                new SymAcceptor(),
                new WhitespaceAcceptor(),
                new CommentAcceptor()
        });
    }
}
