grammar Formula;


fragment LOWER          : [a-z];
fragment UPPER          : [A-Z];
fragment LETTER         : LOWER | UPPER;
fragment DIGIT          : [0-9];
fragment ANYCHAR        : LETTER | DIGIT | '_';

AGENT                   : 'A' | 'B' | 'C' | 'D' | 'E';
LOWER_NAME              : LOWER ANYCHAR*;
UPPER_NAME              : LOWER ANYCHAR*;
OP_AND                  : '&' | '&&';
WS                      : [ \n\t\r]+ -> skip;
OP_OR                   : '|';
OP_ARROW                : '->';
OP_BOX                  : 'K';
OP_DIAMOND              : 'M';
OP_BOXA                 : OP_BOX AGENT;
OP_DIAMONDA             : OP_DIAMOND AGENT;
OP_NEG                  : '!';



formula
    : LOWER_NAME              # atom
    | '(' formula ')'         # parens
    | OP_BOXA formula          # box
    | OP_DIAMONDA formula      # diamond
    | OP_NEG formula          # negation

    
    | formula OP_OR formula   # or
    | formula OP_ARROW formula   # implication
    | formula OP_AND formula  # conjunction


    ;

