
package AnalizadorSintacticoFinal;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    
    private final Token y = new Token(TipoToken.Y, "y");
    private final Token clase = new Token(TipoToken.CLASE, "clase");
    private final Token ademas = new Token(TipoToken.ADEMAS, "ademas");
    private final Token para = new Token(TipoToken.PARA, "para");
    private final Token funcion = new Token(TipoToken.FUNCION, "funcion");
    private final Token si = new Token(TipoToken.SI, "si");
    private final Token nulo = new Token(TipoToken.NULO, "nulo");
    private final Token imprimir = new Token(TipoToken.IMPRIMIR, "imprimir");
    private final Token devolver = new Token(TipoToken.DEVOLVER, "devolver");
    private final Token supr = new Token(TipoToken.SUPER, "super");
    private final Token este = new Token(TipoToken.ESTE, "este");
    private final Token verdadero = new Token(TipoToken.VERDADERO, "verdadero");
    private final Token falso = new Token(TipoToken.FALSO, "falso");
    private final Token mientras = new Token(TipoToken.MIENTRAS, "mientras");
    private final Token variable = new Token(TipoToken.VARIABLE, "variable");
    private final Token o = new Token(TipoToken.O, "o");
    private final Token no = new Token(TipoToken.NO, "!");
    private final Token igual = new Token(TipoToken.IGUAL, "==");
    private final Token diferente_de = new Token(TipoToken.DIFERENTE, "!=");
    private final Token asignar = new Token(TipoToken.ASIGNAR, "=");
    private final Token menor = new Token(TipoToken.MENOR_QUE, "<");
    private final Token menor_igual = new Token(TipoToken.MENOR_IGUAL, "<=");
    private final Token mayor = new Token(TipoToken.MAYOR_QUE, ">");
    private final Token mayor_igual = new Token(TipoToken.MAYOR_IGUAL, ">=");
    private final Token numero = new Token(TipoToken.NUMERO, "numero");
    private final Token cadena = new Token(TipoToken.CADENA, "cadena");
    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "identificador");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token suma = new Token(TipoToken.SUMA, "+");
    private final Token resta = new Token(TipoToken.RESTA, "-");
    private final Token multiplicacion = new Token(TipoToken.MULTIPLICACION, "*");
    private final Token division = new Token(TipoToken.DIVISION, "/");
    private final Token parentesis_izq = new Token(TipoToken.PARENTESIS_IZQ, "(");
    private final Token parentesis_der = new Token(TipoToken.PARENTESIS_DER, ")");
    private final Token llave_izq = new Token(TipoToken.LLAVE_IZQ, "{");
    private final Token llave_der = new Token(TipoToken.LLAVE_DER, "}");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token punto_y_coma = new Token(TipoToken.PUNTO_Y_COMA, ";");
    private final Token finCadena = new Token(TipoToken.EOF, "");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public boolean parse(){

        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();
        if(!hayErrores && !preanalisis.equals(finCadena)){
            return false;
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            //System.out.println("Consulta válida");
            return true;
        }

        /*if(!preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }else if(!hayErrores){
            System.out.println("Consulta válida");
        }*/
        return !hayErrores;
    }

    void PROGRAM(){
        DECLARATION();
       }

    void DECLARATION()
    {
        if(hayErrores) return;

        if(preanalisis.equals(clase))
        {
            CLASS_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(funcion))
        {
            FUN_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(variable))
        {
            VAR_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) ||preanalisis.equals(este) ||preanalisis.equals(numero) ||preanalisis.equals(cadena) ||preanalisis.equals(identificador) ||
                preanalisis.equals(parentesis_izq) ||preanalisis.equals(supr) ||preanalisis.equals(para) || preanalisis.equals(si) ||
                preanalisis.equals(imprimir) || preanalisis.equals(devolver) || preanalisis.equals(mientras) || preanalisis.equals(llave_izq))
        {
            STATEMENT();
            DECLARATION();
        }
    }

    void CLASS_DECL()
    {
        if(hayErrores) return;

        match(clase);
        match(identificador);
        CLASS_INHER();
        match(llave_izq);
        FUNCTIONS();
        match(llave_der);
    }

    void CLASS_INHER()
    {
        if(hayErrores) return;

        if(preanalisis.equals(menor))
        {
            match(menor);
            match(identificador);
        }
    }

    void FUN_DECL()
    {
        if(hayErrores) return;

        match(funcion);
        FUNCTION();
    }

    void VAR_DECL()
    {
        if(hayErrores) return;

        match(variable);
        match(identificador);
        VAR_INIT();
        match(punto_y_coma);
    }

    void VAR_INIT()
    {
        if(hayErrores) return;

        if(preanalisis.equals(asignar))
        {
            match(asignar);
            EXPRESSION();
        }
    }

    void STATEMENT()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) ||
                preanalisis.equals(identificador) || preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            EXPR_STMT();
        }
        else if(preanalisis.equals(para))
        {
            FOR_STMT();
        }
        else if(preanalisis.equals(si))
        {
            IF_STMT();
        }
        else if(preanalisis.equals(imprimir))
        {
            PRINT_STMT();
        }
        else if(preanalisis.equals(devolver))
        {
            RETURN_STMT();
        }
        else if(preanalisis.equals(mientras))
        {
            WHILE_STMT();
        }
        else if(preanalisis.equals(llave_izq))
        {
            BLOCK();
        }
        else
        {
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion);
            System.out.println("Error sintactico");
            System.exit(1);
        }
    }

    void EXPR_STMT()
    {
        if(hayErrores) return;

        EXPRESSION();
        match(punto_y_coma);
    }

    void FOR_STMT()
    {
        if(hayErrores) return;

        match(para);
        match(parentesis_izq);
        FOR_STMT_1();
        FOR_STMT_2();
        FOR_STMT_3();
        match(parentesis_der);
        STATEMENT();
    }

    void FOR_STMT_1()
    {
        if(hayErrores) return;

        if(preanalisis.equals(variable))
        {
            VAR_DECL();
        }
        else if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) ||
                preanalisis.equals(identificador) || preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            EXPR_STMT();
        }
        else if(preanalisis.equals(punto_y_coma))
        {
            match(punto_y_coma);
        }
        else
        {
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion);
            System.out.println("Error sintactico");
            System.exit(1);
        }
    }

    void FOR_STMT_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) ||
                preanalisis.equals(identificador) || preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            EXPRESSION();
            match(punto_y_coma);
        }
        else if(preanalisis.equals(punto_y_coma))
        {
            match(punto_y_coma);
        }
        else
        {
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion);
            System.out.println("Error sintactico");
            System.exit(1);
        }
    }

    void FOR_STMT_3()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) ||
                preanalisis.equals(identificador) || preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            EXPRESSION();
        }
    }

    void IF_STMT()
    {
        if(hayErrores) return;

        match(si);
        match(parentesis_izq);
        EXPRESSION();
        match(parentesis_der);
        STATEMENT();
        ELSE_STATEMENT();
    }

    void ELSE_STATEMENT()
    {
        if(hayErrores) return;

        if(preanalisis.equals(ademas))
        {
            match(ademas);
            STATEMENT();
        }
    }

    void PRINT_STMT()
    {
        if(hayErrores) return;

        match(imprimir);
        EXPRESSION();
        match(punto_y_coma);
    }

    void RETURN_STMT()
    {
        if(hayErrores) return;

        match(devolver);
        RETURN_EXP_OPC();
        match(punto_y_coma);
    }

    void RETURN_EXP_OPC()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) ||
                preanalisis.equals(nulo) || preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) ||
                preanalisis.equals(identificador) || preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            EXPRESSION();
        }
    }

    void WHILE_STMT()
    {
        if(hayErrores) return;

        match(mientras);
        match(parentesis_izq);
        EXPRESSION();
        match(parentesis_der);
        STATEMENT();
    }

    void BLOCK()
    {
        if(hayErrores) return;

        match(llave_izq);
        BLOCK_DECL();
        match(llave_der);
    }

    void BLOCK_DECL()
    {
        if(hayErrores) return;

        if(preanalisis.equals(clase) || preanalisis.equals(funcion) || preanalisis.equals(variable) || preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) ||preanalisis.equals(falso) || preanalisis.equals(nulo) ||
                preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) ||
                preanalisis.equals(parentesis_izq) || preanalisis.equals(supr) || preanalisis.equals(para) || preanalisis.equals(si) || preanalisis.equals(imprimir) ||
                preanalisis.equals(devolver) || preanalisis.equals(mientras) || preanalisis.equals(llave_izq))
        {
            DECLARATION();
            BLOCK_DECL();
        }
    }

    void EXPRESSION()
    {
        if(hayErrores) return;

        ASSIGNMENT();
    }

    void ASSIGNMENT()
    {
        if(hayErrores) return;

        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    void ASSIGNMENT_OPC()
    {
        if(hayErrores) return;

        if(preanalisis.equals(asignar))
        {
            match(asignar);
            EXPRESSION();
        }
    }

    void LOGIC_OR()
    {
        if(hayErrores) return;

        LOGIC_AND();
        LOGIC_OR_2();
    }

    void LOGIC_OR_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(o))
        {
            match(o);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND()
    {
        if(hayErrores) return;

        EQUALITY();
        LOGIC_AND_2();
    }

    void LOGIC_AND_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(y))
        {
            match(y);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    void EQUALITY()
    {
        if(hayErrores) return;

        COMPARISON();
        EQUALITY_2();
    }

    void EQUALITY_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de))
        {
            match(diferente_de);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.equals(igual))
        {
            match(igual);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON()
    {
        if(hayErrores) return;

        TERM();
        COMPARISON_2();
    }

    void COMPARISON_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(mayor))
        {
            match(mayor);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(mayor_igual))
        {
            match(mayor_igual);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(menor))
        {
            match(menor);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(menor_igual))
        {
            match(menor_igual);
            TERM();
            COMPARISON_2();
        }
    }

    void TERM()
    {
        if(hayErrores) return;

        FACTOR();
        TERM_2();
    }

    void TERM_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(menor))
        {
            match(menor);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.equals(suma))
        {
            match(suma);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR()
    {
        if(hayErrores) return;

        UNARY();
        FACTOR_2();
    }

    void FACTOR_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(division))
        {
            match(division);
            UNARY();
            FACTOR_2();
        }

        else if(preanalisis.equals(multiplicacion))
        {
            match(multiplicacion);
            UNARY();
            FACTOR_2();
        }
    }

    void UNARY()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de))
        {
            match(diferente_de);
            UNARY();
        }

        else if(preanalisis.equals(menor))
        {
            match(menor);
            UNARY();
        }

        else if(preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) ||
                preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) ||
                preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            CALL();
        }
        else
        {
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion);
            System.out.println("Error sintactico");
            System.exit(1);
        }
    }

    void CALL()
    {
        if(hayErrores) return;

        PRIMARY();
        CALL_2();
    }

    void CALL_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(parentesis_izq))
        {
            match(parentesis_izq);
            ARGUMENTS_OPC();
            match(parentesis_der);
            CALL_2();
        }
        else if(preanalisis.equals(punto))
        {
            match(punto);
            match(identificador);
            CALL_2();
        }
    }

    void CALL_OPC()
    {
        if(hayErrores) return;

        if(preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) ||
                preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) ||
                preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            CALL();
            match(punto);
        }
    }

    void PRIMARY()
    {
        if(hayErrores) return;

        if(preanalisis.equals(verdadero))
        {
            match(verdadero);
        }
        else if(preanalisis.equals(falso))
        {
            match(falso);
        }
        else if(preanalisis.equals(nulo))
        {
            match(nulo);
        }
        else if(preanalisis.equals(este))
        {
            match(este);
        }
        else if(preanalisis.equals(numero))
        {
            match(numero);
        }
        else if(preanalisis.equals(cadena))
        {
            match(cadena);
        }
        else if(preanalisis.equals(identificador))
        {
            match(identificador);
        }
        else if(preanalisis.equals(parentesis_izq))
        {
            match(parentesis_izq);
            EXPRESSION();
            match(parentesis_der);
        }
        else if(preanalisis.equals(supr))
        {
            match(supr);
            match(punto);
            match(identificador);
        }
        else
        {
            hayErrores = true;
            //System.out.println("Error en la posición " + preanalisis.posicion);
            System.out.println("Error sintactico");
            System.exit(1);
        }
    }

    void FUNCTION()
    {
        if(hayErrores) return;

        match(identificador);
        match(parentesis_izq);
        PARAMETERS_OPC();
        match(parentesis_der);
        BLOCK();
    }

    void FUNCTIONS()
    {
        if (hayErrores) return;

        if(preanalisis.equals(identificador))
        {
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC()
    {
        if(hayErrores) return;

        if(preanalisis.equals(identificador))
        {
            PARAMETERS();
        }
    }

    void PARAMETERS()
    {
        if(hayErrores) return;

        match(identificador);
        PARAMETERS_2();
    }

    void PARAMETERS_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(coma))
        {
            match(coma);
            match(identificador);
            PARAMETERS_2();
        }
    }

    void ARGUMENTS_OPC()
    {
        if(hayErrores) return;

        if(preanalisis.equals(diferente_de) || preanalisis.equals(menor) || preanalisis.equals(verdadero) || preanalisis.equals(falso) || preanalisis.equals(nulo) ||
                preanalisis.equals(este) || preanalisis.equals(numero) || preanalisis.equals(cadena) || preanalisis.equals(identificador) ||
                preanalisis.equals(parentesis_izq) || preanalisis.equals(supr))
        {
            ARGUMENTS();
        }
    }

    void ARGUMENTS()
    {
        if(hayErrores) return;

        EXPRESSION();
        ARGUMENTS_2();
    }

    void ARGUMENTS_2()
    {
        if(hayErrores) return;

        if(preanalisis.equals(coma))
        {
            match(coma);
            EXPRESSION();
            ARGUMENTS_2();
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

        }
    }

}
