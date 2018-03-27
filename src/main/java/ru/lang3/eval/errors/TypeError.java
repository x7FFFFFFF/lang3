package ru.lang3.eval.errors;

public class TypeError extends Exception {
    public TypeError (String s) {super ("Type error: " + s) ;}
}
