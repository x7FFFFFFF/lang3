package ru.lang3;

import java.util.* ;
import java.io.* ;

class L3_Typechecker
{
    static L3_Parser L3_Parser = L3_Type_Impl.L3_Parser ;
    
    // The core of the typechecker:
    // Computing the L3_TYPE of a given L3_EXP in a given TYPE_ENV.
    // Should raise TypeError if L3_EXP isn't well-typed
    
    static L3_TYPE IntegerType = L3_Type_Impl.IntegerType ;
    static L3_TYPE BoolType    = L3_Type_Impl.BoolType ;
    
    static L3_TYPE computeType(L3_EXP exp, TYPE_ENV env) throws TypeError, UnknownVariable
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
            L3_TYPE firstType   = computeType(exp.first(), env);
            L3_TYPE secondType  = computeType(exp.second(), env);
            
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
            L3_TYPE firstType  = computeType(exp.first(),  env);
            L3_TYPE secondType = computeType(exp.second(), env);
            L3_TYPE thirdType  = computeType(exp.third(),  env);
            
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
    L3_TYPE typeOf (String var) throws UnknownVariable ;
    }

    static class L3_Type_Env implements TYPE_ENV {

    TreeMap env ;

    public L3_TYPE typeOf (String var) throws UnknownVariable {
        L3_TYPE t = (L3_TYPE)(env.get(var)) ;
        if (t == null) throw new UnknownVariable(var) ;
        else return t ;
    }

    // Constructor for cloning a type env
    L3_Type_Env (L3_Type_Env given) {
        this.env = (TreeMap)given.env.clone() ;
    }

    // Constructor for building a type env from the type decls 
    // appearing in a program
    L3_Type_Env (TREE prog) throws DuplicatedVariable {
        this.env = new TreeMap() ;
        TREE prog1 = prog ;
        while (prog1.getRhs() != L3_Parser.epsilon) {
        TREE typeDecl = prog1.getChildren()[0].getChildren()[0] ;
        String var = typeDecl.getChildren()[0].getValue() ;
        L3_TYPE theType = L3_Type_Impl.convertType
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
    L3_TYPE addArgBindings (TREE args, L3_TYPE theType)
        throws DuplicatedVariable, TypeError {
        TREE args1=args ;
        L3_TYPE theType1 = theType ;
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

    static L3_Type_Env compileTypeEnv (TREE prog)
    throws DuplicatedVariable{
    return new L3_Type_Env (prog) ;
    }

    // Building a closure (using lambda) from argument list and body
    static L3_EXP buildClosure (TREE args, L3_EXP exp) {
    if (args.getRhs() == L3_Parser.epsilon)
        return exp ;
    else {
        L3_EXP exp1 = buildClosure (args.getChildren()[1], exp) ;
        String var = args.getChildren()[0].getValue() ;
        return new L3_Exp_Impl (var, exp1) ;
    }
    }

    // Name-closure pairs (result of processing a TermDecl).
    static class Named_L3_EXP {
    String name ; L3_EXP exp ;
    Named_L3_EXP (String name, L3_EXP exp) {
        this.name = name; this.exp = exp ;
    }
    }

    static Named_L3_EXP typecheckDecl (TREE decl, L3_Type_Env env)
    throws TypeError, UnknownVariable, DuplicatedVariable,
           NameMismatchError {
    // typechecks the given decl against the env, 
    // and returns a name-closure pair for the entity declared.
    String theVar = decl.getChildren()[0].getChildren()[0].getValue();
    String theVar1= decl.getChildren()[1].getChildren()[0].getValue();
    if (!theVar.equals(theVar1)) 
        throw new NameMismatchError(theVar,theVar1) ; 
    L3_TYPE theType =
        L3_Type_Impl.convertType (decl.getChildren()[0].getChildren()[2]) ;
    L3_EXP theExp =
        L3_Exp_Impl.convertExp (decl.getChildren()[1].getChildren()[3]) ;
    TREE theArgs = decl.getChildren()[1].getChildren()[1] ;
    L3_Type_Env theEnv = new L3_Type_Env (env) ;
    L3_TYPE resultType = theEnv.addArgBindings (theArgs, theType) ;
    L3_TYPE expType = computeType (theExp, theEnv) ;
    if (expType.equals(resultType)) {
        return new Named_L3_EXP (theVar,buildClosure(theArgs,theExp));
    }
    else throw new TypeError ("RHS of declaration of " +
                  theVar + " has wrong type") ;
    }

    static L3_Exp_Env typecheckProg (TREE prog, L3_Type_Env env)
    throws TypeError, UnknownVariable, DuplicatedVariable,
           NameMismatchError {
    TREE prog1 = prog ;
    TreeMap treeMap = new TreeMap() ;
    while (prog1.getRhs() != L3_Parser.epsilon) {
        TREE theDecl = prog1.getChildren()[0] ;
        Named_L3_EXP binding = typecheckDecl (theDecl, env) ;
        treeMap.put (binding.name, binding.exp) ;
        prog1 = prog1.getChildren()[1] ;
    }
    System.out.println ("Typecheck successful.") ;
    return new L3_Exp_Env (treeMap) ;
    }

    // For testing:

    public static void main (String[] args) throws Exception {
    Reader reader = new BufferedReader (new FileReader (args[0])) ;
    // try {
        LEX_TOKEN_STREAM L3_Lexer =
        new CheckedSymbolLexer (new L3_Lexer (reader)) ;
        TREE prog = L3_Parser.parseTokenStream (L3_Lexer) ;
        L3_Type_Env typeEnv = compileTypeEnv (prog) ;
        L3_Exp_Env runEnv = typecheckProg (prog, typeEnv) ;
    // } catch (Exception x) {
        //  System.out.println ("L3 Error: " + x.getMessage()) ;
    // }
    }
}
