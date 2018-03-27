package ru.lang3.parser;

public class L3Parser extends GenParser implements Parser
{
    String startSymbol() {return "#Prog" ;}

    public String[] epsilon              = new String[] { } ;
    String[] Decl_Prog            = new String[] {"#Decl", "#Prog"} ;
    String[] TypeDecl_TermDecl    = new String[] {"#TypeDecl", "#TermDecl"} ;
    String[] TypeDecl_rule        = new String[] {"VAR", "::", "#Type", ";"} ;
    String[] Type1_TypeOps        = new String[] {"#Type1", "#TypeOps"} ;
    public String[] Integer              = new String[] {"Integer"} ;
    public String[] Bool                 = new String[] {"Bool"} ;
    String[] lbr_Type_rbr         = new String[] {"(", "#Type", ")"} ;
    String[] arrow_Type           = new String[] {"->", "#Type"} ;
    String[] TermDecl_rule        = new String[] {"VAR", "#Args", "=", "#Exp", ";"} ;
    String[] VAR_Args             = new String[] {"VAR", "#Args"} ;
    public String[] Exp1                 = new String[] {"#Exp1"} ;
    String[] if_then_else_rule    = new String[] {"if", "#Exp", "then", "#Exp", "else", "#Exp"} ;
    String[] Exp2_Op1             = new String[] {"#Exp2", "#Op1"} ;
    String[] eq_rule              = new String[] {"==", "#Exp2"} ;
    String[] lt_rule              = new String[] {"<", "#Exp2"} ;
    String[] Exp3_Ops2            = new String[] {"#Exp3", "#Ops2"} ;
    String[] plus_rule            = new String[] {"+", "#Exp3", "#Ops2"} ;
    String[] minus_rule           = new String[] {"-", "#Exp3", "#Ops2"} ;
    String[] Exp4_Ops3            = new String[] {"#Exp4", "#Ops3"} ;
    String[] times_rule           = new String[] {"*", "#Exp4", "#Ops3"} ;
    String[] Exp5_Ops4            = new String[] {"#Exp5", "#Ops4"} ;
    String[] VAR                  = new String[] {"VAR"} ;
    String[] NUM                  = new String[] {"NUM"} ;
    String[] BOOLEAN              = new String[] {"BOOLEAN"} ;
    public String[] lbr_Exp_rbr          = new String[] {"(", "#Exp", ")"} ;

    private void printError(String nonterm, String tokClass, String legalExpansions)
    {
        System.err.printf("An error has occured while parsing\n");
        System.err.printf("\tInput does not comply to grammar\n");
        System.err.printf("\t\tNot possible to expand %s to %s\n", nonterm, tokClass);
        System.err.printf("\t\tHint: if we expect %s we should next see %s%s\n\n", nonterm, legalExpansions.indexOf(" ") > -1 ? "one of " : "", legalExpansions);
    }

