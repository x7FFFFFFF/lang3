package ru.lang3.parser;

import ru.lang3.lexer.LexTokenStream;

public interface Parser {
    Tree parseTokenStream (LexTokenStream tokStream)
            throws Exception ;
    Tree parseTokenStreamAs (LexTokenStream tokStream, String nonterm)
            throws Exception ;
}
