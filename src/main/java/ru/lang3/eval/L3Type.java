package ru.lang3.eval;

public interface L3Type {
    boolean isInteger() ;
    boolean isBool() ;
    boolean isArrow() ;
    L3Type left() ;    // returns left constituent of arrow type
    L3Type right() ;   // returns right constituent
    boolean equals (L3Type other) ;
    String toString() ; // for testing/debugging
}
