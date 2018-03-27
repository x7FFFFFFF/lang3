package ru.lang3.eval;

import ru.lang3.eval.errors.DuplicatedVariable;
import ru.lang3.eval.errors.NameMismatchError;
import ru.lang3.eval.errors.TypeError;
import ru.lang3.eval.errors.UnknownVariable;
import ru.lang3.lexer.CheckedSymbolLexer;
import ru.lang3.lexer.L3Lexer;
import ru.lang3.lexer.LexTokenStream;
import ru.lang3.parser.L3Parser;
import ru.lang3.parser.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.TreeMap;

public class L3Typechecker
{
    static L3Parser L3_Parser = L3TypeImpl.L3_Parser ;

    // The core of the typechecker:
    // Computing the L3_TYPE of a given L3_EXP in a given TYPE_ENV.
    // Should raise TypeError if L3_EXP isn't well-typed

    static L3Type IntegerType = L3TypeImpl.IntegerType ;
    static L3Type BoolType    = L3TypeImpl.BoolType ;

    static L3Type computeType(L3Exp exp, TYPE_ENV env) throws TypeError, UnknownVariable
    {
        if (exp.isVAR())
        {
            return env.typeOf(exp.value());
        }
        else if (exp.isNUM())
        {
            String value = exp.value();

            for (char c : value.toCharArray())
            {
                if (!(c >= '0' && c <= '9'))
                {
                    throw new TypeError("IntegerType " + value + " contains non-digit character " + c);
                }
            }

            return IntegerType;
        }
        else if (exp.isBOOLEAN())
        {
            String value = exp.value();

            if (!(value.equals("True") || value.equals("False")))
            {
                throw new TypeError("BoolType " + value + " does not match \"True\" or \"False\"");
            }

            return BoolType;
        }
        else if (exp.isAPP())
        {
            L3Type firstType   = computeType(exp.first(), env);
            L3Type secondType  = computeType(exp.second(), env);

            if (!firstType.isArrow())
            {
                throw new TypeError("Using " + secondType + " as function");
            }

            if (!firstType.left().equals(secondType))
            {
                throw new TypeError("Argument type does not match expected type");
            }

            return firstType.right();
        }
        else if (exp.isINFIX())
        {
            char infixOp = exp.infixOp();
            boolean firstIsIntegerType  = computeType(exp.first(),  env).equals(IntegerType);
            boolean secondIsIntegerType = computeType(exp.second(), env).equals(IntegerType);

            if (!(firstIsIntegerType && secondIsIntegerType))
            {
                throw new TypeError("Infix " + infixOp + " applied to non IntegerType arguments");
            }

            switch (infixOp)
            {
                case '=':
                case '<':    return BoolType;
                case '+':
                case '-':
                case '*':    return IntegerType;

                default:    throw new TypeError("Infix operator " + infixOp + " not in language");
            }
        }
        else if (exp.isIF())
        {
            L3Type firstType  = computeType(exp.first(),  env);
            L3Type secondType = computeType(exp.second(), env);
            L3Type thirdType  = computeType(exp.third(),  env);

            if (!firstType.equals(BoolType))
            {
                throw new TypeError("if-then-else expression doesn't begin with boolean condition");
            }

            if (!secondType.equals(thirdType))
            {
                throw new TypeError("Types of \"then\" and \"else\" in if-then-else expression differ");
            }

            return thirdType;
        }
        else
        {
            throw new TypeError("Expression not in language");
        }
    }


    // Type environments:

    interface TYPE_ENV {
        L3Type typeOf (String var) throws UnknownVariable ;
    }

    static class L3_Type_Env implements TYPE_ENV {

        TreeMap env ;

        public L3Type typeOf (String var) throws UnknownVariable {
            L3Type t = (L3Type)(env.get(var)) ;
            if (t == null) throw new UnknownVariable(var) ;
            else return t ;
        }

        // Constructor for cloning a type env
        L3_Type_Env (L3_Type_Env given) {
            this.env = (TreeMap)given.env.clone() ;
        }

        // Constructor for building a type env from the type decls
        // appearing in a program
        L3_Type_Env (Tree prog) throws DuplicatedVariable {
            this.env = new TreeMap() ;
            Tree prog1 = prog ;
            while (prog1.getRhs() != L3_Parser.epsilon) {
                Tree typeDecl = prog1.getChildren()[0].getChildren()[0] ;
                String var = typeDecl.getChildren()[0].getValue() ;
                L3Type theType = L3TypeImpl.convertType
                        (typeDecl.getChildren()[2]);
                if (env.containsKey(var))
                    throw new DuplicatedVariable(var) ;
                else env.put(var,theType) ;
                prog1 = prog1.getChildren()[1] ;
            }
            System.out.println ("Type conversions successful.") ;
        }

