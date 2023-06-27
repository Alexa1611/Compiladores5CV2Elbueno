public class SolverAritmetico {

    private final Nodo nodo;
    private final TablaDeSimbolos tablaDeSimbolos;

    public SolverAritmetico(Nodo nodo, TablaDeSimbolos tablaDeSimbolos) {
        this.nodo = nodo;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public Object resolver() {
        return resolver(nodo);
    }

    private Object resolver(Nodo n) {
        // No tiene hijos, es un operando
        if (n.getHijos() == null) {
            if (n.getValue().tipo == TipoToken.NUMERO || n.getValue().tipo == TipoToken.CADENA) {
                return n.getValue().literal;
            } else if (n.getValue().tipo == TipoToken.IDENTIFICADOR) {
                // Obtener el valor del identificador de la tabla de símbolos
                return tablaDeSimbolos.obtener(n.getValue().literal.toString());
            }
        }

        // Por simplicidad se asume que la lista de hijos del nodo tiene dos elementos
        Nodo izq = n.getHijos().get(0);
        Nodo der = n.getHijos().get(1);

        Object resultadoIzquierdo = resolver(izq);
        Object resultadoDerecho = resolver(der);

        if (resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double) {
            switch (n.getValue().tipo) {
                case SUMA:
                    return ((Double) resultadoIzquierdo + (Double) resultadoDerecho);
                case RESTA:
                    return ((Double) resultadoIzquierdo - (Double) resultadoDerecho);
                case MULTIPLICACION:
                    return ((Double) resultadoIzquierdo * (Double) resultadoDerecho);
                case DIVISION:
                    return ((Double) resultadoIzquierdo / (Double) resultadoDerecho);
            }
        } else if (resultadoIzquierdo instanceof String && resultadoDerecho instanceof String) {
            if (n.getValue().tipo == TipoToken.SUMA) {
                // Ejecutar la concatenación de cadenas
                return resultadoIzquierdo.toString() + resultadoDerecho.toString();
            }
        } else {
            // Error por diferencia de tipos
            throw new UnsupportedOperationException("Operación no válida entre los tipos de datos");
        }

        return null;
    }
}

