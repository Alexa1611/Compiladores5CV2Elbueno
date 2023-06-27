import java.util.List;
import java.util.Stack;

public class GeneradorAST {

    private final List<Token> postfija;
    private final Stack<Nodo> pila;

    public GeneradorAST(List<Token> postfija){
        this.postfija = postfija;
        this.pila = new Stack<>();
    }

 public Arbol generarAST() {
    Stack<Nodo> pilaPadres = new Stack<>();
    Nodo raiz = new Nodo(null);
    pilaPadres.push(raiz);

    Nodo padre = raiz;

    for (Token t : postfija) {
        if (t.tipo == TipoToken.EOF) {
            break;
        }

        if (t.esPalabraReservada()) {
            Nodo n = new Nodo(t);

            padre = pilaPadres.peek();
            padre.insertarSiguienteHijo(n);

            pilaPadres.push(n);
            padre = n;

        } else if (t.esOperando()) {
            Nodo n = new Nodo(t);
            pila.push(n);
        } else if (t.esOperador()) {
            int aridad = t.aridad();
            Nodo n = new Nodo(t);
            if (pila.size() < aridad) {
                // Manejar el caso de pila insuficiente, muestra un mensaje de error o lanza una excepción.
                System.err.println("Error: Insuficientes operandos en la pila");
                // O bien, puedes lanzar una excepción personalizada
                throw new IllegalStateException("Insuficientes operandos en la pila");
            }
            for (int i = 1; i <= aridad; i++) {
                Nodo nodoAux = pila.pop();
                n.insertarHijo(nodoAux);
            }
            pila.push(n);
        } else if (t.tipo == TipoToken.PUNTO_Y_COMA) {

            if (pilaPadres.isEmpty()) {
                // Manejar el caso de pilaPadres vacía, muestra un mensaje de error o lanza una excepción.
                System.err.println("Error: La pilaPadres está vacía");
                // O bien, puedes lanzar una excepción personalizada
                throw new IllegalStateException("La pilaPadres está vacía");
            }
            
            if (pila.isEmpty()) {
                // La pila está vacía, retrocede al padre anterior
                pilaPadres.pop();
                if (!pilaPadres.isEmpty()) {
                    padre = pilaPadres.peek();
                }
            } else {
                Nodo n = pila.pop();

                if (padre.getValue().tipo == TipoToken.VAR) {
                    /*
                     * En el caso del VAR, es necesario eliminar el igual que pudiera aparecer en la raíz del nodo n.
                     */
                    if (n.getValue().tipo == TipoToken.IGUAL) {
                        padre.insertarHijos(n.getHijos());
                    } else {
                        padre.insertarSiguienteHijo(n);
                    }
                    pilaPadres.pop();
                    if (!pilaPadres.isEmpty()) {
                        padre = pilaPadres.peek();
                    }
                } else if (padre.getValue().tipo == TipoToken.PRINT) {
                    padre.insertarSiguienteHijo(n);
                    pilaPadres.pop();
                    if (!pilaPadres.isEmpty()) {
                        padre = pilaPadres.peek();
                    }
                } else {
                    padre.insertarSiguienteHijo(n);
                }
            }
        }
    }

    // Suponiendo que en la pila solamente queda un nodo
    // Nodo nodoAux = pila.pop();
    Arbol programa = new Arbol(raiz);

    return programa;
}
}
