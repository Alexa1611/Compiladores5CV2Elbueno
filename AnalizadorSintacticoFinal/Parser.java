import java.util.List;

public class Parser {

    private final List<Token> tokens;

    // Palabras reservadas
    private final Token classK = new Token(TipoToken.CLASS, "class");
    private final Token funK= new Token(TipoToken.FUN, "fun");
    private final Token varK = new Token(TipoToken.VAR, "var");
    private final Token ifK = new Token(TipoToken.IF, "if");
    private final Token elseK = new Token(TipoToken.ELSE, "else");
    private final Token forK = new Token(TipoToken.FOR, "for");
    private final Token printK = new Token(TipoToken.PRINT, "print");
    private final Token returnK = new Token(TipoToken.RETURN, "return");
    private final Token whileK = new Token(TipoToken.WHILE, "while");
    private final Token trueK = new Token(TipoToken.TRUE, "true");
    private final Token falseK = new Token(TipoToken.FALSE, "false");
    private final Token nullK = new Token(TipoToken.NULL, "null");
    private final Token thisK = new Token(TipoToken.THIS, "this");
    private final Token orK = new Token(TipoToken.OR, "or");
    private final Token andK = new Token(TipoToken.AND, "and");
    private final Token superk = new Token(TipoToken.SUPER,"super");
    private final Token numberK = new Token(TipoToken.NUMERO, "");
    private final Token cadenaK = new Token(TipoToken.CADENA, "");
    // Identificadores
    private final Token id = new Token(TipoToken.IDENTIFICADOR, "");

    // Símbolos
    
    private final Token puntoycoma = new Token(TipoToken.PUNTO_Y_COMA, ";");
    private final Token parentesisIzq = new Token(TipoToken.PARENTESIS_IZQ, "(");
    private final Token parentesisDer = new Token(TipoToken.PARENTESIS_DER, ")");
    private final Token llaveIzq = new Token(TipoToken.LLAVE_IZQ, "{");
    private final Token llaveDer = new Token(TipoToken.LLAVE_DER, "}");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token igual = new Token(TipoToken.IGUAL, "=");
    private final Token diferente = new Token(TipoToken.DIFERENTE, "!=");
    private final Token igualIgual = new Token(TipoToken.IGUAL_IGUAL, "==");
    private final Token mayorQue = new Token(TipoToken.MAYOR_QUE, ">");
    private final Token mayorOigual = new Token(TipoToken.MAYOR_IGUAL, ">=");
    private final Token menorQue = new Token(TipoToken.MENOR_QUE, "<");
    private final Token menorOigual = new Token(TipoToken.MENOR_IGUAL, "<=");
    private final Token suma= new Token(TipoToken.SUMA, "+");
    private final Token menos = new Token(TipoToken.RESTA, "-");
    private final Token multiplicacion = new Token(TipoToken.MULTIPLICACION, "*");
    private final Token division = new Token(TipoToken.DIVISION, "/");
    private final Token not = new Token(TipoToken.NOT,"!");
       
    private final Token finCadena = new Token(TipoToken.EOF, "");
   
    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        declaracion();
        

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }
    }
    //DECLARACIONES
void declaracion() {
    if(hayErrores) return;
    if (preanalisis.equals(classK)) {
        class_decl();
        declaracion();
    } else if (preanalisis.equals(funK)) {
        fun_decl();
        declaracion();
    } else if (preanalisis.equals(varK)) {
        var_decl();
        declaracion();
    } else if (preanalisis.equals(ifK) || preanalisis.equals(whileK) || preanalisis.equals(printK) || preanalisis.equals(returnK) || preanalisis.equals(forK)) {
        state_decl();
        declaracion();
    }
}

void class_decl() {
    if(hayErrores) return;
        match(classK);
        match(id);
        class_inher();
        match(llaveIzq);
        function();
        match(llaveDer);
    
}

void class_inher() {
    if (preanalisis.equals(menorQue)) {
        match(menorQue);
        match(id);
    }
}

void fun_decl() {
    if(hayErrores) return;
    match(funK);
    function();
}

void var_decl() {
    match(varK);
    match(id);
    var_init();
    match(puntoycoma);
}

void var_init() {
    if(hayErrores) return;
    if (preanalisis.equals(igual)) {
        match(igual);
        expression();
    }
}

void state_decl() {
    if(hayErrores) return;
    if (preanalisis.equals(ifK)) {
        if_stmt();
    } else if (preanalisis.equals(forK)) {
        for_stmt();
    } else if (preanalisis.equals(whileK)) {
        while_stmt();
    } else if (preanalisis.equals(printK)) {
        print_stmt();
    } else if (preanalisis.equals(returnK)) {
        return_stmt();
    } else if (preanalisis.equals(llaveIzq)) {
        block();
    } else {
        expr_stmt();
    }
}

