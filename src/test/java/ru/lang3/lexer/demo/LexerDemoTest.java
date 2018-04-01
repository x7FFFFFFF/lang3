package ru.lang3.lexer.demo;

import org.junit.jupiter.api.Test;
import ru.lang3.lexer.LexError;
import ru.lang3.lexer.LexToken;
import ru.lang3.lexer.StateOutOfRange;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by paramonov on 27.03.18.
 */
class LexerDemoTest {

    @Test
    void main() throws IOException, LexError, StateOutOfRange, URISyntaxException {

        List<LexToken> etalon = Arrays.asList(
       /* new LexToken("fib", "VAR", row, startPosition),
                new LexToken("::", "SYM", row, startPosition),
                new LexToken("Integer", "Integer", row, startPosition),
                new LexToken("->", "SYM", row, startPosition),
                new LexToken("Integer", "Integer", row, startPosition),
                new LexToken(";", ";", row, startPosition),
                new LexToken("fib", "VAR", row, startPosition),
                new LexToken("n", "VAR", row, startPosition),
                new LexToken("=", "SYM", row, startPosition),
                new LexToken("n", "VAR", row, startPosition),
                new LexToken("+", "SYM", row, startPosition),
                new LexToken("10000", "NUM", row, startPosition),
                new LexToken(";", ";", row, startPosition)*/

        );

        final Iterator<LexToken> etalonIterator = etalon.iterator();
        final URL resource = LexerDemoTest.class.getResource("/L3_example2.txt");
        LexerDemo.getLexer(resource, lexToken -> {
            final LexToken etalonToken = etalonIterator.next();
            assertEquals(etalonToken, lexToken);


            System.out.println (lexToken.value() + " \t" +
                   lexToken.lexClass()) ;





       });




    }
}