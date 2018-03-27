package ru.lang3.eval;

public interface L3Exp {
    boolean isVAR() ;
    boolean isNUM() ;
    boolean isBOOLEAN() ;
    boolean isAPP() ;
    boolean isINFIX() ;
    boolean isIF() ;
    boolean isLAMBDA() ; // only needed for evaluator: L3 itself doesn't have lambda expressions.

    String value() ;     // for VAR, NUM, BOOLEAN: returns e.g. "x", "5", "True"
    char infixOp() ;     // for infix expressions: returns '=', '<', '+', '-' or '*'
    L3Exp first() ;     // returns first child (for application, infix, if-expressions)
    L3Exp second() ;    // returns second child (for application, infix, if-expressions)
    L3Exp third() ;     // returns third child (for if-expressions only)
}
