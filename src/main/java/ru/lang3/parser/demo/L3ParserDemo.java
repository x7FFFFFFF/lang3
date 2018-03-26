package ru.lang3.parser.demo;

import ru.lang3.lexer.CheckedSymbolLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.LexTokenStream;
import ru.lang3.parser.L3Parser;
import ru.lang3.parser.Parser;
import ru.lang3.parser.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class L3ParserDemo {

    static Parser L3_Parser = new L3Parser() ;

    public static void main (String[] args) throws Exception {
        Reader reader = new BufferedReader(new FileReader(args[0])) ;
        LexTokenStream L3_Lexer = new CheckedSymbolLexer(new L3Lexer(reader)) ;
        Tree theTree = L3_Parser.parseTokenStream (L3_Lexer) ;
    }
}