        // Augmenting a type env with a list of function arguments.
        // Takes the type of the function, returns the result type.
        L3Type addArgBindings (Tree args, L3Type theType)
                throws DuplicatedVariable, TypeError {
            Tree args1=args ;
            L3Type theType1 = theType ;
            while (args1.getRhs() != L3_Parser.epsilon) {
                if (theType1.isArrow()) {
                    String var = args1.getChildren()[0].getValue() ;
                    if (env.containsKey(var)) {
                        throw new DuplicatedVariable(var) ;
                    } else {
                        this.env.put(var, theType1.left()) ;
                        theType1 = theType1.right() ;
                        args1 = args1.getChildren()[1] ;
                    }
                } else throw new TypeError ("Too many function arguments");
            } ;
            return theType1 ;
        }
    }

    static L3_Type_Env compileTypeEnv (Tree prog)
            throws DuplicatedVariable{
        return new L3_Type_Env (prog) ;
    }

    // Building a closure (using lambda) from argument list and body
    static L3Exp buildClosure (Tree args, L3Exp exp) {
        if (args.getRhs() == L3_Parser.epsilon)
            return exp ;
        else {
            L3Exp exp1 = buildClosure (args.getChildren()[1], exp) ;
            String var = args.getChildren()[0].getValue() ;
            return new L3ExpImpl(var, exp1) ;
        }
    }

    // Name-closure pairs (result of processing a TermDecl).
    static class Named_L3_EXP {
        String name ; L3Exp exp ;
        Named_L3_EXP (String name, L3Exp exp) {
            this.name = name; this.exp = exp ;
        }
    }

    static Named_L3_EXP typecheckDecl (Tree decl, L3_Type_Env env)
            throws TypeError, UnknownVariable, DuplicatedVariable,
            NameMismatchError {
        // typechecks the given decl against the env,
        // and returns a name-closure pair for the entity declared.
        String theVar = decl.getChildren()[0].getChildren()[0].getValue();
        String theVar1= decl.getChildren()[1].getChildren()[0].getValue();
        if (!theVar.equals(theVar1))
            throw new NameMismatchError(theVar,theVar1) ;
        L3Type theType =
                L3TypeImpl.convertType (decl.getChildren()[0].getChildren()[2]) ;
        L3Exp theExp =
                L3ExpImpl.convertExp (decl.getChildren()[1].getChildren()[3]) ;
        Tree theArgs = decl.getChildren()[1].getChildren()[1] ;
        L3_Type_Env theEnv = new L3_Type_Env (env) ;
        L3Type resultType = theEnv.addArgBindings (theArgs, theType) ;
        L3Type expType = computeType (theExp, theEnv) ;
        if (expType.equals(resultType)) {
            return new Named_L3_EXP (theVar,buildClosure(theArgs,theExp));
        }
        else throw new TypeError ("RHS of declaration of " +
                theVar + " has wrong type") ;
    }

    static L3ExpEnv typecheckProg (Tree prog, L3_Type_Env env)
            throws TypeError, UnknownVariable, DuplicatedVariable,
            NameMismatchError {
        Tree prog1 = prog ;
        TreeMap treeMap = new TreeMap() ;
        while (prog1.getRhs() != L3_Parser.epsilon) {
            Tree theDecl = prog1.getChildren()[0] ;
            Named_L3_EXP binding = typecheckDecl (theDecl, env) ;
            treeMap.put (binding.name, binding.exp) ;
            prog1 = prog1.getChildren()[1] ;
        }
        System.out.println ("Typecheck successful.") ;
        return new L3ExpEnv(treeMap) ;
    }

    // For testing:

    public static void main (String[] args) throws Exception {
        Reader reader = new BufferedReader(new FileReader(args[0])) ;
        // try {
        LexTokenStream L3_Lexer =
                new CheckedSymbolLexer(new L3Lexer(reader)) ;
        Tree prog = L3_Parser.parseTokenStream (L3_Lexer) ;
        L3_Type_Env typeEnv = compileTypeEnv (prog) ;
        L3ExpEnv runEnv = typecheckProg (prog, typeEnv) ;
        // } catch (Exception x) {
        //  System.out.println ("L3 Error: " + x.getMessage()) ;
        // }
    }
}