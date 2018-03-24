package ru.lang3;

// Provides abstract syntax trees for expressions:
// effectively, trees for the simplified grammar

//     Exp  -->   VAR | NUM | BOOLEAN | Exp Exp | Exp infix Exp
//                | if Exp then Exp else Exp

import java.util.* ;


interface L3_EXP {
    boolean isVAR() ;
    boolean isNUM() ;
    boolean isBOOLEAN() ;
    boolean isAPP() ;
    boolean isINFIX() ;
    boolean isIF() ;
    boolean isLAMBDA() ; // only needed for evaluator: L3 itself doesn't have lambda expressions.

    String value() ;     // for VAR, NUM, BOOLEAN: returns e.g. "x", "5", "True"
    char infixOp() ;     // for infix expressions: returns '=', '<', '+', '-' or '*'
    L3_EXP first() ;     // returns first child (for application, infix, if-expressions)
    L3_EXP second() ;    // returns second child (for application, infix, if-expressions)
    L3_EXP third() ;     // returns third child (for if-expressions only)
}

class L3_Exp_Impl implements L3_EXP {
    private int kind ;
    private String value ;
    private char infixOp ;
    private L3_EXP firstChild ;
    private L3_EXP secondChild ;
    private L3_EXP thirdChild ;
    public boolean isVAR() {return kind==0 ;}
    public boolean isNUM() {return kind==1 ;}
    public boolean isBOOLEAN() {return kind==2 ;}
    public boolean isAPP() {return kind==3 ;}
    public boolean isINFIX() {return kind==4 ;}
    public boolean isIF() {return kind==5 ;}
    public boolean isLAMBDA() {return kind==6 ;}
    public String value() {return value ;}
    public char infixOp() {return infixOp ;}
    public L3_EXP first() {return firstChild ;}
    public L3_EXP second() {return secondChild ;}
    public L3_EXP third() {return thirdChild ;}

    // Various constructors: number and type of arguments determine
    // the kind of expression.

    // For atomic expressions (VAR, NUM, BOOLEAN)
    L3_Exp_Impl (String lexClass, String value) {
	this.value = value ;
	if (lexClass.equals("VAR")) kind=0 ;
	else if (lexClass.equals("NUM")) kind=1 ;
	else if (lexClass.equals("BOOLEAN")) kind=2 ;
	else {
	    System.out.println ("Warning: unknown lexClass " + lexClass) ;
	    kind=-1 ; 
	}
    }

    // For applications
    L3_Exp_Impl (L3_EXP left, L3_EXP right) {
	this.kind = 3 ;
	this.firstChild = left ;
	this.secondChild = right ;
    }

    // For infix expressions
    L3_Exp_Impl (L3_EXP left, char infixOp, L3_EXP right) {
	this.kind = 4 ;
	this.firstChild = left ;
	this.secondChild = right ;
	this.infixOp = infixOp ;
    }

    // For if-expressions
    L3_Exp_Impl (L3_EXP condition, L3_EXP branch1, L3_EXP branch2) {
	this.kind = 5 ;
	this.firstChild = condition ;
	this.secondChild = branch1 ;
	this.thirdChild = branch2 ;
    }

    // For lambda-expressions
    L3_Exp_Impl (String var, L3_EXP body) {
	this.kind = 6 ;
	this.value = var ;
	this.firstChild = body ;
    }


    // Converting parse trees to ASTs for expressions

    static L3_Parser L3_Parser = L3_Type_Impl.L3_Parser ;

    static class TaggedExp {
	L3_EXP exp ;
	char tag ;
	TaggedExp (L3_EXP exp, char tag) {
	    this.exp = exp ; this.tag = tag ;
	}
    }

