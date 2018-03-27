package ru.lang3.eval.errors;

public class DuplicatedVariable extends Exception {
    public DuplicatedVariable (String var) {
        super("Duplicated variable " + var) ;
    }
}