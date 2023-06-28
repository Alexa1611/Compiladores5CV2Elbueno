


public class SolverAritmetico {

private Nodo nodo;

    public SolverAritmetico(Nodo nodo) {
        this.nodo = nodo;
    }
    public SolverAritmetico(){}

    public Object resolver(){
        return resolver(nodo);
    }
    public Object resolver(Nodo n){
        // No tiene hijos, es un operando
        if(n.getHijos() == null){
            if(n.getValue().tipo == TipoToken.NUMERO || n.getValue().tipo == TipoToken.CADENA) {
                return n.getValue().literal;
            }
            else if(n.getValue().tipo == TipoToken.IDENTIFICADOR){
                // Ver la tabla de símbolos
                if(TablaDeSimbolos.existeIdentificador(n.getValue().lexema)){
                    return TablaDeSimbolos.obtener(n.getValue().lexema);
                }else {
                    System.err.println("Error!! : Variable " +  n.getValue().lexema + " no definida ");
                    System.exit(1);
                }
            } else if (n.getValue().tipo == TipoToken.FALSE) {
                return false;
            } else if (n.getValue().tipo == TipoToken.TRUE) {
                return true;
            }
        }

        // Por simplicidad se asume que la lista de hijos del nodo tiene dos elementos
        Nodo izq = n.getHijos().get(0);
        Nodo der = n.getHijos().get(1);


        Object resultadoIzquierdo = resolver(izq);
        Object resultadoDerecho = resolver(der);


        if (resultadoIzquierdo instanceof Boolean && resultadoDerecho instanceof Boolean){
            switch (n.getValue().tipo){
                case AND:
                    return ((Boolean)resultadoIzquierdo && (Boolean) resultadoDerecho);
                case OR:
                    return ((Boolean)resultadoIzquierdo || (Boolean) resultadoDerecho);
            }
        }else if(resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double){
            switch (n.getValue().tipo){
                case SUMA:
                    return ((Double)resultadoIzquierdo + (Double) resultadoDerecho);
                case RESTA:
                    return ((Double)resultadoIzquierdo - (Double) resultadoDerecho);
                case MULTIPLICACION:
                    return ((Double)resultadoIzquierdo * (Double) resultadoDerecho);
                case DIVISION:
                    return ((Double)resultadoIzquierdo / (Double) resultadoDerecho);
                case MENOR_QUE:
                    return ((Double)resultadoIzquierdo < (Double) resultadoDerecho);
                case MENOR_IGUAL:
                    return ((Double)resultadoIzquierdo <= (Double) resultadoDerecho);
                case MAYOR_QUE:
                    return ((Double)resultadoIzquierdo > (Double) resultadoDerecho);
                case MAYOR_IGUAL:
                    return ((Double)resultadoIzquierdo >= (Double) resultadoDerecho);
                case IGUAL_IGUAL:
                    return (((Double) resultadoIzquierdo).equals((Double) resultadoDerecho));
                case DIFERENTE:
                    return (!((Double) resultadoIzquierdo).equals((Double) resultadoDerecho));
                case IGUAL:
                    // Asignar el valor de la derecha a la variable de la izquierda
                    if (izq.getValue().tipo == TipoToken.IDENTIFICADOR){
                        TablaDeSimbolos.asignar(izq.getValue().lexema, resultadoDerecho);
                    }
                    break;

            }
        }
        
            
        
        else if(resultadoIzquierdo instanceof String || resultadoDerecho instanceof String){
            String valor;

            if (! (nodo.getValue().tipo == TipoToken.SUMA)){
                return null;
            }

            // Ejecutar la concatenación

            if (!(resultadoDerecho instanceof String)){
                valor = resultadoDerecho.toString();
                return resultadoIzquierdo + valor;
            }

            if (!(resultadoIzquierdo instanceof String)){
                valor = resultadoIzquierdo.toString();
                return valor + resultadoDerecho;
            }

            return (String) resultadoIzquierdo + resultadoDerecho;
        }
        return null;
    }}
    
