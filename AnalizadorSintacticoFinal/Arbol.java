import java.util.ArrayList;
import java.util.List;

public class Arbol {
    private final Nodo raiz;
    private final TablaDeSimbolos tablaDeSimbolos;

    public Arbol(Nodo raiz, TablaDeSimbolos tablaDeSimbolos) {
        this.raiz = raiz;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public void recorrer() {
        recorrerNodo(raiz);

    }

    private void recorrerNodo(Nodo nodo) {
        if (nodo.getValue() == null) {
            return;
        }

        Token t = nodo.getValue();
        switch (t.tipo) {
            // Operadores aritméticos
            case SUMA:
            case RESTA:
            case MULTIPLICACION:
            case DIVISION:
                SolverAritmetico solver = new SolverAritmetico(nodo, tablaDeSimbolos);
                Object res = solver.resolver();
                System.out.println(res);
                break;

            case VAR:
                String nombreVariable = nodo.getHijos().get(0).getValue().literal;
                Token valorToken = nodo.getHijos().get(1).getValue();
                Object valor = null;
                if (valorToken.tipo == TipoToken.NUMERO) {
                    valor = Double.parseDouble((valorToken.literal));

                } else if (valorToken.tipo == TipoToken.CADENA) {
                    valor = valorToken.literal;
                }
                break;
            case PRINT:
                Nodo expresion = nodo.getHijos().get(0);
                SolverAritmetico print = new SolverAritmetico(expresion, tablaDeSimbolos);
                Object printR = print.resolver();
                System.out.println(printR);
                break;
            case WHILE:
                Nodo condWhile = nodo.getHijos().get(0);
                Nodo cuerpWhile = nodo.getHijos().get(1);
                SolverAritmetico condsSolver = new SolverAritmetico(condWhile, tablaDeSimbolos);
                boolean whileR = (boolean) condsSolver.resolver();
                while (whileR) {
                    recorrerNodo(cuerpWhile);
                    condsSolver = new SolverAritmetico(condWhile, tablaDeSimbolos);
                    whileR = (boolean) condsSolver.resolver();
                }
                break;
            case FOR:
                Nodo IniFor = nodo.getHijos().get(0);
                Nodo condsFor = nodo.getHijos().get(1);
                Nodo actFor = nodo.getHijos().get(2);
                Nodo cuerpFor = nodo.getHijos().get(3);
                recorrerNodo(IniFor);
                SolverAritmetico condsForS = new SolverAritmetico(condsFor, tablaDeSimbolos);
                boolean forR = (boolean) condsForS.resolver();
                while (forR) {
                    recorrerNodo(cuerpFor);
                    recorrerNodo(actFor);
                    condsForS = new SolverAritmetico(condsFor, tablaDeSimbolos);
                    forR = (boolean) condsForS.resolver();

                }
                break;
            case IF:
                Nodo condsIf = nodo.getHijos().get(0);
                Nodo cuerpIf = nodo.getHijos().get(1);
                SolverAritmetico condsIfS = new SolverAritmetico(condsIf, tablaDeSimbolos);
                boolean ifR = (boolean) condsIfS.resolver();
                if (ifR) {
                    recorrerNodo(cuerpIf);
                }
                break;
            default:
                break;
        }
        for (Nodo n : nodo.getHijos()) {
            recorrerNodo(n);
        }

    }
}
