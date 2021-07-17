package scanner;

import java_cup.runtime.Symbol;
import parser.Symbols;
import java.util.List;
import java.util.ArrayList;

%%
/*
*
*   Optionen
*
*/

%public
%class Scanner

%cupsym Symbols
%implements Symbols
%cup
%unicode
%standalone
%char
%line
%column

/*
*
*   Hilfsfunktionen gemäss https://www.jflex.de/manual.html
*
*/

%{

    private boolean printTokens = false;
    private List<Symbol> tokens;

    public Scanner(java.io.Reader in, boolean printTokens) {
        this(in);
        this.printTokens = printTokens;
        this.tokens = new ArrayList<>();
    }

    private Symbol sym(int type) {
        Symbol s = new Symbol(type, yyline, yycolumn, yytext());
        this.tokens.add(s);
        return s;
    }

    private Symbol symVal(int type) {
        Symbol s = new Symbol(type, yyline, yycolumn, yytext());
        this.tokens.add(s);
        return s;
    }

%}
%eof{
    if (printTokens) {
        System.out.println("");
        System.out.println("Scanned tokens:");
        System.out.println("");

        for(Symbol s : this.tokens) {
            System.out.println("Found <" + s.value + "> at line " + s.left + ", column " + s.right + ", matches token {" + this.terminalNames[s.sym] + "}.");
        }
    }
%eof}
/*
*
*   Definition der Tokens
*
*/

equal = =

addSub = [\+\-]
multDiv = [\*\/]


number = [0-9]+(.[0-9]+)?

doubleEqual = ==
notEqual = \!=
greater = >
smaller = <
greaterEqual = >=
smallerEqual = <=

if = if
then = then
else = else
colon = :

boolean = True | False

leftBracket = \(
rightBracket = \)
leftCurlyBracket = \{
rightCurlyBracket = \}

while = while

separator = ,

arrow = =>

characters = \"[^\"]*\"

terminator = ;
return = return
print = print


undefined = undefined

identifier = [a-zA-z_]+[0-9]*

COMMENT = "/*"~"*/"

WHITESPACE = [ \t\f\r\n]+
ERR = [^]

%%

/*
*
*   Umwandlung der gelesenen Tokens in Symbole
*
*/

{equal}                { return sym(EQUAL); }
{addSub}               { return symVal(ADD_SUB); }
{multDiv}              { return symVal(MULT_DIV); }

{number}               { return symVal(NUMBER); }
{doubleEqual}          { return symVal(DOUBLE_EQUAL); }
{notEqual}             { return symVal(NOT_EQUAL); }
{greater}              { return symVal(GREATER); }
{smaller}              { return symVal(SMALLER); }
{greaterEqual}         { return symVal(GREATER_EQUAL); }
{smallerEqual}         { return symVal(SMALLER_EQUAL); }

{if}                   { return sym(IF); }
{then}                 { return sym(THEN); }
{else}                 { return sym(ELSE); }
{colon}                { return sym(COLON); }
{boolean}              { return symVal(BOOLEAN_VALUE); }
{leftBracket}          { return sym(BRACKETS_LEFT); }
{rightBracket}         { return sym(BRACKETS_RIGTH); }
{leftCurlyBracket}     { return sym(CURLY_BRACKETS_LEFT); }
{rightCurlyBracket}    { return sym(CURLY_BRACKETS_RIGTH); }
{while}                { return sym(WHILE); }
{separator}            { return sym(SEPARATOR); }
{arrow}                { return sym(ARROW); }
{characters}           { return symVal(CHARACTERS); }
{terminator}           { return sym(TERMINATOR); }
{return}               { return sym(RETURN); }
{print}                { return sym(PRINT); }

{undefined}            { return sym(UNDEFINED); }
{identifier}           { return symVal(IDENTIFIER); }

{COMMENT}              { /* ignore */ }
{WHITESPACE}           { /* ignore */ }
{ERR}                  { throw new Error("Illegal character <"+ yytext()+">"); }
