package ru.lang3.eval.errors;

public class NameMismatchError extends Exception {
    public NameMismatchError (String var1, String var2) {
        super("Name mismatch between " + var1 + " and " + var2) ;
    }
}
