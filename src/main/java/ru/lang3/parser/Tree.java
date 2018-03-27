package ru.lang3.parser;

// Recursive class for syntax tree nodes (any grammar).
// The same class serves for both terminal and non-terminal nodes.

public interface Tree {
    String getLabel() ;    // nonterminal symbol or lexical class of terminal
    boolean isTerminal() ;
    String getValue() ;    // only relevant for terminal nodes
    void setValue(String value) ;
    String[] getRhs() ;    // only relevant for non-terminal nodes
    Tree[] getChildren() ; // ditto
    void setRhsChildren(String[] rhs, Tree[] children) ;
}
