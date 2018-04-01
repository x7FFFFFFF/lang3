package ru.lang3.parser;

import java.util.Arrays;

public class STree implements Tree {

    private String label ;        // Convention: nonterminals begin with "#".
    private String value ;
    private String[] rhs ;
    private Tree[] children ;

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

    @Override
    public String toString() {
        return "STree{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                ", rhs=" + Arrays.toString(rhs) +
                '}';
    }
}
