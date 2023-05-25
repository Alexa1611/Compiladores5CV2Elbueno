public class Token {
    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;

    public Token(TipoToken tipo, String lexema, Object literal, int linea) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal != null ? literal : "";
        this.linea = linea;
    }

    public String toString() {
        return tipo + " " + lexema + " " + literal + " (línea " + linea + ")";
    }
    
}
