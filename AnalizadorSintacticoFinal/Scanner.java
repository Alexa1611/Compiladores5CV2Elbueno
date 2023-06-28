import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("class", TipoToken.CLASS);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("while", TipoToken.WHILE);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("this", TipoToken.THIS);
        palabrasReservadas.put("for", TipoToken.FOR);
    }

    public Scanner(String source) {
        this.source = source + " ";
    }

    public List<Token> scanTokens() {
        int estado = 0;
        char caracter = 0;
        String lexema = "";
        int inicioLexema = 0;

        for (int i = 0; i < source.length(); i++) {
            caracter = source.charAt(i);

            switch (estado) {
                case 0:
                    if (caracter == '=') {
                        estado = 4;
                    } else if (caracter == '>') {
                        estado = 12;
                    } else if (caracter == '+') {
                        tokens.add(new Token(TipoToken.SUMA, "+", i + 1));
                    } else if (caracter == '-') {
                        tokens.add(new Token(TipoToken.RESTA, "-", i + 1));
                    } else if (caracter == '*') {
                        tokens.add(new Token(TipoToken.MULTIPLICACION, "*", i + 1));
                    } else if (caracter == '/') {
                        estado = 5;
                    } else if (caracter == '<') {
                        estado = 11;
                    } else if (caracter == '(') {
                        tokens.add(new Token(TipoToken.PARENTESIS_IZQ, "(", i + 1));
                    } else if (caracter == ')') {
                        tokens.add(new Token(TipoToken.PARENTESIS_DER, ")", i + 1));
                    } else if (caracter == '{') {
                        tokens.add(new Token(TipoToken.LLAVE_IZQ, "{", i + 1));
                    } else if (caracter == '}') {
                        tokens.add(new Token(TipoToken.LLAVE_DER, "}", i + 1));
                    } else if (caracter == ',') {
                        tokens.add(new Token(TipoToken.COMA, ",", i + 1));
                    } else if (caracter == ';') {
                        tokens.add(new Token(TipoToken.PUNTO_Y_COMA, ";", i + 1));
                    } else if (Character.isAlphabetic(caracter)) {
                        estado = 1;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    } else if (Character.isDigit(caracter)) {
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    } else if (caracter == '\"') {
                        estado = 3;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    break;

                case 1:
                    if (Character.isAlphabetic(caracter) || Character.isDigit(caracter)) {
                        lexema = lexema + caracter;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if (tt == null) {
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, inicioLexema + 1));
                        } else {
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 2:
                    if (Character.isDigit(caracter)) {
                        lexema = lexema + caracter;
                    } else if (caracter == '.' && !lexema.contains(".")) {
                        lexema = lexema + caracter;
                    } else {
                        try {
                            double numero = Double.parseDouble(lexema);
                            tokens.add(new Token(TipoToken.NUMERO, String.valueOf(numero), inicioLexema + 1));
                        } catch (NumberFormatException e) {
                            // Manejo del error si el número no es válido
                            throw new IllegalArgumentException("Error de sintaxis: número inválido");
                        }
                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 3:
                    if (caracter != '\"') {
                        lexema = lexema + caracter;
                    } else {
                        tokens.add(new Token(TipoToken.CADENA, lexema, inicioLexema + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 4:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.IGUAL_IGUAL, "==", i + 1));
                        estado = 0;
                    } else {
                        tokens.add(new Token(TipoToken.IGUAL, "=", inicioLexema + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                        i--;
                    }
                    break;

                case 5:
                    if (caracter == '/') {
                        // Comentario de una línea, se omite el resto de la línea actual
                        i = source.indexOf('\n', i); // Salta hasta el final de la línea
                        estado = 0;
                    } else if (caracter == '*') {
                        // Comentario de varias líneas, se omite hasta encontrar el cierre del
                        // comentario
                        estado = 6;
                    } else {
                        tokens.add(new Token(TipoToken.DIVISION, "/", i + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                        i--;
                    }
                    break;

                case 6:
                    if (caracter == '*') {
                        if (source.charAt(i + 1) == '/') {
                            // Cierre del comentario de varias líneas
                            i++; // Avanza el índice para evitar agregar el carácter '/'
                            estado = 0;
                        }
                    } else if (caracter == '\0') {
                        // Si se alcanza el final del código sin encontrar el cierre del comentario, se
                        // genera un error
                        throw new IllegalArgumentException("Error de sintaxis: comentario de varias líneas no cerrado");
                    }
                    break;

                case 11:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.MENOR_IGUAL, "<=", i + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    } else {
                        tokens.add(new Token(TipoToken.MENOR_QUE, "<", i + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                        i--;
                    }
                    break;

                case 12:
                    if (caracter == '=') {
                        tokens.add(new Token(TipoToken.MAYOR_IGUAL, ">=", i + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                    } else {
                        tokens.add(new Token(TipoToken.MAYOR_QUE, ">", inicioLexema + 1));
                        estado = 0;
                        lexema = "";
                        inicioLexema = 0;
                        i--;
                    }
                    break;

            }
        }
        tokens.add(new Token(TipoToken.EOF, "", source.length()));

        return tokens;
    }
}
