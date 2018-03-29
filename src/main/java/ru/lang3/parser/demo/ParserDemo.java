package ru.lang3.parser.demo;

// For testing

import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.demo.DemoLexer;
import ru.lang3.parser.EvenAndParser;
import ru.lang3.parser.Parser;
import ru.lang3.parser.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class ParserDemo {

    static Parser evenAndParser = new EvenAndParser() ;

    public static void main (String[] args) throws Exception {
        Reader reader = new BufferedReader(new FileReader(args[0])) ;
        GenLexer demoLexer = new DemoLexer(reader) ;
        Tree theTree = evenAndParser.parseTokenStream(demoLexer) ;
    }
}
