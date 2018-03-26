package ru.lang3.lexer.demo;



import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.LexError;
import ru.lang3.lexer.LexToken;
import ru.lang3.lexer.StateOutOfRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;



// To try out the lexer on the dummy examples, compile this file, type
//    java LexerDemo
// and then type a line of input such as
//    abcd&&&&
// You can also experiment with erroneous inputs.
public class LexerDemo {

    public static void main (String[] args)
            throws LexError, StateOutOfRange, IOException {
        System.out.print ("Lexer> ") ;
        Reader reader = new BufferedReader(new InputStreamReader(System.in)) ;
        GenLexer demoLexer = new L3Lexer(reader) ;
        LexToken currTok = demoLexer.pullProperToken() ;
        while (currTok != null) {
            System.out.println (currTok.value() + " \t" +
                    currTok.lexClass()) ;
            currTok = demoLexer.pullProperToken() ;
        } ;
        System.out.println ("END OF INPUT.") ;
    }
}
