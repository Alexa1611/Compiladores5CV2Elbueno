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

    Scanner(String source){
        this.source = source + " ";
    }

    List<Token> scanTokens(){
        int estado = 0;
        char caracter = 0;
        String lexema = "";
        int inicioLexema = 0;

        for(int i=0; i<source.length(); i++){
            caracter = source.charAt(i);

            switch (estado){
                case 0:
                    if(caracter == '='){
                        tokens.add(new Token(TipoToken.ASIGNACION, "=", i + 1));
                    }
                    else if(caracter == '+'){
                        tokens.add(new Token(TipoToken.SUMA, "+", i + 1));
                    }
                    else if(caracter == '-'){
                        tokens.add(new Token(TipoToken.RESTA, "-", i + 1));
                    }
                    else if(caracter == '*'){
                        tokens.add(new Token(TipoToken.MULTIPLICACION, "*", i + 1));
                    }
                    else if(caracter == '/'){
                        tokens.add(new Token(TipoToken.DIVISION, "/", i + 1));
                    }
                    else if(caracter == '('){
                        tokens.add(new Token(TipoToken.PARENTESIS_IZQ, "(", i + 1));
                    }
                    else if(caracter == ')'){
                        tokens.add(new Token(TipoToken.PARENTESIS_DER, ")", i + 1));
                    }
                    else if(caracter == ','){
                        tokens.add(new Token(TipoToken.COMA, ",", i + 1));
                    }
                    else if(caracter == ';'){
                        tokens.add(new Token(TipoToken.PUNTO_Y_COMA, ";", i + 1));
                    }
                    else if(Character.isAlphabetic(caracter)){
                        estado = 1;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    else if(Character.isDigit(caracter)){
                        estado = 2;
                        lexema = lexema + caracter;
                        inicioLexema = i;
                    }
                    break;

                case 1:
                    if(Character.isAlphabetic(caracter) || Character.isDigit(caracter)){
                        lexema = lexema + caracter;
                    }
                    else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, inicioLexema + 1));
                        }
                        else{
                            tokens.add(new Token(tt, lexema, inicioLexema + 1));
                        }

                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 2:
                    if(Character.isDigit(caracter)){
                        lexema = lexema + caracter;
                    }
                    else if(caracter == '.'){
                        estado = 3;
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.ENTERO, lexema, inicioLexema + 1));
                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;

                case 3:
                    if(Character.isDigit(caracter)){
                        lexema = lexema + caracter;
                    }
                    else{
                        tokens.add(new Token(TipoToken.REAL, lexema, inicioLexema + 1));
                        estado = 0;
                        i--;
                        lexema = "";
                        inicioLexema = 0;
                    }
                    break;
            }
        }
        tokens.add(new Token(TipoToken.EOF, "", source.length()));

        return tokens;
    }
}
