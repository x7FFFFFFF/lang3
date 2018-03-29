package ru.lang3.parser;

public class STree implements Tree {

    String label ;        // Convention: nonterminals begin with "#".
    String value ;
    String[] rhs ;
    Tree[] children ;

    public String getLabel() {return label ;}
    public boolean isTerminal() {return (label.charAt(0) != '#') ;}
    public String getValue() {return value ;}
    public void setValue(String value) {this.value = value ;}
    public String[] getRhs() {return rhs ;}
    public Tree[] getChildren() {return children ;}
    public void setRhsChildren(String[] rhs, Tree[] children) {
        this.rhs = rhs ; this.children = children ;
    }

    // Constructors
    public STree (String label) {this.label = label ;}
}
