package ru.lang3.lexer;

// The output of the lexing phase, and the input to the parsing phase,
// will be a LEX_TOKEN_STREAM object from which tokens may be drawn at will
// by calling `nextToken'.


public interface LexTokenStream {
    LexToken pullToken () throws Exception ;
    // pulls next token from stream, regardless of class
    LexToken pullProperToken () throws Exception ;
    // pulls next token not of class "" (e.g. skip whitespace and comments)
    LexToken peekToken () throws Exception ;
    // returns next token without removing it from stream
    LexToken peekProperToken () throws Exception ;
    // similarly for non-"" tokens
    // All these methods return null once end of input is reached
}