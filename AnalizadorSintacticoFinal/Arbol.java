public class Arbol {
    private Nodo raiz;
    private final TablaDeSimbolos tablaDeSimbolos;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
        this.tablaDeSimbolos = TablaDeSimbolos.getInstance();
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
                String nombreVariable = nodo.getHijos().get(0).getValue().literal.toString();
                Token valorToken = nodo.getHijos().get(1).getValue();
                Object valor = null;
                if (valorToken.tipo == TipoToken.NUMERO) {
                    valor = Double.parseDouble(valorToken.literal.toString());
                } else if (valorToken.tipo == TipoToken.CADENA) {
                    valor = valorToken.literal.toString();
                }
                tablaDeSimbolos.asignar(nombreVariable, valor);
                break;

            case PRINT:
                Nodo identificadorNode = nodo.getHijos().get(0); // Obtener el nodo hijo que contiene el identificador
                String identificador = identificadorNode.getValue().literal.toString();
                Object valorImpresion = tablaDeSimbolos.obtener(identificador);
                System.out.println(valorImpresion);
                break;

            case WHILE:
                if (nodo.getHijos().size() < 2) {
                    Principal.error(t.posicion, "Faltan operandos para el ciclo while");
                    return;
                }
                Nodo condicionWhile = nodo.getHijos().get(0);
                Nodo cuerpoWhile = nodo.getHijos().get(1);
                SolverAritmetico condicionWhileSolver = new SolverAritmetico(condicionWhile, tablaDeSimbolos);
                boolean whileR = (boolean) condicionWhileSolver.resolver();
                while (whileR) {
                    recorrerNodo(cuerpoWhile);
                    condicionWhileSolver = new SolverAritmetico(condicionWhile, tablaDeSimbolos);
                    whileR = (boolean) condicionWhileSolver.resolver();
                }
                break;

            case FOR:
                if (nodo.getHijos().size() < 4) {
                    Principal.error(t.posicion, "Faltan operandos para el ciclo for");
                    return;
                }
                Nodo inicializacionFor = nodo.getHijos().get(0);
                Nodo condicionFor = nodo.getHijos().get(1);
                Nodo actualizacionFor = nodo.getHijos().get(2);
                Nodo cuerpoFor = nodo.getHijos().get(3);
                recorrerNodo(inicializacionFor);
                SolverAritmetico condicionForSolver = new SolverAritmetico(condicionFor, tablaDeSimbolos);
                boolean forR = (boolean) condicionForSolver.resolver();
                while (forR) {
                    recorrerNodo(cuerpoFor);
                    recorrerNodo(actualizacionFor);
                    condicionForSolver = new SolverAritmetico(condicionFor, tablaDeSimbolos);
                    forR = (boolean) condicionForSolver.resolver();
                }
                break;

            case IF:
                if (nodo.getHijos().size() < 2) {
                    Principal.error(t.posicion, "Faltan operandos para la estructura if");
                    return;
                }
                Nodo condicionIf = nodo.getHijos().get(0);
                Nodo cuerpoIf = nodo.getHijos().get(1);
                SolverAritmetico condicionIfSolver = new SolverAritmetico(condicionIf, tablaDeSimbolos);
                boolean ifR = (boolean) condicionIfSolver.resolver();
                if (ifR) {
                    recorrerNodo(cuerpoIf);
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
