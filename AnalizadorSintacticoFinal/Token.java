
public class Token {

    final TipoToken tipo;
    final String lexema;
    final int posicion;
    Object literal;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = 0;
        this.literal = null;

    }

    public Token(TipoToken tipo, String lexema, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = posicion;
        this.literal = null;

    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = 0;
        this.literal = literal;

    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if (this.tipo == ((Token) o).tipo) {
            return true;
        }

        return false;
    }

    public String toString() {
        return tipo + " " + lexema + " " + (literal == null ? " " : literal.toString());
    }

    // Métodos auxiliares
    public boolean esOperando() {
        switch (this.tipo) {
            case IDENTIFICADOR:
            case NUMERO:
            case CADENA:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador() {
        switch (this.tipo) {
           case MULTIPLICACION:
            case DIVISION:

            case SUMA:
            case RESTA:

            case MAYOR_QUE:
            case MAYOR_IGUAL:
            case MENOR_QUE:
            case MENOR_IGUAL:

            case IGUAL_IGUAL:
            case DIFERENTE:

            case AND:

            case OR:

            case IGUAL:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada() {
        switch (this.tipo) {
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

    public boolean esEstructuraDeControl() {
        switch (this.tipo) {
            case IF:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenciaMayorIgual(Token t) {
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }

    private int obtenerPrecedencia() {
        switch (this.tipo) {
            case MULTIPLICACION:
            case DIVISION:
                return 7;
            case SUMA:
            case RESTA:
                return 6;

            case MAYOR_QUE:
            case MAYOR_IGUAL:
            case MENOR_QUE:
            case MENOR_IGUAL:
                return 5;
            case IGUAL_IGUAL:
            case DIFERENTE:
                return 4;
            case AND:
                return 3;
            case OR:
                return 2;
            case IGUAL:
                return 1;
        }

        return 0;
    }

    public int aridad() {
        switch (this.tipo) {
            case MULTIPLICACION:
            case DIVISION:

            case SUMA:
            case RESTA:

            case MAYOR_QUE:
            case MAYOR_IGUAL:
            case MENOR_QUE:
            case MENOR_IGUAL:

            case IGUAL_IGUAL:
            case DIFERENTE:

            case AND:

            case OR:

            case IGUAL:

                return 2;
        }
        return 0;
    }

    public boolean esIdentificador() {
        return tipo == TipoToken.IDENTIFICADOR;
    }

}
