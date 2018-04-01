package ru.lang3.lexer;

// Thin wrapper for token streams: checks if a symbol token is 
// among those of L3, and renames its lexical class to be the symbol itself.
// (Helpful for the parser, which only looks at lexical classes.)


import java.io.IOException;

public class CheckedSymbolLexer implements LexTokenStream {

    private GenLexer tokStream ;

    public CheckedSymbolLexer (GenLexer tokStream) {
	this.tokStream = tokStream ;
    }

    private static String[] validTokens = new String[]
	{"::", "->", "=", "==", "<", "+", "-", "*"} ;

    private static String checkString(String s) throws UnknownSymbol {
	for (int i=0; i<validTokens.length; i++) {
	    if (s.equals(validTokens[i])) return s ;
	}
	throw new UnknownSymbol(s) ;
    }

    private static LexToken checkToken(LexToken t) throws UnknownSymbol {
	if (t != null && t.lexClass().equals("SYM")) {
	    return new LexToken (t.value(), checkString(t.value()), t.getRow(), t.getStartPosition()) ;
	} else return t ;
    }

    public LexToken pullToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.pullToken()) ;
    }

    public LexToken pullProperToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.pullProperToken()) ;
    }

    public LexToken peekToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.peekToken()) ;
    }

    public LexToken peekProperToken () 
	throws LexError, StateOutOfRange, IOException, UnknownSymbol {
	return checkToken (tokStream.peekProperToken()) ;
    }

}

class UnknownSymbol extends Exception {
    UnknownSymbol(String s) {
	super ("Unknown symbolic token " + s) ;
    }
}


