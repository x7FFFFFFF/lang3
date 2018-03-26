package ru.lang3.lexer;



// The following allows a LexTokenStream object to be created for
// a given input file and a language-specific repertoire of lexical classes.

import java.io.IOException;
import java.io.Reader;

public class GenLexer implements LexTokenStream {

    Reader reader ;
    // for reading characters from input
    DFA[] acceptors ;
    // array of acceptors for the lexical classes, in order of priority

    public GenLexer (Reader reader, DFA[] acceptors) {
        this.reader = reader ;
        this.acceptors = acceptors ;
    }

    LexToken bufferToken ;       // buffer to allow 1-token lookahead
    boolean bufferInUse = false ;

    static final char EOF = (char)65535 ;

    // Implementation of longest-match lexer as described in lectures.
    // We go for simplicity and clarity rather than maximum efficiency.

    LexToken nextToken ()
            throws LexError, StateOutOfRange, IOException {
        char c ;                 // current input character
        String definite = "" ;   // characters up to last acceptance point
        String maybe = "" ;      // characters since last acceptance point
        int acceptorIndex = -1 ; // array index of highest priority acceptor
        boolean liveFound = false ;      // flags for use in
        boolean acceptorFound = false ;  // iteration over acceptors

        for (int i=0; i<acceptors.length; i++) {
            acceptors[i].reset() ;
        } ;
        do {
            c = (char)(reader.read()) ;
            acceptorFound = false ;
            liveFound = false ;
            if (c != EOF) {
                maybe += c ;
                for (int i=0; i<acceptors.length; i++) {
                    acceptors[i].processChar(c) ;
                    if (!acceptors[i].isDead()) {
                        liveFound = true ;
                    }
                    if (!acceptorFound && acceptors[i].isAccepting()) {
                        acceptorFound = true ;
                        acceptorIndex = i ;
                        definite += maybe ;
                        maybe = "" ;
                        reader.mark(10) ; // register backup point in input
                    } ;
                }
            }
        } while (liveFound && c != EOF) ;
        if (acceptorIndex >= 0) { // lex token has been found
            // backup to last acceptance point and output token
            reader.reset() ;
            String theLexClass = acceptors[acceptorIndex].lexClass() ;
            return new LexToken (definite, theLexClass) ;
        } else if (c == EOF && maybe.equals("")) {
            // end of input already reached before nextToken was called
            reader.close() ;
            return null ;    // by convention, signifies end of input
        } else {
            reader.close() ;
            throw new LexError(maybe) ;
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