package AnalizadorSintacticoFinal;

public class Arbol {
    private Nodo raiz;

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
                case MENOR_QUE:
                case MENOR_IGUAL:
                case MAYOR_IGUAL:
                case MAYOR_QUE:
                case IGUAL:
                case DIFERENTE:
                case IGUAL_IGUAL:
                    SolverAritmetico solver = new SolverAritmetico(n);
                    solver.resolver();
                    // System.out.println(res);
                    break;

                case VAR:
                    // Crear una variable. Usar tabla de simbolos

                    Nodo VerHijos = n.getHijos().get(0);

                    if (VerHijos.getHijos() == null) {
                        if (TablaDeSimbolos.existeIdentificador(n.getHijos().get(0).getValue().lexema) == false) {
                            TablaDeSimbolos.asignar(n.getHijos().get(0).getValue().lexema, null);
                        } else {
                            System.err.println("Ya existe el identificador " + n.getHijos().get(0).getValue().lexema);
                            System.exit(1);
                        }

                    }

                    else if (TablaDeSimbolos
                            .existeIdentificador(VerHijos.getHijos().get(0).getValue().lexema) == false) {

                        if (VerHijos.getHijos().size() == 2) {
                            Nodo NodoOperaciones = VerHijos.getHijos().get(1);
                            SolverAritmetico SolverVar = new SolverAritmetico(NodoOperaciones);
                            Object ResOpeVar = SolverVar.resolver();

                            TablaDeSimbolos.asignar(VerHijos.getHijos().get(0).getValue().lexema, ResOpeVar);
                        }
                    } else {
                        System.err
                                .println("Ya existe el identificador " + VerHijos.getHijos().get(0).getValue().lexema);
                        System.exit(1);
                    }

                    break;
                case IF:
                    Nodo VerHijos1 = n.getHijos().get(0);
                    int ELSE = n.getHijos().size() - 1;

                    if (VerHijos1.getHijos().size() == 1) {
                        System.out.println("Faltan elementos dentro del if");
                        System.exit(1);
                    }

                    SolverAritmetico SlvCon = new SolverAritmetico(VerHijos1);
                    boolean ConCumIf = (boolean) SlvCon.resolver();

                    if (ConCumIf) {
                        for (int a = 0; a <= n.getHijos().size() - 1; a++) {

                            if (n.getHijos().get(a).getValue().tipo == TipoToken.ELSE) {
                                a++;
                            } else {
                                Nodo Aux = new Nodo(null);
                                Nodo SigIns = n.getHijos().get(a);
                                Aux.insertarHijo(SigIns);
                                Arbol NuevoArbol = new Arbol(Aux);
                                NuevoArbol.recorrer();
                            }
                        }
                    } else if (n.getHijos().get(ELSE).getValue().tipo == TipoToken.ELSE) {
                        Nodo Aux = new Nodo(null);
                        Nodo ademas = n.getHijos().get(ELSE);
                        Aux.insertarHijo(ademas);
                        Arbol NuevoArbol = new Arbol(Aux);
                        NuevoArbol.recorrer();
                    }

                    break;
                case ELSE:
                    if (n.getHijos() == null) {
                        System.out.println("Faltan elementos dentro del else");
                        System.exit(1);
                    }

                    for (int b = 0; b < n.getHijos().size(); b++) {
                        Nodo Aux = new Nodo(null);
                        Nodo SigIns = n.getHijos().get(b);
                        Aux.insertarHijo(SigIns);
                        Arbol NuevoArbol = new Arbol(Aux);
                        NuevoArbol.recorrer();
                    }
                    break;
                case PRINT:

                    Nodo print = n.getHijos().get(0);
                    SolverAritmetico Slv = new SolverAritmetico(print);
                    Object resultado = Slv.resolver();
                    System.out.println(resultado);

                    break;

                case WHILE:
                    Nodo condicion = n.getHijos().get(0);
                    Nodo cuerpo = n.getHijos().get(1);

                    SolverAritmetico slvCondicion = new SolverAritmetico(condicion);
                    boolean cumpleCondicion = (boolean) slvCondicion.resolver();

                    while (cumpleCondicion) {
                        Nodo aux = new Nodo(null);
                        aux.insertarHijo(cuerpo);
                        Arbol nuevoArbol = new Arbol(aux);
                        nuevoArbol.recorrer();

                        slvCondicion = new SolverAritmetico(condicion);
                        cumpleCondicion = (boolean) slvCondicion.resolver();
                    }
                    break;

                case FOR:
                    Nodo inicializacion = n.getHijos().get(0);
                    Nodo condicionPara = n.getHijos().get(1);
                    Nodo incremento = n.getHijos().get(2);
                    Nodo cuerpoPara = n.getHijos().get(3);

                    // Inicialización
                    SolverAritmetico slvInicializacion = new SolverAritmetico(inicializacion);
                    slvInicializacion.resolver();

                    // Condición
                    SolverAritmetico slvCondicionPara = new SolverAritmetico(condicionPara);
                    boolean cumpleCondicionPara = (boolean) slvCondicionPara.resolver();

                    while (cumpleCondicionPara) {
                        Nodo aux = new Nodo(null);
                        aux.insertarHijo(cuerpoPara);
                        Arbol nuevoArbol = new Arbol(aux);
                        nuevoArbol.recorrer();

                        // Incremento
                        SolverAritmetico slvIncremento = new SolverAritmetico(incremento);
                        slvIncremento.resolver();

                        // Actualización de la condición
                        slvCondicionPara = new SolverAritmetico(condicionPara);
                        cumpleCondicionPara = (boolean) slvCondicionPara.resolver();
                    }
                    break;

                default:
                    break;
            }
        }
    }

}
