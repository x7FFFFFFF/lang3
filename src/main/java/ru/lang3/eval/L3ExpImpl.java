package ru.lang3.eval;


import ru.lang3.parser.L3Parser;
import ru.lang3.parser.Tree;

import java.util.Stack;

public class L3ExpImpl implements L3Exp {
    private int kind ;
    private String value ;
    private char infixOp ;
    private L3Exp firstChild ;
    private L3Exp secondChild ;
    private L3Exp thirdChild ;
    public boolean isVAR() {return kind==0 ;}
    public boolean isNUM() {return kind==1 ;}
    public boolean isBOOLEAN() {return kind==2 ;}
    public boolean isAPP() {return kind==3 ;}
    public boolean isINFIX() {return kind==4 ;}
    public boolean isIF() {return kind==5 ;}
    public boolean isLAMBDA() {return kind==6 ;}
    public String value() {return value ;}
    public char infixOp() {return infixOp ;}
    public L3Exp first() {return firstChild ;}
    public L3Exp second() {return secondChild ;}
    public L3Exp third() {return thirdChild ;}

    // Various constructors: number and type of arguments determine
    // the kind of expression.

    // For atomic expressions (VAR, NUM, BOOLEAN)
    L3ExpImpl(String lexClass, String value) {
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
    L3ExpImpl(L3Exp left, L3Exp right) {
        this.kind = 3 ;
        this.firstChild = left ;
        this.secondChild = right ;
    }

    // For infix expressions
    L3ExpImpl(L3Exp left, char infixOp, L3Exp right) {
        this.kind = 4 ;
        this.firstChild = left ;
        this.secondChild = right ;
        this.infixOp = infixOp ;
    }

    // For if-expressions
    L3ExpImpl(L3Exp condition, L3Exp branch1, L3Exp branch2) {
        this.kind = 5 ;
        this.firstChild = condition ;
        this.secondChild = branch1 ;
        this.thirdChild = branch2 ;
    }

    // For lambda-expressions
    L3ExpImpl(String var, L3Exp body) {
        this.kind = 6 ;
        this.value = var ;
        this.firstChild = body ;
    }


    // Converting parse trees to ASTs for expressions

    static L3Parser L3_Parser = L3TypeImpl.L3_Parser ;

    static class TaggedExp {
        L3Exp exp ;
        char tag ;
        TaggedExp (L3Exp exp, char tag) {
            this.exp = exp ; this.tag = tag ;
        }
    }

    static L3Exp convertExp (Tree exp) {
        if (exp.getLabel().equals("#Exp5")) {
            if (exp.getRhs() == L3_Parser.lbr_Exp_rbr)
                return convertExp (exp.getChildren()[1]) ;
            else {
                Tree terminal = exp.getChildren()[0] ;
                // build atomic expression
                return new L3ExpImpl
                        (terminal.getLabel(), terminal.getValue()) ;
            }
        } else if (exp.getLabel().equals("#Exp4")) {
            L3Exp head = convertExp (exp.getChildren()[0]) ;
            Stack rest = convertOps4 (exp.getChildren()[1]) ;
            while (! rest.isEmpty()) {
                // build application expression
                head = new L3ExpImpl(head, (L3Exp)(rest.pop())) ;
            } ;
            return head ;
        } else if (exp.getLabel().equals("#Exp3")) {
            L3Exp head = convertExp (exp.getChildren()[0]) ;
            Stack rest = convertOps3 (exp.getChildren()[1]) ;
            while (! rest.isEmpty()) {
                // build '*' infix expression
                head = new L3ExpImpl(head, '*', (L3Exp)(rest.pop())) ;
            } ;
            return head ;
        } else if (exp.getLabel().equals("#Exp2")) {
            L3Exp head = convertExp (exp.getChildren()[0]) ;
            Stack rest = convertOps2 (exp.getChildren()[1]) ;
            while (! rest.isEmpty()) {
                // build '+' or '-' infix expression
                TaggedExp tt = (TaggedExp)rest.pop() ;
                head = new L3ExpImpl(head, tt.tag, tt.exp) ;
            } ;
            return head ;
        } else if (exp.getLabel().equals("#Exp1")) {
            L3Exp head = convertExp (exp.getChildren()[0]) ;
            Tree op1 = exp.getChildren()[1] ;
            if (op1.getRhs() == L3_Parser.epsilon)
                return head ;
            else {
                L3Exp other = convertExp (op1.getChildren()[1]) ;
                char i = op1.getChildren()[0].getLabel().charAt(0) ;
                // build '=' or '<' infix expression
                return new L3ExpImpl(head, i, other) ;
            }
        } else if (exp.getLabel().equals("#Exp")) {
            if (exp.getRhs() == L3_Parser.Exp1)
                return convertExp (exp.getChildren()[0]) ;
            else // construct if-expression
                return new L3ExpImpl
                        (convertExp (exp.getChildren()[1]),
                                convertExp (exp.getChildren()[3]),
                                convertExp (exp.getChildren()[5])) ;
        } else {
            System.out.println ("Unexpected label " + exp.getLabel()) ;
            return null ;
        }
    }

    static Stack convertOps4 (Tree ops4) {
        if (ops4.getRhs() == L3_Parser.epsilon)
            return new Stack() ;
        else {
            L3Exp exp = convertExp (ops4.getChildren()[0]) ;
            Stack stack = convertOps4 (ops4.getChildren()[1]) ;
            stack.push(exp) ;
            return stack ;
        }
    }

    static Stack convertOps3 (Tree ops3) {
        if (ops3.getRhs() == L3_Parser.epsilon)
            return new Stack() ;
        else {
            L3Exp exp = convertExp (ops3.getChildren()[1]) ;
            Stack stack = convertOps3 (ops3.getChildren()[2]) ;
            stack.push(exp) ;
            return stack ;
        }
    }

    static Stack convertOps2 (Tree ops2) {
        if (ops2.getRhs() == L3_Parser.epsilon)
            return new Stack() ;
        else {
            L3Exp exp = convertExp (ops2.getChildren()[1]) ;
            char tag = ops2.getChildren()[0].getLabel().charAt(0) ;
            Stack stack = convertOps2 (ops2.getChildren()[2]) ;
            stack.push (new TaggedExp(exp,tag)) ;
            return stack ;
        }
    }

}