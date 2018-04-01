package ru.lang3.parser.demo;

import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.demo.DemoLexer;
import ru.lang3.lexer.demo.LexerDemo;
import ru.lang3.parser.EvenAndParser;
import ru.lang3.parser.L3Parser;
import ru.lang3.parser.Parser;
import ru.lang3.parser.Tree;


import java.net.URL;

/**
 * Created on 01.04.2018.
 */
public class L3ParserDemoTest {

    public static void main(String[] args) throws Exception{
        final URL resource = ParserDemoTest.class.getResource("/L3_example2.txt");
        final GenLexer lexer = LexerDemo.getLexer(resource, L3Lexer.class);
        Parser l3Parser = new L3Parser() ;
        Tree theTree = l3Parser.parseTokenStream(lexer) ;
        System.out.println("theTree = " + theTree);
        DrawTreePanel.draw(theTree);


    }
}
