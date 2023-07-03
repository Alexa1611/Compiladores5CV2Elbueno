package AnalizadorSintacticoFinal;



public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;

    //final int posicion;

    /*public Token(TipoToken tipo, String lexema,int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.posicion = posicion;
    }*/

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }

    public String toString(){
        return tipo + " " + lexema + " " + (literal == null ? " " : literal.toString());
    }

    // Métodos auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case IDENTIFICADOR:
            case NUMERO:
            case CADENA:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador(){
        switch (this.tipo){
            case SUMA:
            case RESTA:
            case MULTIPLICACION:
            case DIVISION:
            case IGUAL_IGUAL:
            case MAYOR_QUE:
            case MAYOR_IGUAL:
                //
            case MENOR_IGUAL:
            case MENOR_QUE:
            case DIFERENTE:
            case IGUAL:
            case AND:
            case OR:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada()
    {
        switch (this.tipo)
        {
            case VAR:
            case IF:
            case PRINT:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean esEstructuraDeControl()
    {
        switch (this.tipo)
        {
            case IF:
            case FOR:
            case WHILE:
            case ELSE:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenciaMayorIgual(Token t)
    {
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }

    private int obtenerPrecedencia()
    {
        switch (this.tipo)
        {
            case MULTIPLICACION:
            case DIVISION:
                return 7;
            case SUMA:
            case RESTA:
                return 6;
            case IGUAL:
                return 1;
            case MAYOR_IGUAL:
            case MAYOR_QUE:
            case MENOR_QUE:
            case MENOR_IGUAL:
                return 5;
            case DIFERENTE:
            case IGUAL_IGUAL:
                return 4;
            case AND:
                return 3;
            case OR:
                return 2;
                default:
                break;
        }

        return 0;
    }

    public int aridad()
    {
        switch (this.tipo)
        {
            case MULTIPLICACION:
            case DIVISION:
            case SUMA:
            case RESTA:
            case IGUAL_IGUAL:
            case IGUAL:
            case MAYOR_QUE:
            case MAYOR_IGUAL:
            case MENOR_IGUAL:
            case MENOR_QUE:
            case AND:
            case OR:
                return 2;
                default:
                break;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Token))
        {
            return false;
        }

        if(this.tipo == ((Token)o).tipo)
        {
            return true;
        }

        return false;
    }
}