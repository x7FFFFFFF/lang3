package ru.lang3;

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


import java.math.* ;
import java.io.* ;

class Evaluator {

    static boolean reducible (L3_EXP e) {
	return ! (e.isNUM() || e.isBOOLEAN() || e.isLAMBDA()) ;
    }

    static L3_EXP subst (L3_EXP e, String v1, L3_EXP e1)
	throws RuntimeError {
	// returns e with e1 substituted for all free occurrences of
	// the variable v1
	if (e.isVAR()) {
	    if (e.value().equals(v1)) return e1 ;
	    else return e ;
	} else if (e.isNUM() || e.isBOOLEAN()) {
	    return e ;
	} else if (e.isAPP()) {
	    return new L3_Exp_Impl
		(subst (e.first(),v1,e1), subst (e.second(),v1,e1)) ;
	} else if (e.isINFIX()) {
	    return new L3_Exp_Impl
		(subst (e.first(),v1,e1), e.infixOp(), 
		 subst (e.second(),v1,e1)) ;
	} else if (e.isIF()) {
	    return new L3_Exp_Impl
		(subst (e.first(),v1,e1), subst (e.second(),v1,e1),
		 subst (e.third(),v1,e1)) ;
	} else if (e.isLAMBDA()) {
	    String v2 = e.value() ;
	    if (v2.equals(v1)) return e ;
	    else return new L3_Exp_Impl (v2, subst (e.first(),v1,e1)) ;
	} else throw new RuntimeError() ;
    }

    static L3_EXP reduce (L3_EXP e, L3_Exp_Env env)
	throws RuntimeError, UnknownVariable {
	if (e.isVAR()) {
	    return env.valueOf(e.value()) ;
	} else if (e.isINFIX()) {
	    L3_EXP e1 = e.first() ;
	    L3_EXP e2 = e.second() ;
	    char i = e.infixOp() ;
	    if (reducible(e1)) {
		return new L3_Exp_Impl (reduce(e1,env),i,e2) ;
	    } else if (reducible(e2)) {
		return new L3_Exp_Impl (e1,i,reduce(e2,env)) ;
	    } else {
		BigInteger v1 = new BigInteger (e1.value()) ;
		BigInteger v2 = new BigInteger (e2.value()) ;
		switch (i) {
		case '+': return new L3_Exp_Impl
			("NUM", v1.add(v2).toString()) ;
		case '-': return new L3_Exp_Impl
 			("NUM", v1.subtract(v2).toString()) ;
		case '*': return new L3_Exp_Impl
			("NUM", v1.multiply(v2).toString()) ;
		case '=': return new L3_Exp_Impl
			("BOOLEAN", (v1.equals(v2)?"True":"False")) ;
		case '<': return new L3_Exp_Impl
			("BOOLEAN", (v1.compareTo(v2)<0?"True":"False")) ;
		default: throw new RuntimeError() ;
		}
	    }
	} else if (e.isIF()) {
	    L3_EXP e1 = e.first() ;
	    L3_EXP e2 = e.second() ;
	    L3_EXP e3 = e.third() ;
	    if (reducible(e1)) {
		return new L3_Exp_Impl (reduce(e1,env),e2,e3) ;
	    } else if (e1.value().equals("True")) {
		return e2 ;
	    } else if (e1.value().equals("False")) {
		return e3 ;
	    } else throw new RuntimeError() ;
	} else if (e.isAPP()) {
	    L3_EXP e1 = e.first() ;
	    L3_EXP e2 = e.second() ;
	    if (reducible(e1)) {
		return new L3_Exp_Impl (reduce(e1,env),e2) ;
	    } else if (e1.isLAMBDA()) {  
		// N.B. call-by-name reduction
		String var = e1.value() ;
		L3_EXP body = e1.first() ;
		return subst (body,var,e2) ;
	    } else throw new RuntimeError() ;
	} else throw new RuntimeError() ;
    }

    static L3_EXP evaluate (L3_EXP e, L3_Exp_Env env)
	throws RuntimeError, UnknownVariable {
	L3_EXP d = e ;
	while (reducible(d)) {
	    d = reduce(d,env) ;
	} ;
	return d ;
    }

    static String printForm (L3_EXP e) {
	if (e.isNUM() || e.isBOOLEAN()) return e.value() ;
	else return "-" ;
    }

    static class RuntimeError extends Exception {} ;

    public static void main (String[] args) throws Exception {
	// processes L3 program from specified file and then enters
	// interactive read-eval loop.

	Reader fileReader = new BufferedReader (new FileReader (args[0])) ;
	L3_Typechecker.L3_Type_Env typeEnv = null ;
	L3_Exp_Env runEnv = null ;
	// load L3 definitions from specified file
	try {
	    LEX_TOKEN_STREAM L3_Lexer =
		new CheckedSymbolLexer (new L3_Lexer (fileReader)) ;
	    TREE prog = L3_Typechecker.L3_Parser.parseTokenStream (L3_Lexer) ;
	    typeEnv = L3_Typechecker.compileTypeEnv (prog) ;
	    runEnv = L3_Typechecker.typecheckProg (prog, typeEnv) ;
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
		L3_EXP e = null ;
		L3_TYPE t = null ;
		// lex, parse and typecheck one line of console input
		try {
		    Reader lineReader = 
			new BufferedReader (new StringReader (inputLine)) ;
		    LEX_TOKEN_STREAM lineLexer =
			new CheckedSymbolLexer (new L3_Lexer (lineReader)) ;
		    TREE dec = L3_Typechecker.L3_Parser.parseTokenStreamAs
			(lineLexer, "#TermDecl");
		    e = L3_Exp_Impl.convertExp (dec.getChildren()[3]) ;
		    t = L3_Typechecker.computeType (e,typeEnv) ;
		} catch (Exception x) {
		    System.out.println ("L3 Error: " + x.getMessage()) ;
		} ;
		if (t != null) {
		    // display type
		    System.out.println ("  it :: " + t.toString()) ;
		    // evaluate expression
		    L3_EXP e1 = evaluate (e,runEnv) ;
		    // display value
		    System.out.println ("  it  = " + printForm(e1)) ;
		}
	    }
	}
    }
}