    String[] tableEntry(String nonterm, String tokClass)
    {
        if (nonterm.equals("#Prog"))
        {
            if (tokClass == null)
            {
                return epsilon;
            }
            else if (tokClass.equals("VAR"))
            {
                return Decl_Prog;
            }
            else
            {
                printError(nonterm, tokClass, "$ VAR");
                return null;
            }
        }
        else if (nonterm.equals("#Decl"))
        {
            if (tokClass == null)
            {
                return epsilon;
            }
            else if (tokClass.equals("VAR"))
            {
                return TypeDecl_TermDecl;
            }
            else
            {
                printError(nonterm, tokClass, "$ VAR");
                return null;
            }
        }
        else if (nonterm.equals("#TypeDecl"))
        {
            if (tokClass.equals("VAR"))
            {
                return TypeDecl_rule;
            }
            else
            {
                printError(nonterm, tokClass, "VAR");
                return null;
            }
        }
        else if (nonterm.equals("#Type"))
        {
            if (tokClass.equals("Integer"))
            {
                return Type1_TypeOps;
            }
            else if (tokClass.equals("Bool"))
            {
                return Type1_TypeOps;
            }
            else if (tokClass.equals("("))
            {
                return Type1_TypeOps;
            }
            else
            {
                printError(nonterm, tokClass, "Integer Bool (");
                return null;
            }
        }
        else if (nonterm.equals("#Type1"))
        {
            if (tokClass.equals("Integer"))
            {
                return Integer;
            }
            else if (tokClass.equals("Bool"))
            {
                return Bool;
            }
            else if (tokClass.equals("("))
            {
                return lbr_Type_rbr;
            }
            else
            {
                printError(nonterm, tokClass, "Integer Bool (");
                return null;
            }
        }
        else if (nonterm.equals("#TypeOps"))
        {
            if (tokClass.equals(")"))
            {
                return epsilon;
            }
            else if (tokClass.equals(";"))
            {
                return epsilon;
            }
            else if (tokClass.equals("->"))
            {
                return arrow_Type;
            }
            else
            {
                printError(nonterm, tokClass, ") ; ->");
                return null;
            }
        }
        else if (nonterm.equals("#TermDecl"))
        {
            if (tokClass.equals("VAR"))
            {
                return TermDecl_rule;
            }
            else
            {
                printError(nonterm, tokClass, "VAR");
                return null;
            }
        }
        else if (nonterm.equals("#Args"))
        {
            if (tokClass.equals("VAR"))
            {
                return VAR_Args;
            }
            else if (tokClass.equals("="))
            {
                return epsilon;
            }
            else
            {
                printError(nonterm, tokClass, "VAR =");
                return null;
            }
        }
        else if (nonterm.equals("#Exp"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp1;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp1;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp1;
            }
            else if (tokClass.equals("("))
            {
                return Exp1;
            }
            else if (tokClass.equals("if"))
            {
                return if_then_else_rule;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN ( if");
                return null;
            }
        }
        else if (nonterm.equals("#Exp1"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp2_Op1;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp2_Op1;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp2_Op1;
            }
            else if (tokClass.equals("("))
            {
                return Exp2_Op1;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN (");
                return null;
            }
        }
        else if (nonterm.equals("#Op1"))
        {
            if (tokClass.equals("then"))
            {
                return epsilon;
            }
            else if (tokClass.equals("else"))
            {
                return epsilon;
            }
            else if (tokClass.equals(")"))
            {
                return epsilon;
            }
            else if (tokClass.equals(";"))
            {
                return epsilon;
            }
            else if (tokClass.equals("=="))
            {
                return eq_rule;
            }
            else if (tokClass.equals("<"))
            {
                return lt_rule;
            }
            else
            {
                printError(nonterm, tokClass, "then else ) ; == <");
                return null;
            }
        }
        else if (nonterm.equals("#Exp2"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp3_Ops2;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp3_Ops2;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp3_Ops2;
            }
            else if (tokClass.equals("("))
            {
                return Exp3_Ops2;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN (");
                return null;
            }
        }
        else if (nonterm.equals("#Ops2"))
        {
            if (tokClass.equals("then"))
            {
                return epsilon;
            }
            else if (tokClass.equals("else"))
            {
                return epsilon;
            }
            else if (tokClass.equals(")"))
            {
                return epsilon;
            }
            else if (tokClass.equals(";"))
            {
                return epsilon;
            }
            else if (tokClass.equals("=="))
            {
                return epsilon;
            }
            else if (tokClass.equals("<"))
            {
                return epsilon;
            }
            else if (tokClass.equals("+"))
            {
                return plus_rule;
            }
            else if (tokClass.equals("-"))
            {
                return minus_rule;
            }
            else
            {
                printError(nonterm, tokClass, "then else ) ; == < + -");
                return null;
            }
        }
        else if (nonterm.equals("#Exp3"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp4_Ops3;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp4_Ops3;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp4_Ops3;
            }
            else if (tokClass.equals("("))
            {
                return Exp4_Ops3;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN (");
                return null;
            }
        }
        else if (nonterm.equals("#Ops3"))
        {
            if (tokClass.equals("then"))
            {
                return epsilon;
            }
            else if (tokClass.equals("else"))
            {
                return epsilon;
            }
            else if (tokClass.equals(")"))
            {
                return epsilon;
            }
            else if (tokClass.equals(";"))
            {
                return epsilon;
            }
            else if (tokClass.equals("=="))
            {
                return epsilon;
            }
            else if (tokClass.equals("<"))
            {
                return epsilon;
            }
            else if (tokClass.equals("+"))
            {
                return epsilon;
            }
            else if (tokClass.equals("-"))
            {
                return epsilon;
            }
            else if (tokClass.equals("*"))
            {
                return times_rule;
            }
            else
            {
                printError(nonterm, tokClass, "then else ) ; == < + - *");
                return null;
            }
        }
        else if (nonterm.equals("#Exp4"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("("))
            {
                return Exp5_Ops4;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN (");
                return null;
            }
        }
        else if (nonterm.equals("#Ops4"))
        {
            if (tokClass.equals("VAR"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("NUM"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals("then"))
            {
                return epsilon;
            }
            else if (tokClass.equals("else"))
            {
                return epsilon;
            }
            else if (tokClass.equals("("))
            {
                return Exp5_Ops4;
            }
            else if (tokClass.equals(")"))
            {
                return epsilon;
            }
            else if (tokClass.equals(";"))
            {
                return epsilon;
            }
            else if (tokClass.equals("=="))
            {
                return epsilon;
            }
            else if (tokClass.equals("<"))
            {
                return epsilon;
            }
            else if (tokClass.equals("+"))
            {
                return epsilon;
            }
            else if (tokClass.equals("-"))
            {
                return epsilon;
            }
            else if (tokClass.equals("*"))
            {
                return epsilon;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN then else ( ) ; == < + - *");
                return null;
            }
        }
        else if (nonterm.equals("#Exp5"))
        {
            if (tokClass.equals("VAR"))
            {
                return VAR;
            }
            else if (tokClass.equals("NUM"))
            {
                return NUM;
            }
            else if (tokClass.equals("BOOLEAN"))
            {
                return BOOLEAN;
            }
            else if (tokClass.equals("("))
            {
                return lbr_Exp_rbr;
            }
            else
            {
                printError(nonterm, tokClass, "VAR NUM BOOLEAN (");
                return null;
            }
        }
        else
        {
            return null;
        }
    }
}