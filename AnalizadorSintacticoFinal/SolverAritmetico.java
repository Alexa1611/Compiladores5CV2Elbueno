import java.util.List;

public class SolverAritmetico {

    private final Nodo nodo;
    private TablaDeSimbolos tablaDeSimbolos;

    public SolverAritmetico(Nodo nodo, TablaDeSimbolos tablaDeSimbolos) {
        this.nodo = nodo;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public Object resolver() {
        if (nodo.getHijos().isEmpty()) {
            Token token = nodo.getValue();
            if (token.esOperando()) {
                if (token.tipo == TipoToken.NUMERO) {
                    return Double.parseDouble(token.literal.toString());
                } else if (token.tipo == TipoToken.VAR) {
                    String nombreVariable = token.literal.toString();
                    return tablaDeSimbolos.obtener(nombreVariable);
                } else if (token.tipo == TipoToken.CADENA) {
                    return token.literal.toString();
                } else if (token.tipo == TipoToken.TRUE) {
                    return true;
                } else if (token.tipo == TipoToken.FALSE) {
                    return false;
                }
            }
        } else {
            List<Nodo> hijos = nodo.getHijos();
            Token token = nodo.getValue();

            if (hijos.size() == 1) {
                SolverAritmetico solver = new SolverAritmetico(hijos.get(0), tablaDeSimbolos);
                return solver.resolver();
            } else if (hijos.size() == 2) {
                SolverAritmetico solver1 = new SolverAritmetico(hijos.get(0), tablaDeSimbolos);
                SolverAritmetico solver2 = new SolverAritmetico(hijos.get(1), tablaDeSimbolos);

                Object res1 = solver1.resolver();
                Object res2 = solver2.resolver();

                if (token.tipo == TipoToken.SUMA) {
                    if (res1 instanceof String || res2 instanceof String) {
                        return res1.toString() + res2.toString();
                    } else if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 + (double) res2;
                    }
                } else if (token.tipo == TipoToken.RESTA) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 - (double) res2;
                    }
                } else if (token.tipo == TipoToken.MULTIPLICACION) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 * (double) res2;
                    }
                } else if (token.tipo == TipoToken.DIVISION) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 / (double) res2;
                    }
                } else if (token.tipo == TipoToken.MAYOR_QUE) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 > (double) res2;
                    }
                } else if (token.tipo == TipoToken.MENOR_QUE) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 < (double) res2;
                    }
                } else if (token.tipo == TipoToken.MAYOR_IGUAL) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 >= (double) res2;
                    }
                } else if (token.tipo == TipoToken.MENOR_IGUAL) {
                    if (res1 instanceof Double && res2 instanceof Double) {
                        return (double) res1 <= (double) res2;
                    }
                } else if (token.tipo == TipoToken.IGUAL) {
                    return res1.equals(res2);
                } else if (token.tipo == TipoToken.DIFERENTE) {
                    return !res1.equals(res2);
                } else if (token.tipo == TipoToken.AND) {
                    if (res1 instanceof Boolean && res2 instanceof Boolean) {
                        return (boolean) res1 && (boolean) res2;
                    }
                } else if (token.tipo == TipoToken.OR) {
                    if (res1 instanceof Boolean && res2 instanceof Boolean) {
                        return (boolean) res1 || (boolean) res2;
                    }
                }
            }
        }

        return null;
    }
}
