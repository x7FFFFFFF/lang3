package ru.lang3.parser;

import ru.lang3.lexer.LexToken;
import ru.lang3.lexer.LexTokenStream;

import java.util.Stack;

public abstract class GenParser implements Parser {

    // Stubs for methods specific to a particular grammar
    abstract String startSymbol() ;
    abstract String[] tableEntry (String nonterm, String tokenType) ;
    // LL(1) parse table - should return null for blank entries.
    // In the second argument, null serves as the end-of-input marker '$'.

    // The LL(1) parsing algorithm, as in lectures

    public Tree parseTokenStream (LexTokenStream tokStream)
            throws Exception {
        return parseTokenStreamAs (tokStream, this.startSymbol()) ;
    }

    public Tree parseTokenStreamAs
            (LexTokenStream tokStream, String nonterm)
            throws Exception {
        Stack<STree> theStack = new Stack<>() ;
        STree rootNode = new STree (nonterm) ;
        theStack.push(rootNode) ;
        STree currNode ;
        String currLabel ;
        LexToken currToken ;
        String currLexClass ;
        do {
            currNode = theStack.pop();
            currLabel = currNode.getLabel() ;
            currToken = tokStream.peekProperToken() ;
            if (currToken == null) {
                currLexClass = null ;
            } else {
                currLexClass = currToken.lexClass() ;
            }
            if (currNode.isTerminal()) {
                // match expected terminal against input token
                if (currLexClass != null &&
                        currLexClass.equals(currLabel)) {
                    // all OK
                    currNode.setValue (currToken.value()) ;
                    tokStream.pullToken() ;
                } else { // report error: expected terminal not found
                    if (currToken == null) {
                        throw new UnexpectedInput
                                (currLabel, "end of input") ;
                    } else throw new UnexpectedInput
                            (currLabel, currLexClass) ;
                }
            } else {
                // lookup expected nonterminal vs input token in table
                // OK if currLexClass is null (end-of-input marker)
                String[] rhs = tableEntry (currLabel, currLexClass) ;
                if (rhs != null) {
                    STree[] children = new STree[rhs.length] ;
                    for (int i=0; i<rhs.length; i++) {
                        children[i] = new STree(rhs[i]) ;
                    }
                    currNode.setRhsChildren(rhs,children) ;
                    for (int i=rhs.length-1; i>=0; i--) {
                        theStack.push(children[i]) ;
                    }
                } else {
                    // report error: blank entry in table
                    throw new UnexpectedInput (currLabel, currLexClass) ;
                }
            }
        } while (!theStack.empty()) ;
        LexToken next = tokStream.pullProperToken() ;
        if (next != null) {
            // non-fatal warning: parse completed before end of input
            System.out.println ("Warning: " + next.value() +
                    " found after parse completed.");
        } else {
            System.out.println ("Parse successful.") ;
        }
        return rootNode ;
    }

    // Perhaps add method for parsing as a specified nonterminal
}