void expr_stmt() {
    if(hayErrores) return;
    expression();
    match(puntoycoma);
    
}

void for_stmt() {
    if(hayErrores) return;
    match(forK);
    match(parentesisIzq);
    for_stms1();
    for_stms2();
    for_stms3();
    match(parentesisDer);
    state_decl();
}

void for_stms1() {
    if(hayErrores) return;

    if(preanalisis.equals(varK))
    {
        var_decl();
    }
    else if(preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) || preanalisis.equals(falseK) || 
    preanalisis.equals(nullK) || preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || 
    preanalisis.equals(id) || preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        expr_stmt();
    }
    else if(preanalisis.equals(puntoycoma))
    {
        match(puntoycoma);
    }
    else
    {
        hayErrores = true;
        System.out.println("Error en la posición " + preanalisis.posicion);
    }
}

void for_stms2() {
     if(hayErrores) return;

    if(preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) || preanalisis.equals(falseK) || 
    preanalisis.equals(nullK) || preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || 
    preanalisis.equals(id) || preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        expression();
        match(puntoycoma);
    }
    else if(preanalisis.equals(puntoycoma))
    {
        match(puntoycoma);
    }
    else
    {
        hayErrores = true;
        System.out.println("Error en la posición " + preanalisis.posicion);
    }
}

void for_stms3() {
     if(hayErrores) return;

    if(preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) || preanalisis.equals(falseK) || 
    preanalisis.equals(nullK) || preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || 
    preanalisis.equals(id) || preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        expression();
    }
}

void if_stmt() {
    if(hayErrores) return;
    match(ifK);
    match(parentesisIzq);
    expression();
    match(parentesisDer);
    state_decl();
    else_state();
}

void else_state() {
    if(hayErrores) return;
    if (preanalisis.equals(elseK)) {
        match(elseK);
        state_decl();
    }
}

void print_stmt() {
    if(hayErrores) return;
    match(printK);
    expression();
    match(puntoycoma);
}

void return_stmt() {
    if(hayErrores) return;
    match(returnK);
    return_exp_opc();
    match(puntoycoma);
}

void return_exp_opc() {
    if(hayErrores) return;

    if(preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) || preanalisis.equals(falseK) || 
    preanalisis.equals(nullK) || preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || 
    preanalisis.equals(id) || preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        expression();
    }
}

void while_stmt() {
    match(whileK);
    match(parentesisIzq);
    expression();
    match(parentesisDer);
    state_decl();
}

void block() {
    if(hayErrores) return;
    match(llaveIzq);
    block_decl();
    match(llaveDer);
}

void block_decl() {
    if(hayErrores) return;
   if(preanalisis.equals(classK) || preanalisis.equals(funK) || preanalisis.equals(varK) || preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) ||preanalisis.equals(falseK) || preanalisis.equals(nullK) ||
    preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || preanalisis.equals(id) ||
    preanalisis.equals(parentesisIzq) || preanalisis.equals(superk) || preanalisis.equals(forK) || preanalisis.equals(ifK) || preanalisis.equals(printK) ||
    preanalisis.equals(returnK) || preanalisis.equals(whileK) || preanalisis.equals(llaveIzq))
    {
        declaracion(); 
        block_decl();
    }
}

void expression() {
    if(hayErrores) return;
    assignment();
}

void assignment() {
    if(hayErrores) return;
    logic_or();
    assignment_opc();
}

void assignment_opc() {
    if(hayErrores) return;
    if (preanalisis.equals(igual)) {
        match(igual);
        expression();
    }
}

void logic_or() {
    if(hayErrores) return;
    logic_and();
    logic_or_2();
}

void logic_or_2() {
    if(hayErrores) return;
    if (preanalisis.equals(orK)) {
        match(orK);
        logic_and();
        logic_or_2();
    }
}

void logic_and() {
    if(hayErrores) return;
    equality();
    logic_and_2();
}

void logic_and_2() {
    if(hayErrores) return;
    if (preanalisis.equals(andK)) {
        match(andK);
        equality();
        logic_and_2();
    }
}

void equality() {
    if(hayErrores) return;
    comparison();
    equality_2();
}

void equality_2() {
    if(hayErrores) return;
    if (preanalisis.equals(igualIgual)) {
        match(igualIgual);
        comparison();
        equality_2();
    } else if (preanalisis.equals(diferente)) {
        match(diferente);
        comparison();
        equality_2();
    }
}

void comparison() {
    if(hayErrores) return;
    term();
    comparison_2();
}

