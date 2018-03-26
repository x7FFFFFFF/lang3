package ru.lang3.parser;

// Tiny example: Parser for grammar
// #S -> epsilon | EVEN #S && #S
// Hint: read EVEN as (, && as ).

public class EvenAndParser extends GenParser implements Parser {

    String startSymbol() {return "#S" ;}

    String[] epsilon      = new String[] { } ;
    String[] EVEN_S_AND_S = new String[] {"EVEN", "#S", "&&", "#S"} ;

    String[] tableEntry (String nonterm, String tokClass) {
        if (nonterm.equals("#S")) {
            if (tokClass == null) return epsilon ;
            else if (tokClass.equals("&&")) return epsilon ;
            else if (tokClass.equals("EVEN")) return EVEN_S_AND_S ;
            else return null ;
        }
        else return null ;
    }
    // N.B. All this use of strings isn't great for efficiency,
    // but at least it makes for relatively readable code.
}