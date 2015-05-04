%{
#define YYSTYPE double
#include "ETALIS_Grammar.tab.h"
#include <stdlib.h>


%}

%option noyywrap
white [ \t]+
digit [0-9]
letter [a-zA-Z]
text {letter}+
integer {digit}+
exponent [eE][+-]?{integer}
real {integer}("."{integer})?{exponent}?
var  [_]{letter}{integer}

%%

{white} { }
{real} { yylval=atof(yytext);
 return NUMBER;
}
{var} { yylval=5;
return VAR;
}

"+"  return PLUS;
"-"  return MINUS;
"*"  return TIMES;
"/"  return DIVIDE;
"^"  return POWER;
"("  return LEFT;
")"  return RIGHT;
"\n" return END;
">"  return BIGGERTHAN;
"<"  return SMALLERTHAN;
","  return COMMA;
"="  return EQUAL;



