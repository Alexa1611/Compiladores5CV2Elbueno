
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
    private final Token corcheteIzq = new Token(TipoToken.CORCHETE_IZQ, "{");
    private final Token corcheteDer = new Token(TipoToken.CORCHETE_DER, "}");
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
void declaracion(){
    
    if (preanalisis.equals(classK)){
        class_decl();
        declaracion();
    }else if(preanalisis.equals(funK)){
        fun_decl();
        declaracion();
    }else if(preanalisis.equals(varK)){
        var_decl();
        declaracion();
    
    }else if(preanalisis.equals(ifK) || preanalisis.equals(whileK) || preanalisis.equals(printK) || preanalisis.equals(returnK)){
        state_decl();
        declaracion();
    }
        

    }
    void class_decl(){
        
        if(preanalisis.equals(classK)){
            match(classK);
            match(id);
           class_inher();
            match(corcheteIzq);
            function();
            match(corcheteDer);
        }

    }
    void class_inher(){
        
           if(preanalisis.equals(menorQue)){
                match(menorQue);
                match(id);
            }
    }

    void fun_decl(){
        
        if(preanalisis.equals(funK)){
            match(funK);
            function();
        }
    }

    void var_decl(){
        
        if(preanalisis.equals(varK)){
            match(varK);
            match(id);
            var_init();
            match(puntoycoma);
        }
    }