    static L3_EXP convertExp (TREE exp) {
	if (exp.getLabel().equals("#Exp5")) {
	    if (exp.getRhs() == L3_Parser.lbr_Exp_rbr)
		return convertExp (exp.getChildren()[1]) ;
	    else {
		TREE terminal = exp.getChildren()[0] ;
		// build atomic expression
		return new L3_Exp_Impl
		    (terminal.getLabel(), terminal.getValue()) ;
	    }
	} else if (exp.getLabel().equals("#Exp4")) {
	    L3_EXP head = convertExp (exp.getChildren()[0]) ;
	    Stack rest = convertOps4 (exp.getChildren()[1]) ;
	    while (! rest.isEmpty()) {
		// build application expression
		head = new L3_Exp_Impl (head, (L3_EXP)(rest.pop())) ;
	    } ;
	    return head ;
	} else if (exp.getLabel().equals("#Exp3")) {
	    L3_EXP head = convertExp (exp.getChildren()[0]) ;
	    Stack rest = convertOps3 (exp.getChildren()[1]) ;
	    while (! rest.isEmpty()) {
		// build '*' infix expression
		head = new L3_Exp_Impl (head, '*', (L3_EXP)(rest.pop())) ;
	    } ;
	    return head ;
	} else if (exp.getLabel().equals("#Exp2")) {
	    L3_EXP head = convertExp (exp.getChildren()[0]) ;
	    Stack rest = convertOps2 (exp.getChildren()[1]) ;
	    while (! rest.isEmpty()) {
		// build '+' or '-' infix expression
		TaggedExp tt = (TaggedExp)rest.pop() ;
		head = new L3_Exp_Impl (head, tt.tag, tt.exp) ;
	    } ;
	    return head ;
	} else if (exp.getLabel().equals("#Exp1")) {
	    L3_EXP head = convertExp (exp.getChildren()[0]) ;
	    TREE op1 = exp.getChildren()[1] ;
	    if (op1.getRhs() == L3_Parser.epsilon)
		return head ;
	    else {
		L3_EXP other = convertExp (op1.getChildren()[1]) ;
		char i = op1.getChildren()[0].getLabel().charAt(0) ;
		// build '=' or '<' infix expression
		return new L3_Exp_Impl (head, i, other) ;
	    }
	} else if (exp.getLabel().equals("#Exp")) {
	    if (exp.getRhs() == L3_Parser.Exp1)
		return convertExp (exp.getChildren()[0]) ;
	    else // construct if-expression
		return new L3_Exp_Impl
		    (convertExp (exp.getChildren()[1]),
		     convertExp (exp.getChildren()[3]),
		     convertExp (exp.getChildren()[5])) ;				       
	} else {
	    System.out.println ("Unexpected label " + exp.getLabel()) ;
	    return null ;
	}
    }

    static Stack convertOps4 (TREE ops4) {
	if (ops4.getRhs() == L3_Parser.epsilon)
	    return new Stack() ;
	else {
	    L3_EXP exp = convertExp (ops4.getChildren()[0]) ;
	    Stack stack = convertOps4 (ops4.getChildren()[1]) ;
	    stack.push(exp) ;
	    return stack ;
	}
    }

    static Stack convertOps3 (TREE ops3) {
	if (ops3.getRhs() == L3_Parser.epsilon)
	    return new Stack() ;
	else {
	    L3_EXP exp = convertExp (ops3.getChildren()[1]) ;
	    Stack stack = convertOps3 (ops3.getChildren()[2]) ;
	    stack.push(exp) ;
	    return stack ;
	}
    }

    static Stack convertOps2 (TREE ops2) {
	if (ops2.getRhs() == L3_Parser.epsilon)
	    return new Stack() ;
	else {
	    L3_EXP exp = convertExp (ops2.getChildren()[1]) ;
	    char tag = ops2.getChildren()[0].getLabel().charAt(0) ;
	    Stack stack = convertOps2 (ops2.getChildren()[2]) ;
	    stack.push (new TaggedExp(exp,tag)) ;
	    return stack ;
	}
    }

}




// Expression environments, associating names with closures.
// For use by runtime system.

class L3_Exp_Env {
    private java.util.TreeMap env ;
    L3_Exp_Env (java.util.TreeMap env) {this.env = env ;}
    public L3_EXP valueOf (String var) throws UnknownVariable {
	L3_EXP e = (L3_EXP)env.get(var) ;
	if (e == null) throw new UnknownVariable(var) ;
	else return e ;
    }
}
