package ru.lang3.parser.demo;

import org.junit.jupiter.api.Test;
import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.demo.DemoLexer;
import ru.lang3.lexer.demo.LexerDemo;
import ru.lang3.parser.EvenAndParser;
import ru.lang3.parser.Parser;
import ru.lang3.parser.Tree;

import java.net.URL;

/**
 * Created by paramonov on 28.03.18.
 */
class ParserDemoTest {

    @Test
    public void test() throws Exception {

        final URL resource = ParserDemoTest.class.getResource("/EvenAnd_example.txt");
        final GenLexer demoLexer = LexerDemo.getLexer(resource, DemoLexer.class);
        Parser evenAndParser = new EvenAndParser() ;
        Tree theTree = evenAndParser.parseTokenStream(demoLexer) ;
        System.out.println("theTree = " + theTree);
        DrawTree.draw(theTree);

    }

    public static void main(String[] args) throws Exception{
        final URL resource = ParserDemoTest.class.getResource("/EvenAnd_example.txt");
        final GenLexer demoLexer = LexerDemo.getLexer(resource, DemoLexer.class);
        Parser evenAndParser = new EvenAndParser() ;
        Tree theTree = evenAndParser.parseTokenStream(demoLexer) ;
        System.out.println("theTree = " + theTree);
        DrawTree.draw(theTree);
    }

}