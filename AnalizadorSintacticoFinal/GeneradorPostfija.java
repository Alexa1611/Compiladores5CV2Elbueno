import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPostfija {

    private final List<Token> infija;
    private final Stack<Token> pila;
    private final List<Token> postfija;

    public GeneradorPostfija(List<Token> infija) {
        this.infija = infija;
        this.pila = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir() {
        boolean estructuraDeControl = false;
        Stack<Token> pilaEstructurasDeControl = new Stack<>();

        for (int i = 0; i < infija.size(); i++) {
            Token t = infija.get(i);

            if (t.tipo == TipoToken.EOF) {
                break;
            }

            if (t.esPalabraReservada()) {
                postfija.add(t);
                if (t.esEstructuraDeControl()) {
                    estructuraDeControl = true;
                    pilaEstructurasDeControl.push(t);
                }
            } else if (t.esOperando() || t.tipo == TipoToken.CADENA) {
                postfija.add(t);
            } else if (t.tipo == TipoToken.PARENTESIS_IZQ) {
                pila.push(t);
            } else if (t.tipo == TipoToken.PARENTESIS_DER) {
                while (!pila.isEmpty() && pila.peek().tipo != TipoToken.PARENTESIS_IZQ) {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                if (pila.peek().tipo == TipoToken.PARENTESIS_IZQ) {
                    pila.pop();
                }
                if (estructuraDeControl && infija.get(i + 1).tipo == TipoToken.LLAVE_IZQ) {
                    postfija.add(new Token(TipoToken.PUNTO_Y_COMA, ";", null));
                }
            } else if (t.esOperador()) {
                while (!pila.isEmpty() && pila.peek().precedenciaMayorIgual(t)) {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                pila.push(t);
            } else if (t.tipo == TipoToken.PUNTO_Y_COMA) {
                while (!pila.isEmpty() && pila.peek().tipo != TipoToken.LLAVE_IZQ) {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            } else if (t.tipo == TipoToken.LLAVE_IZQ) {
                pila.push(t);
            } else if (t.tipo == TipoToken.LLAVE_DER && estructuraDeControl) {
                if (infija.get(i + 1).tipo == TipoToken.ELSE) {
                    pila.pop();
                } else {
                    pila.pop();
                    postfija.add(new Token(TipoToken.PUNTO_Y_COMA, ";", null));
                    Token aux = pilaEstructurasDeControl.pop();
                    if(aux.tipo == TipoToken.ELSE){
                       pilaEstructurasDeControl.pop();
                        postfija.add(new Token(TipoToken.PUNTO_Y_COMA, ";", null)); 
                    }
                    
                    if (pilaEstructurasDeControl.isEmpty()) {
                        estructuraDeControl = false;
                    }
                }
            }
        }

        while (!pila.isEmpty()) {
            Token temp = pila.pop();
            postfija.add(temp);
        }

        while (!pilaEstructurasDeControl.isEmpty()) {
            pilaEstructurasDeControl.pop();
            postfija.add(new Token(TipoToken.PUNTO_Y_COMA, ";", null));
        }

        return postfija;
    }
}
