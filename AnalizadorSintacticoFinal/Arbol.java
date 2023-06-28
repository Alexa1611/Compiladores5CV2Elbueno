
public class Arbol {
    private Nodo raiz;
   

    public Arbol(Nodo raiz) {
        this.raiz = raiz;
        
    }
    public Arbol(){}
    public void setRaiz(Nodo raiz){
        this.raiz=raiz;

    }
    

public void recorrer() {
   for(Nodo nodo : raiz.getHijos()){

        Token t = nodo.getValue();
        switch (t.tipo) {
            // Operadores aritméticos
            case SUMA:
            case RESTA:
            case MULTIPLICACION:
            case DIVISION:
            case MAYOR_QUE:
            case MENOR_QUE:
            case MAYOR_IGUAL:
            case MENOR_IGUAL:
            case IGUAL_IGUAL:
            case DIFERENTE:
            case IGUAL:
                SolverAritmetico solver = new SolverAritmetico(nodo);
                Object res = solver.resolver();
                System.out.println(res);
                break;

            case VAR:
            if(!TablaDeSimbolos.existeIdentificador(nodo.getHijos().get(0).getValue().lexema)){
                if(nodo.getHijos().size() == 2){
                    Nodo nombreVariable = nodo.getHijos().get(1);
                    SolverAritmetico solverAritmetico = new SolverAritmetico(nombreVariable);
                    Object valorVar = solverAritmetico.resolver();

                    TablaDeSimbolos.asignar(nodo.getHijos().get(0).getValue().lexema,valorVar);
                }else{
                    TablaDeSimbolos.asignar(nodo.getHijos().get(0).getValue().lexema, null);
                }
            }
                
                break;

            case PRINT:
                Nodo identificadorNode = nodo.getHijos().get(0); // Obtener el nodo hijo que contiene el identificador
                SolverAritmetico identificadorSol = new SolverAritmetico(identificadorNode);
                Object valorImpresion = identificadorSol.resolver();
                System.out.println(valorImpresion);
                break;

            case WHILE:
                if (nodo.getHijos().size() == 1){
                        System.err.println("Error : Instruccion While");
                        System.exit(1);
                    }
                    SolverAritmetico solverMientras = new SolverAritmetico();
                    Arbol arbolInstruccionwhile = new Arbol();
                    Nodo auxRaizwhile = new Nodo(null);
                    Nodo condicionwhile = nodo.getHijos().get(0);
                    boolean condicionCumplidawhile = (Boolean) solverMientras.resolver(condicionwhile);

                    while (condicionCumplidawhile) {
                        for (int i = 1; i < nodo.getHijos().size(); i++) {
                            Nodo instruccionwhile = nodo.getHijos().get(i);
                            auxRaizwhile.insertarHijo(instruccionwhile);

                            arbolInstruccionwhile.setRaiz(auxRaizwhile);
                            arbolInstruccionwhile.recorrer();
                           
                        }
                        condicionCumplidawhile = (Boolean) solverMientras.resolver(condicionwhile);
                    }
                break;

            case FOR:
                 if (nodo.getHijos().size() == 3){
                        System.err.println("Error : Faltan instrucciones el For");
                        System.exit(1);
                    }
                    SolverAritmetico solverPara = new SolverAritmetico();
                    Arbol arbolInstruccionPara = new Arbol();
                    Nodo auxRaizPara = new Nodo(null);

                    Nodo auxdecla = new Nodo(null);
                    Nodo declaracion = nodo.getHijos().get(0);
                    auxdecla.insertarHijo(declaracion);
                    Arbol arbolDeclaracion = new Arbol(auxdecla);
                    arbolDeclaracion.recorrer();

                    Nodo paracondicion = nodo.getHijos().get(1);
                    //SolverAritmetico solverParaCondicion = new SolverAritmetico(paracondicion);
                    boolean condicionParaCumplida = (Boolean) solverPara.resolver(paracondicion);

                    while (condicionParaCumplida) {
                        for (int i = 3; i < nodo.getHijos().size(); i++) {
                            Nodo instruccionPara = nodo.getHijos().get(i);
                            auxRaizPara.insertarHijo(instruccionPara);

                            arbolInstruccionPara.setRaiz(auxRaizPara);
                            arbolInstruccionPara.recorrer();
                            
                        }

                        // Aquí agregar el código para el incremento
                        Nodo incremento = nodo.getHijos().get(2);
                        //SolverAritmetico solverCondicionwhile = new SolverAritmetico(incremento);
                        solverPara.resolver(incremento);
                        condicionParaCumplida = (Boolean) solverPara.resolver(paracondicion);
                    }
                break;

            case IF:
                if (nodo.getHijos().size() == 1 || nodo.getHijos().get(1).getValue().tipo == TipoToken.ELSE){
                        System.err.println("Error : Falta una instruccion dentro del IF");
                        System.exit(1);
                    }
                Nodo condicionIf = nodo.getHijos().get(0);
                SolverAritmetico condicionIfSolver = new SolverAritmetico(condicionIf);
                boolean ifR = (boolean) condicionIfSolver.resolver();
                if (ifR) {
                    for(int i=1; i<nodo.getHijos().size() -1;i++){
                        Nodo auxRaiz = new Nodo(null);
                        Nodo inst = nodo.getHijos().get(i);
                        auxRaiz.insertarHijo(inst);
                        Arbol arbInst = new Arbol(auxRaiz);
                        arbInst.recorrer();
                    }
                    }else if( nodo.getHijos().get(nodo.getHijos().size()-1).getValue().tipo == TipoToken.ELSE){
                        Nodo auxRaiz = new Nodo(null);
                        Nodo elseNodo = nodo.getHijos().get(nodo.getHijos().size() - 1);

                        auxRaiz.insertarHijo(elseNodo);

                        Arbol arbolInstruccion = new Arbol(auxRaiz);
                        arbolInstruccion.recorrer();
                    }

                    
            
                break;

            default:
            System.out.print("Error inesperado");
                break;
        }
    
       }
        }
}