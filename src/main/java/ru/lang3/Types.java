package ru.lang3;


// Provides abstract syntax trees for types:
// effectively, trees for the simplified grammar
//     Type  -->   Integer | Bool | Type->Type


interface L3_TYPE {
    boolean isInteger() ;
    boolean isBool() ;
    boolean isArrow() ;
    L3_TYPE left() ;    // returns left constituent of arrow type
    L3_TYPE right() ;   // returns right constituent
    boolean equals (L3_TYPE other) ;
    String toString() ; // for testing/debugging
}

class L3_Type_Impl implements L3_TYPE {
    private int kind ;
    private L3_TYPE leftChild ;
    private L3_TYPE rightChild ;
    public boolean isInteger() {return kind==0 ;}
    public boolean isBool() {return kind==1 ;}
    public boolean isArrow() {return kind==2 ;}
    public L3_TYPE left() {return leftChild ;}
    public L3_TYPE right() {return rightChild ;}

    public boolean equals (L3_TYPE other) {
	return ((this.isInteger() && other.isInteger()) ||
		(this.isBool() && other.isBool()) ||
		(this.isArrow() && other.isArrow() && 
		 this.left().equals(other.left()) &&
		 this.right().equals(other.right()))) ;
    }

    public String toString () {
	if (this.isInteger()) return "Integer" ;
	else if (this.isBool()) return "Bool" ;
	else return ("(" + this.left().toString() + " -> "
		     + this.right().toString() + ")") ;
    }

    // Constructors
    private L3_Type_Impl (int kind, L3_TYPE leftChild, L3_TYPE rightChild) {
	this.kind = kind ;
	this.leftChild = leftChild ;
	this.rightChild = rightChild ;
    } ;

    // Constants for L3 types Integer and Bool
    public static L3_TYPE IntegerType = new L3_Type_Impl (0,null,null) ;
    public static L3_TYPE BoolType = new L3_Type_Impl (1,null,null) ;
    // Constructor for arrow types
    L3_Type_Impl (L3_TYPE leftChild, L3_TYPE rightChild) {
	this (2, leftChild, rightChild) ;
    }

    // Conversion from parse trees to ASTs for L3 types

    // convertType accepts any well-formed tree whose root node has label
    // #Type, and returns the corresponding abstract syntax tree 
    // (see Types.java for the definition of ASTs for types).
    // convertType1 does the same for #Type1.
    // The relevant LL(1) grammar rules are:

    // #Type    -> #Type1 #TypeOps
    // #Type1   -> Integer | Bool | ( #Type )
    // #TypeOps -> epsilon | -> #Type

    // Since trees with label #Type can have subtrees of label #Type1
    // and vice versa, these two methods are mutually recursive.
    
    static L3_Parser L3_Parser = new L3_Parser() ;

    static L3_TYPE convertType (TREE tree)
	{
		if (tree.getChildren()[1].getRhs() == L3_Parser.epsilon)
		{
			return convertType1(tree.getChildren()[0]) ;
		}
		else
		{
			L3_TYPE left  = convertType1(tree.getChildren()[0]) ;
			L3_TYPE right = convertType(tree.getChildren()[1].getChildren()[1]) ;
			return new L3_Type_Impl (left,right) ;
		}
    }

    static L3_TYPE convertType1 (TREE tree1)
	{
		if (tree1.getRhs() == L3_Parser.Integer)
		{
			return L3_Type_Impl.IntegerType ;
		}
		else if (tree1.getRhs() == L3_Parser.Bool)
		{
			return L3_Type_Impl.BoolType ;
		}
		else
		{
			return convertType(tree1.getChildren()[1]) ;
		}
	}

}

// Errors that may arise during typechecking:

class TypeError extends Exception {
    TypeError (String s) {super ("Type error: " + s) ;}
}

class UnknownVariable extends Exception {
    public UnknownVariable (String var) {
	super("Variable " + var + " not in scope.") ;
    }
}

class DuplicatedVariable extends Exception {
    public DuplicatedVariable (String var) {
	super("Duplicated variable " + var) ;
    }
}

class NameMismatchError extends Exception {
    public NameMismatchError (String var1, String var2) {
	super("Name mismatch between " + var1 + " and " + var2) ;
    }
}

