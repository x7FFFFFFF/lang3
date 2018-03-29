package ru.lang3.lexer.demo;


import ru.lang3.lexer.GenLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.LexError;
import ru.lang3.lexer.LexToken;
import ru.lang3.lexer.StateOutOfRange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;


// To try out the lexer on the dummy examples, compile this file, type
//    java LexerDemo
// and then type a line of input such as
//    abcd&&&&
// You can also experiment with erroneous inputs.
public class LexerDemo {

    public static void main (String[] args)
            throws LexError, StateOutOfRange, IOException {
        System.out.print ("Lexer> ") ;
        GenLexer demoLexer = getLexer(System.in);
        LexToken currTok = demoLexer.pullProperToken() ;
        while (currTok != null) {
            System.out.println (currTok.value() + " \t" +
                    currTok.lexClass()) ;
            currTok = demoLexer.pullProperToken() ;
        } ;
        System.out.println ("END OF INPUT.") ;
    }

    public static GenLexer getLexer(InputStream in) {
        Reader reader = new BufferedReader(new InputStreamReader(in)) ;
        return new L3Lexer(reader);
    }


    public static GenLexer getLexer(URL url, Class<? extends GenLexer> clz) throws IOException, URISyntaxException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Path path = Paths.get(url.toURI());
        Reader reader = Files.newBufferedReader(path) ;
        final Constructor<? extends GenLexer> constructor = clz.getConstructor(Reader.class);
        return constructor.newInstance(reader);
    }

    public static GenLexer getLexer(Path path) throws IOException {
        Reader reader = Files.newBufferedReader(path) ;
        return new L3Lexer(reader);
    }
    public static void getLexer(URL url, Consumer<LexToken> consumer) throws IOException, StateOutOfRange, LexError, URISyntaxException {
        final Path path = Paths.get(url.toURI());
        Reader reader = Files.newBufferedReader(path) ;
        getLexer(reader, consumer);
    }

    public static void getLexer(Reader reader, Consumer<LexToken> consumer) throws IOException, StateOutOfRange, LexError {
       // Reader reader = Files.newBufferedReader(path) ;
        final L3Lexer l3Lexer = new L3Lexer(reader);
        LexToken currTok = l3Lexer.pullProperToken() ;
        while (currTok != null) {
           /* System.out.println (currTok.value() + " \t" +
                    currTok.lexClass()) ;*/
            consumer.accept(currTok);
            currTok = l3Lexer.pullProperToken() ;
        }
    }
}
