package ru.lang3.eval;


// Rudimentary runtime system for the  language.
// Illustrates the ideas of small-step operational semantics.

// The first thing to note is that this source file is relatively small:
// the bulk of the work has already been done at earlier stages of the
// language processing pipeline. That said, this is only a toy run-time
// system for demonstration purposes: it's relatively compact and corresponds
// closely to the operational semantics of the language, but it's VERY SLOW
// compared to a real-world implementation of Haskell!

// To use the evaluator (once you've completed the practical):

// 1. Download this file to the same directory as your other Java files.
// 2. Compile it using
//    javac Evaluator.java
// 3. Run the evaluator on the L3 source file of your choice, e.g.
//    java Evaluator L3_example.txt
//    This will load and typecheck the L3 program, and display a prompt "L3> "
// 4. Type in an expression you would like to evaluate, and hit return.
//    The expression may involve the functions declared in your L3 program.
//    Do this as many times as you like. E.g.
//    L3>  3+5
//    ...
//    L3>  fib 7
//    ...
//    L3>  div 20 3
//    ...
//    L3>  div 20
//    ...
// 5. Hit CTRL-c to quit.


import ru.lang3.eval.errors.UnknownVariable;
import ru.lang3.lexer.CheckedSymbolLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.LexTokenStream;
import ru.lang3.parser.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;

public class Evaluator {

    static boolean reducible (L3Exp e) {
        return ! (e.isNUM() || e.isBOOLEAN() || e.isLAMBDA()) ;
    }

    static L3Exp subst (L3Exp e, String v1, L3Exp e1)
            throws RuntimeError {
        // returns e with e1 substituted for all free occurrences of
        // the variable v1
        if (e.isVAR()) {
            if (e.value().equals(v1)) return e1 ;
            else return e ;
        } else if (e.isNUM() || e.isBOOLEAN()) {
            return e ;
        } else if (e.isAPP()) {
            return new L3ExpImpl
                    (subst (e.first(),v1,e1), subst (e.second(),v1,e1)) ;
        } else if (e.isINFIX()) {
            return new L3ExpImpl
                    (subst (e.first(),v1,e1), e.infixOp(),
                            subst (e.second(),v1,e1)) ;
        } else if (e.isIF()) {
            return new L3ExpImpl
                    (subst (e.first(),v1,e1), subst (e.second(),v1,e1),
                            subst (e.third(),v1,e1)) ;
        } else if (e.isLAMBDA()) {
            String v2 = e.value() ;
            if (v2.equals(v1)) return e ;
            else return new L3ExpImpl(v2, subst (e.first(),v1,e1)) ;
        } else throw new RuntimeError() ;
    }

    static L3Exp reduce (L3Exp e, L3ExpEnv env)
            throws RuntimeError, UnknownVariable {
        if (e.isVAR()) {
            return env.valueOf(e.value()) ;
        } else if (e.isINFIX()) {
            L3Exp e1 = e.first() ;
            L3Exp e2 = e.second() ;
            char i = e.infixOp() ;
            if (reducible(e1)) {
                return new L3ExpImpl(reduce(e1,env),i,e2) ;
            } else if (reducible(e2)) {
                return new L3ExpImpl(e1,i,reduce(e2,env)) ;
            } else {
                BigInteger v1 = new BigInteger (e1.value()) ;
                BigInteger v2 = new BigInteger (e2.value()) ;
                switch (i) {
                    case '+': return new L3ExpImpl
                            ("NUM", v1.add(v2).toString()) ;
                    case '-': return new L3ExpImpl
                            ("NUM", v1.subtract(v2).toString()) ;
                    case '*': return new L3ExpImpl
                            ("NUM", v1.multiply(v2).toString()) ;
                    case '=': return new L3ExpImpl
                            ("BOOLEAN", (v1.equals(v2)?"True":"False")) ;
                    case '<': return new L3ExpImpl
                            ("BOOLEAN", (v1.compareTo(v2)<0?"True":"False")) ;
                    default: throw new RuntimeError() ;
                }
            }
        } else if (e.isIF()) {
            L3Exp e1 = e.first() ;
            L3Exp e2 = e.second() ;
            L3Exp e3 = e.third() ;
            if (reducible(e1)) {
                return new L3ExpImpl(reduce(e1,env),e2,e3) ;
            } else if (e1.value().equals("True")) {
                return e2 ;
            } else if (e1.value().equals("False")) {
                return e3 ;
            } else throw new RuntimeError() ;
        } else if (e.isAPP()) {
            L3Exp e1 = e.first() ;
            L3Exp e2 = e.second() ;
            if (reducible(e1)) {
                return new L3ExpImpl(reduce(e1,env),e2) ;
            } else if (e1.isLAMBDA()) {
                // N.B. call-by-name reduction
                String var = e1.value() ;
                L3Exp body = e1.first() ;
                return subst (body,var,e2) ;
            } else throw new RuntimeError() ;
        } else throw new RuntimeError() ;
    }

    static L3Exp evaluate (L3Exp e, L3ExpEnv env)
            throws RuntimeError, UnknownVariable {
        L3Exp d = e ;
        while (reducible(d)) {
            d = reduce(d,env) ;
        } ;
        return d ;
    }

    static String printForm (L3Exp e) {
        if (e.isNUM() || e.isBOOLEAN()) return e.value() ;
        else return "-" ;
    }

    static class RuntimeError extends Exception {} ;

    public static void main (String[] args) throws Exception {
        // processes L3 program from specified file and then enters
        // interactive read-eval loop.

        Reader fileReader = new BufferedReader (new FileReader (args[0])) ;
        L3Typechecker.L3_Type_Env typeEnv = null ;
        L3ExpEnv runEnv = null ;
        // load L3 definitions from specified file
        try {
            LexTokenStream L3_Lexer =
                    new CheckedSymbolLexer(new L3Lexer(fileReader)) ;
            Tree prog = L3Typechecker.L3_Parser.parseTokenStream (L3_Lexer) ;
            typeEnv = L3Typechecker.compileTypeEnv (prog) ;
            runEnv = L3Typechecker.typecheckProg (prog, typeEnv) ;
        } catch (Exception x) {
            System.out.println ("L3 Error: " + x.getMessage()) ;
        }
        if (runEnv != null) {
            BufferedReader consoleReader =
                    new BufferedReader (new InputStreamReader (System.in)) ;
            // Enter interactive read-eval loop
            while (0==0) {
                System.out.print ("\nL3> ") ;
                String inputLine = "it = " + consoleReader.readLine() + ";" ;
                L3Exp e = null ;
                L3Type t = null ;
                // lex, parse and typecheck one line of console input
                try {
                    Reader lineReader =
                            new BufferedReader (new StringReader (inputLine)) ;
                    LexTokenStream lineLexer =
                            new CheckedSymbolLexer (new L3Lexer(lineReader)) ;
                    Tree dec = L3Typechecker.L3_Parser.parseTokenStreamAs
                            (lineLexer, "#TermDecl");
                    e = L3ExpImpl.convertExp (dec.getChildren()[3]) ;
                    t = L3Typechecker.computeType (e,typeEnv) ;
                } catch (Exception x) {
                    System.out.println ("L3 Error: " + x.getMessage()) ;
                } ;
                if (t != null) {
                    // display type
                    System.out.println ("  it :: " + t.toString()) ;
                    // evaluate expression
                    L3Exp e1 = evaluate (e,runEnv) ;
                    // display value
                    System.out.println ("  it  = " + printForm(e1)) ;
                }
            }
        }
    }
}