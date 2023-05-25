import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int linea = 1;
    private int indice = 0; // Variable indice agregada

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("Y", TipoToken.Y);
        palabrasReservadas.put("CLASS", TipoToken.CLASS);
        palabrasReservadas.put("IF", TipoToken.IF);
        palabrasReservadas.put("ELSE", TipoToken.ELSE);
        palabrasReservadas.put("WHILE", TipoToken.WHILE);
        palabrasReservadas.put("clase", TipoToken.FOR);
        palabrasReservadas.put("clase", TipoToken.DO);
        palabrasReservadas.put("clase", TipoToken.SWITCH);
        palabrasReservadas.put("clase", TipoToken.CASE);
        palabrasReservadas.put("clase", TipoToken.DEFAULT);
        palabrasReservadas.put("clase", TipoToken.BREAK);
        palabrasReservadas.put("clase", TipoToken.CONTINUE);
        palabrasReservadas.put("clase", TipoToken.RETURN);
        palabrasReservadas.put("clase", TipoToken.TRY);
        palabrasReservadas.put("clase", TipoToken.CATCH);
        palabrasReservadas.put("clase", TipoToken.THROW);
        palabrasReservadas.put("clase", TipoToken.FINALLY);
        palabrasReservadas.put("clase", TipoToken.PUBLIC);
        palabrasReservadas.put("clase", TipoToken.PRIVATE);
        palabrasReservadas.put("clase", TipoToken.PROTECTED);
        palabrasReservadas.put("clase", TipoToken.INTREFACE);
        palabrasReservadas.put("clase", TipoToken.ENUM);
        palabrasReservadas.put("clase", TipoToken.EXTENDS);
        palabrasReservadas.put("clase", TipoToken.IMPLEMENTS);
        palabrasReservadas.put("clase", TipoToken.PACKAGE);
        palabrasReservadas.put("clase", TipoToken.IMPORT);
        palabrasReservadas.put("clase", TipoToken.STATIC);
        palabrasReservadas.put("clase", TipoToken.FINAL );
        palabrasReservadas.put("clase", TipoToken.ABSTRACT);
        palabrasReservadas.put("clase", TipoToken.SYNCHRONIZED);
        palabrasReservadas.put("clase", TipoToken.NULL);
        palabrasReservadas.put("clase", TipoToken.FALSE);
        palabrasReservadas.put("clase", TipoToken.TRUE);
        palabrasReservadas.put("clase", TipoToken.NEW);
        palabrasReservadas.put("clase", TipoToken.INSTANCEOF);
        palabrasReservadas.put("clase", TipoToken.THIS);
        palabrasReservadas.put("clase", TipoToken.SUPER);
        palabrasReservadas.put("clase", TipoToken.NATIVE);
        palabrasReservadas.put("clase", TipoToken.TRANSIENT);
        palabrasReservadas.put("clase", TipoToken.VOLATILE);


    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        int inicioToken = 0;

        while (indice < source.length()) { // Usamos la variable indice de la clase
            char caracter = source.charAt(indice);
            switch (caracter) {
                // Manejo de caracteres especiales
                case '(':
                    addToken(TipoToken.PARENTESIS_IZQ, "", null);
                    break;
                case ')':
                    addToken(TipoToken.PARENTESIS_DER, "", null);
                    break;
                case '{':
                    addToken(TipoToken.LLAVE_IZQ, "", null);
                    break;
                case '}':
                    addToken(TipoToken.LLAVE_DER, "", null);
                    break;
                case ',':
                    addToken(TipoToken.COMA, "", null);
                    break;
                case '.':
                    addToken(TipoToken.PUNTO, "", null);
                    break;
                case ';':
                    addToken(TipoToken.PUNTO_COMA, "", null);
                    break;
                case '-':
                    addToken(TipoToken.MENOS, "", null);
                    break;
                case '+':
                    addToken(TipoToken.MAS, "", null);
                    break;
                case '*':
                    addToken(TipoToken.ASTERISCO, "", null);
                    break;
                case '/':
                    addToken(TipoToken.SLASH, "", null);
                    break;
                case '!':
                    if (match('=')) {
                        addToken(TipoToken.DIFERENTE, "", null);
                    } else {
                        addToken(TipoToken.EXCLAMACION, "", null);
                    }
                    break;
                case '=':
                    if (match('=')) {
                        addToken(TipoToken.IGUAL_IGUAL, "", null);
                    } else {
                        addToken(TipoToken.IGUAL, "", null);
                    }
                    break;
                case '<':
                    if (match('=')) {
                        addToken(TipoToken.MENOR_IGUAL, "", null);
                    } else {
                        addToken(TipoToken.MENOR, "", null);
                    }
                    break;
                case '>':
                    if (match('=')) {
                        addToken(TipoToken.MAYOR_IGUAL, "", null);
                    } else {
                        addToken(TipoToken.MAYOR, "", null);
                    }
                    break;
                // Manejo de espacios y saltos de línea
                case ' ':
                case '\r':
                case '\t':
                    inicioToken++;
                    break;
                case '\n':
                    inicioToken++;
                    linea++;
                    break;
                // Manejo de identificadores y palabras reservadas
                default:
                    if (isDigit(caracter)) {
                        inicioToken = indice;
                        indice = scanNumero(inicioToken);
                    } else if (isAlpha(caracter)) {
                        inicioToken = indice;
                        indice = scanPalabra(inicioToken);
                    } else {
                        // Caracter desconocido o inválido
                        System.err.println("Error: Caracter desconocido o inválido en la línea " + linea);
                        indice++; // Avanzar al siguiente carácter
                    }
                    continue;
            }
            indice++;
        }

        addToken(TipoToken.EOF, "", null);
        return tokens;
    }

    private int scanNumero(int inicioToken) {
        int indice = inicioToken;
        while (indice < source.length() && isDigit(source.charAt(indice))) {
            indice++;
        }
        String lexema = source.substring(inicioToken, indice);
        addToken(TipoToken.NUMERO, lexema, Integer.parseInt(lexema));
        return indice; // Devolvemos el nuevo valor de indice
    }

    private int scanPalabra(int inicioToken) {
        int indice = inicioToken;
        while (indice < source.length() && (isAlphaNumeric(source.charAt(indice)) || source.charAt(indice) == '_')) {
            indice++;
        }
        String lexema = source.substring(inicioToken, indice);
        TipoToken tipoToken = palabrasReservadas.getOrDefault(lexema, TipoToken.IDENTIFICADOR);
        addToken(tipoToken, lexema, null);
        return indice; // Devolvemos el nuevo valor de indice
    }

    private boolean match(char esperado) {
        if (indice >= source.length()) {
            return false;
        }
        if (source.charAt(indice) != esperado) {
            return false;
        }
        indice++;
        return true;
    }

    private void addToken(TipoToken tipo, String lexema, Object literal) {
        tokens.add(new Token(tipo, lexema, literal, linea));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
