package ru.lang3.lexer;



// The following allows a LexTokenStream object to be created for
// a given input file and a language-specific repertoire of lexical classes.

import java.io.IOException;
import java.io.Reader;

public class GenLexer implements LexTokenStream {

    private Reader reader ;
    // for reading characters from input
    private DFA[] acceptors ;
    // array of acceptors for the lexical classes, in order of priority

    private int row = 0, length = 0;

    public GenLexer (Reader reader, DFA[] acceptors) {
        this.reader = reader ;
        this.acceptors = acceptors ;
    }

    private LexToken bufferToken ;       // buffer to allow 1-token lookahead
    private boolean bufferInUse = false ;

    private static final char EOF = (char)65535 ;

    // Implementation of longest-match lexer as described in lectures.
    // We go for simplicity and clarity rather than maximum efficiency.

    private LexToken nextToken()
            throws LexError, StateOutOfRange, IOException {
        char c ;                 // current input character
        StringBuilder definite = new StringBuilder() ;   // characters up to last acceptance point
        StringBuilder maybe = new StringBuilder() ;      // characters since last acceptance point
        int acceptorIndex = -1 ; // array index of highest priority acceptor
        boolean liveFound, acceptorFound;      // flags for use in         // iteration over acceptors

        for (DFA acceptor : acceptors) {
            acceptor.reset();
        }
        do {
            c = (char)(reader.read()) ;
            if (CharTypes.isNewline(c)) {
                row++;
               // length = 0;
            } else {
               // length++;
            }
            acceptorFound = false ;
            liveFound = false ;
            if (c != EOF) {
                maybe.append(c) ;
                for (int i=0; i<acceptors.length; i++) {
                    acceptors[i].processChar(c) ;
                    if (!acceptors[i].isDead()) {
                        liveFound = true ;
                    }
                    if (!acceptorFound && acceptors[i].isAccepting()) {
                        acceptorFound = true ;
                        acceptorIndex = i ;
                        definite.append(maybe) ;
                        maybe.setLength(0);
                        reader.mark(10) ; // register backup point in input
                    }
                }
            }
        } while (liveFound && c != EOF) ;
        if (acceptorIndex >= 0) { // lex token has been found
            // backup to last acceptance point and output token
            reader.reset() ;
            String theLexClass = acceptors[acceptorIndex].lexClass() ;
            return new LexToken (definite.toString(), theLexClass, row, -1) ;
        } else if (c == EOF && maybe.toString().equals("")) {
            // end of input already reached before nextToken was called
            reader.close() ;
            return null ;    // by convention, signifies end of input
        } else {
            reader.close() ;
            throw new LexError(maybe.toString()) ;
        }
    }

    public LexToken peekToken ()
            throws LexError, StateOutOfRange, IOException {
        if (bufferInUse) {
            return bufferToken ;
        } else {
            bufferToken = nextToken() ;
            bufferInUse = true ;
            return bufferToken ;
        }
    }

    public LexToken pullToken ()
            throws LexError, StateOutOfRange, IOException {
        peekToken () ;
        bufferInUse = false ;
        return bufferToken ;
    }

    public LexToken peekProperToken ()
            throws LexError, StateOutOfRange, IOException {
        LexToken tok = peekToken () ;
        while (tok != null && tok.lexClass().equals("")) {
            pullToken () ;
            tok = peekToken () ;
        }
        bufferToken = tok ;
        bufferInUse = true ;
        return tok ;
    }

    public LexToken pullProperToken ()
            throws LexError, StateOutOfRange, IOException {
        peekProperToken () ;
        bufferInUse = false ;
        return bufferToken ;
    }
}