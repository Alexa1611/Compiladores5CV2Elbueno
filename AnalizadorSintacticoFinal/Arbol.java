

public class Arbol {
    private final Nodo raiz;

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
    }

    public void recorrer() {
        for (Nodo n : raiz.getHijos()) {
            Token t = n.getValue();
            switch (t.tipo) {
                // Operadores aritméticos
                case SUMA:
                case RESTA:
                case MULTIPLICACION:
                case DIVISION:
                    SolverAritmetico solver = new SolverAritmetico(n);
                    Object res = solver.resolver();
                    System.out.println(res);
                    break;

                case VAR:
                    String nombreVariable = n.getHijos().get(0).getValue().literal;
                    Token valorToken = n.getHijos().get(1).getValue();
                    Object valor = null;
                    if (valorToken.tipo == TipoToken.NUMERO) {
                        valor = Double.parseDouble((valorToken.literal));

                    } else if (valorToken.tipo == TipoToken.CADENA) {
                        valor = valorToken.literal;
                    }
                    break;
                    default:
                    break;
                }
            }
        }
    }