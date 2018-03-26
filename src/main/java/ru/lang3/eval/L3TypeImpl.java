package ru.lang3.eval;

import ru.lang3.parser.L3Parser;
import ru.lang3.parser.Tree;

public class L3TypeImpl implements L3Type {
    private int kind ;
    private L3Type leftChild ;
    private L3Type rightChild ;
    public boolean isInteger() {return kind==0 ;}
    public boolean isBool() {return kind==1 ;}
    public boolean isArrow() {return kind==2 ;}
    public L3Type left() {return leftChild ;}
    public L3Type right() {return rightChild ;}

    public boolean equals (L3Type other) {
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
    private L3TypeImpl(int kind, L3Type leftChild, L3Type rightChild) {
        this.kind = kind ;
        this.leftChild = leftChild ;
        this.rightChild = rightChild ;
    } ;

    // Constants for L3 types Integer and Bool
    public static L3Type IntegerType = new L3TypeImpl(0,null,null) ;
    public static L3Type BoolType = new L3TypeImpl(1,null,null) ;
    // Constructor for arrow types
    L3TypeImpl(L3Type leftChild, L3Type rightChild) {
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

    static L3Parser L3_Parser = new L3Parser() ;

    static L3Type convertType (Tree tree)
    {
        if (tree.getChildren()[1].getRhs() == L3_Parser.epsilon)
        {
            return convertType1(tree.getChildren()[0]) ;
        }
        else
        {
            L3Type left  = convertType1(tree.getChildren()[0]) ;
            L3Type right = convertType(tree.getChildren()[1].getChildren()[1]) ;
            return new L3TypeImpl(left,right) ;
        }
    }

    static L3Type convertType1 (Tree tree1)
    {
        if (tree1.getRhs() == L3_Parser.Integer)
        {
            return L3TypeImpl.IntegerType ;
        }
        else if (tree1.getRhs() == L3_Parser.Bool)
        {
            return L3TypeImpl.BoolType ;
        }
        else
        {
            return convertType(tree1.getChildren()[1]) ;
        }
    }

}
