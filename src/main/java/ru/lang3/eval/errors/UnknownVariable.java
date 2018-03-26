package ru.lang3.eval.errors;

public class UnknownVariable extends Exception {
    public UnknownVariable (String var) {
        super("Variable " + var + " not in scope.") ;
        }
}