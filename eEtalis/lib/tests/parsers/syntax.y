%{
#include <math.h>
#include <stdio.h>
#include <stdlib.h>
#define YYSTYPE double


%}

%token NUMBER VAR
%token COMMA PLUS MINUS TIMES DIVIDE POWER
%token BIGGERTHAN SMALLERTHAN EQUAL LEFT RIGHT
%token END

%left PLUS MINUS
%left TIMES DIVIDE
%left NEG
%right POWER

%start Input
%%

Input:

     | Input Line
;

Line:
     END
     | Expression END { printf("Result: %f\n", $1); }
     | LogicalExpression END {char* result=($1==1) ? "True":"False";printf("Result: %s\n",result);}
;

LogicalExpression:
      BIGGERTHAN LEFT Expression COMMA Expression RIGHT {$$= ($3>$5) ? 1 : 0 ;}
      | SMALLERTHAN LEFT Expression COMMA Expression RIGHT {$$= ($3<$5) ? 1 : 0 ;}
      | EQUAL LEFT Expression COMMA Expression RIGHT {$$= ($3==$5) ? 1 : 0 ;}
      ;


Expression:
  NUMBER { $$=$1; }
| VAR    { $$=$1; }
| Expression PLUS Expression { $$=$1+$3; }
| Expression MINUS Expression { $$=$1-$3; }
| Expression TIMES Expression { $$=$1*$3; }
| Expression DIVIDE Expression { $$=$1/$3; }
| MINUS Expression %prec NEG { $$=-$2; }
| Expression POWER Expression { $$=pow($1,$3); }
| LEFT Expression RIGHT { $$=$2; }
;

%%

int yyerror(char *s) {
  printf("%s\n", s);
}

int main() {
  //char tstr[] = "=(_G654,5)\n\0\0";
  //yy_scan_buffer(tstr, sizeof(tstr));
  yyparse();

}