void var_init(){
    
    if(preanalisis.equals(igual)){
        match(igual);
        expression();
        }

}
//SENTENCIAS
    void state_decl(){
    
        if(preanalisis.equals(ifK)){
           if_stmt();
        } else if (preanalisis.equals(forK)) {
            for_stmt();
        } else if (preanalisis.equals(whileK)) {
           while_stmt();
        } else if (preanalisis.equals(printK)){ 
            match(printK);
            print_stmt();
        } else if (preanalisis.equals(returnK)) {
           return_stmt();
        } else if (preanalisis.equals(corcheteIzq)) {
            block();
        } else {
            
            expr_stmt();
        }

    }

    
    void expr_stmt(){
    expression();
       if(preanalisis.equals(puntoycoma)){ 
            match(puntoycoma);}
        
    }
    void for_stmt(){
        
        if(preanalisis.equals(forK)){
            match(forK);
            match(parentesisIzq);
            for_stms1();
            for_stms2();
            for_stms3();
            match(parentesisDer);
            state_decl();
    }
}
    void for_stms1(){
        if(preanalisis.equals(parentesisIzq))
        match(parentesisIzq);
        var_decl();
        expr_stmt();
        match(puntoycoma);

    }

    void for_stms2(){
        
        if(preanalisis.equals(puntoycoma)){
            expression();
            match(puntoycoma);
        }else {
            match(puntoycoma);
        }
    }
    void for_stms3(){
        
        if (preanalisis.equals(puntoycoma)) {
                expression();
        }

    }
    void if_stmt(){
        
        if(preanalisis.equals(ifK)){
            match(ifK);
            match(parentesisIzq);
            expression();
            match(parentesisDer);
            state_decl();
            else_state();
    }
    }



    void else_state(){
        
        if(preanalisis.equals(elseK)){
            match(elseK);
            state_decl();
        }
    }

    void print_stmt(){
        
        if(preanalisis.equals(printK)){
            match(printK);
            expression();
            match(puntoycoma);

        }

    }

    void return_stmt(){
        
        if(preanalisis.equals(returnK)){
            match(returnK);
            return_exp_opc();
            match(puntoycoma);
        }
    }
        

    void return_exp_opc (){
        if(preanalisis.equals(returnK)){
         expression();
        }

    }

    void while_stmt(){
        if(preanalisis.equals(whileK)){
            match(whileK);
            match(parentesisIzq);
            expression();
            match(parentesisDer);
            state_decl();
        }
    }



    void block(){
        
        if (preanalisis.equals(corcheteIzq)) {
            match(corcheteIzq);
            block_decl();
            match(corcheteDer);
        }

    }

    void block_decl(){
     if(preanalisis.equals(corcheteIzq))   
    {
        declaracion();
        block_decl();
       
    }
}
    //EXPRESIONES
    void expression(){
if(preanalisis.equals(parentesisIzq) || preanalisis.equals(igual) || preanalisis.equals(puntoycoma) || preanalisis.equals(printK)){
        assignment();

}

    }
    void assignment(){
        
        logic_or();
        assigment_opc();
    }

    void assigment_opc(){

        if(preanalisis.equals(igual)){
            match(igual);
            expression();
        }
    }

    void logic_or(){
        
        logic_and();
        logic_or_2();
    }

    void logic_or_2(){
        
        if(preanalisis.equals(orK)){
            match(orK);
            logic_and();
            logic_or_2();
        }
    }

    void logic_and(){
        if(preanalisis.equals(orK)){
        equality();
        logic_and_2();
    }
}

    void logic_and_2(){
        
        if(preanalisis.equals(andK)){
            match(andK);
            equality();
            logic_and_2();
        }
    }

    void equality(){
        if(preanalisis.equals(andK)){
        comparison();
        equality_2();
    }
}

    void equality_2(){
        
        if(preanalisis.equals(igualIgual)){
            match(igualIgual);
            comparison();
            equality_2();
        }else if(preanalisis.equals(diferente)){
            match(diferente);
            comparison();
            equality_2();
        }
   }

   void comparison(){
    if(preanalisis.equals(igualIgual) || preanalisis.equals(diferente)){
        term();
        comparison_2();
   }
}

   void comparison_2(){
    
        if(preanalisis.equals(mayorQue)){
            match(mayorQue);
            term();
            comparison_2();
        }else if(preanalisis.equals(mayorOigual)){
            match(mayorOigual);
            term();
            comparison_2();
        }else if(preanalisis.equals(menorQue)){
            match(menorQue);
            term();
            comparison_2();
        }else if(preanalisis.equals(menorOigual)){
            match(menorOigual);
            term();
            comparison_2();
        }
   }

   void term(){
    if(preanalisis.equals(menorOigual) || preanalisis.equals(menorQue)||preanalisis.equals(mayorOigual)||preanalisis.equals(mayorQue)){
    factor();
    term_2();
   }
}

   void term_2(){
    
    if(preanalisis.equals(menos)){
        match(menos);
        factor();
        term_2();
    }else if(preanalisis.equals(suma)){
        match(suma);
        factor();
        term_2();
    }
   }
    void factor(){
        if(preanalisis.equals(suma) || preanalisis.equals(menos)){
        unary();
        factor_2();
    }
}
    void factor_2(){
    if(preanalisis.equals(division)){
        match(division);
        unary();
        factor_2();
    } else if(preanalisis.equals(multiplicacion)){
        match(multiplicacion);
        unary();
        factor_2();
    }
}

    void unary(){
    if(preanalisis.equals(not)){
        match(not);
        unary();
    } else if(preanalisis.equals(menos)){
        match(menos);
        unary();
    } else{
        
        call();
    }
}


    void call(){
        
        primary();
        call_2();
    }

    void call_2(){
        if(preanalisis.equals(parentesisIzq)){
            match(parentesisIzq);
            argumentsOpc();
            match(parentesisDer);
            call_2();
        }else if(preanalisis.equals(punto)){
            match(punto);
            match(id);
            call_2();
        }
    }
void call_opc(){
call();
match(punto);

}

    void primary() {
        
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
        }
    }


    //OTRAS
    void function(){
        if(preanalisis.equals(id)){
            match(id);
            match(parentesisIzq);
            parameter_opc();
            match(parentesisDer);
            block();
            
        }
    }

    void functions(){
        
        
        function();
        functions();
        

    }

    void parameter_opc(){
        if(preanalisis.equals(parentesisIzq)){
       
                parameter();
        }

    }

    void parameter(){
        
        if(preanalisis.equals(id)){
            match(id);
            parameter_2();
        }
    }

    void parameter_2(){
        
        if(preanalisis.equals(coma)){
            match(coma);
            match(id);
            parameter_2();
    }
}



   
    void argumentsOpc(){
        if(preanalisis.equals(parentesisIzq)){
        
        arguments();
        }
    }

    void arguments(){
        expression();
        arguments2();
    }

    void arguments2(){
        if(preanalisis.equals(coma)){
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