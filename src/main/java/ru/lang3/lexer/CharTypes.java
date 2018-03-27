package ru.lang3.lexer;


// Some useful sets of characters.
public class CharTypes {

    public static boolean isLetter (char c) {
        return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) ;
    }

    public static boolean isSmall (char c) {
        return (('a' <= c && c <= 'z') || c == '_') ;
    }

    public static boolean isLarge (char c) {
        return ('A' <= c && c <= 'Z') ;
    }

    public static boolean isDigit (char c) {
        return ('0' <= c && c <= '9') ;
    }

    public static boolean isNonzeroDigit (char c) {
        return ('1' <= c && c <= '9') ;
    }

    public static boolean isSymbolic (char c) {
        return (c == '!' || c == '#' || c == '$' || c == '%' || c == '&' ||
                c == '*' || c == '+' || c == '.' || c == '/' || c == '<' ||
                c == '=' || c == '>' || c == '?' || c == '@' || c == '\\' ||
                c == '^' || c == '|' || c == '-' || c == '~' || c == ':') ;
    }

    public static boolean isWhitespace (char c) {
        return (c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f') ;
    }

    public static boolean isNewline (char c) {
        return (c == '\r' || c == '\n' || c == '\f') ;
    }

}