void comparison_2() {
    if(hayErrores) return;
    if (preanalisis.equals(mayorQue)) {
        match(mayorQue);
        term();
        comparison_2();
    } else if (preanalisis.equals(mayorOigual)) {
        match(mayorOigual);
        term();
        comparison_2();
    } else if (preanalisis.equals(menorQue)) {
        match(menorQue);
        term();
        comparison_2();
    } else if (preanalisis.equals(menorOigual)) {
        match(menorOigual);
        term();
        comparison_2();
    }
}

void term() {
    if(hayErrores) return;
    factor();
    term_2();
}

void term_2() {
    if(hayErrores) return;
    if (preanalisis.equals(menos)) {
        match(menos);
        factor();
        term_2();
    } else if (preanalisis.equals(suma)) {
        match(suma);
        factor();
        term_2();
    }
}

void factor() {
    if(hayErrores) return;
    unary();
    factor_2();
}

void factor_2() {
    if(hayErrores) return;
    if (preanalisis.equals(division)) {
        match(division);
        unary();
        factor_2();
    } else if (preanalisis.equals(multiplicacion)) {
        match(multiplicacion);
        unary();
        factor_2();
    }
}

void unary() {
    if (preanalisis.equals(not)) {
        match(not);
        unary();
    } else if (preanalisis.equals(menos)) {
        match(menos);
        unary();
    } else if(preanalisis.equals(trueK) || preanalisis.equals(falseK) || preanalisis.equals(nullK) ||
    preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || preanalisis.equals(id) ||
    preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
     {
        call();
    }
     else
    {
        hayErrores = true;
        System.out.println("Error en la posición " + preanalisis.posicion);
    }
}

void call() {
    if(hayErrores) return;
    primary();
    call_2();
}

void call_2() {
    if(hayErrores) return;
    if (preanalisis.equals(parentesisIzq)) {
        match(parentesisIzq);
        argumentsOpc();
        match(parentesisDer);
        call_2();
    } else if (preanalisis.equals(punto)) {
        match(punto);
        match(id);
        call_2();
    }
}
void call_opc()
{
    if(hayErrores) return;

    if(preanalisis.equals(trueK) || preanalisis.equals(falseK) || preanalisis.equals(nullK) ||
    preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || preanalisis.equals(id) ||
    preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        call();
        match(punto);
    }
}

void primary() {
        if(hayErrores) return;
        if(preanalisis.equals(trueK)){
            match(trueK);
        }else if(preanalisis.equals(falseK)){
            match(falseK);
        }else if(preanalisis.equals(nullK)){
            match(nullK);
        }else if(preanalisis.equals(thisK)){
            match(thisK);
        }else if (preanalisis.equals(numberK)) {
            match(numberK);
        }
        else if (preanalisis.equals(cadenaK)) {
            match(cadenaK);
            
        }
        else if(preanalisis.equals(id)){
            match(id);
        }else if(preanalisis.equals(parentesisIzq)){
            match(parentesisIzq);
            expression();
            match(parentesisDer);
        }else if(preanalisis.equals(superk)){
            match(superk);
            match(punto);
            match(id);
        }else
    {
        hayErrores = true;
        System.out.println("Error en la posición " + preanalisis.posicion);
    }
    }

void function() {
     if(hayErrores) return;
    match(id);
    match(parentesisIzq);
    parameter_opc();
    match(parentesisDer);
    block();
}

void functions(){
    if(hayErrores) return;
    if(preanalisis.equals(id)){
        function();
        functions();
    }
}

void parameter_opc() {
     if(hayErrores) return;
    if (preanalisis.equals(id)) {
        parameter();
        
    }
}

void parameter() {
     if(hayErrores) return;
    match(id);
    parameter_2();
}

void parameter_2() {
    if(hayErrores)return;
    if (preanalisis.equals(coma)) {
        match(coma);
        match(id);
        parameter_2();
    }
}

void argumentsOpc() {
     if(preanalisis.equals(diferente) || preanalisis.equals(menos) || preanalisis.equals(trueK) || preanalisis.equals(falseK) || preanalisis.equals(nullK) ||
    preanalisis.equals(thisK) || preanalisis.equals(numberK) || preanalisis.equals(cadenaK) || preanalisis.equals(id) ||
    preanalisis.equals(parentesisIzq) || preanalisis.equals(superk))
    {
        arguments();
    }
}

void arguments() {
    
    if(hayErrores) return;
    expression();
    arguments2();
}

void arguments2() {
    
    if(hayErrores) return;
    if (preanalisis.equals(coma)) {
        match(coma);
        expression();
        arguments2();
    }
}

    void match(Token t){
        
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un " + t.tipo);
        }
    }

    

} 
