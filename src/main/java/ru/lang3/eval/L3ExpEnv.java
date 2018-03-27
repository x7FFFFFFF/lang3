package ru.lang3.eval;

// Expression environments, associating names with closures.
// For use by runtime system.


import ru.lang3.eval.errors.UnknownVariable;

public class L3ExpEnv {
    private java.util.TreeMap env ;
    L3ExpEnv(java.util.TreeMap env) {this.env = env ;}
    public L3Exp valueOf (String var) throws UnknownVariable {
        L3Exp e = (L3Exp)env.get(var) ;
        if (e == null) throw new UnknownVariable(var) ;
        else return e ;
    }
}
