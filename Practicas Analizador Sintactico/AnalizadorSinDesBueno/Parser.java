
import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token identificador = new Token(TipoToken.IDENTIFICADOR, "");
    private final Token select = new Token(TipoToken.SELECT, "select");
    private final Token from = new Token(TipoToken.FROM, "from");
    private final Token distinct = new Token(TipoToken.DISTINCT, "distinct");
    private final Token coma = new Token(TipoToken.COMA, ",");
    private final Token punto = new Token(TipoToken.PUNTO, ".");
    private final Token asterisco = new Token(TipoToken.ASTERISCO, "*");
    private final Token finCadena = new Token(TipoToken.EOF, "");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        q();

        if(!hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta válida");
        }
    }

    void q(){
        if(preanalisis.equals(select)){
            match(select);
            d();
            match(from);
            t();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba la palabra reservada SELECT.");
        }
    }

    void d(){
        if(hayErrores) return;

        if(preanalisis.equals(distinct)){
            match(distinct);
            p();
            dPrime();
        }
        else if(preanalisis.equals(asterisco) || preanalisis.equals(identificador)){
            p();
            dPrime();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba DISTINCT, * o un identificador.");
        }
    }
    
    void dPrime(){
        if(hayErrores) return;
        
        if(preanalisis.equals(coma)){
            match(coma);
            p();
            dPrime();
        }
    }

    void p(){
        if(hayErrores) return;

        if(preanalisis.equals(asterisco)){
            match(asterisco);
        }
        else if(preanalisis.equals(identificador)){
            a();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba * o un identificador.");
        }
    }

    void a(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            a2();
            a1();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void a1(){
        if(hayErrores) return;

        if(preanalisis.equals(coma)){
            match(coma);
            a();
        }
    }

    void a2(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            match(identificador);
            a3();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void a3(){
        if(hayErrores) return;

        if(preanalisis.equals(punto)){
            match(punto);
            match(identificador);
        }
    }

    void t(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            t2();
            t1();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void t1(){
        if(hayErrores) return;

        if(preanalisis.equals(coma)){
           match(coma);
            t();
        }
    }

    void t2(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            match(identificador);
            t3();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void t3(){
        if(hayErrores) return;

        if(preanalisis.equals(identificador)){
            match(identificador);
        }
    }

    void match(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un " + t.tipo);
        }
    }

}
/*
Gramatica modificada para descendente
S → select D From T
D → distinct P D'
D' → , P D' | ε
P → * | A
A → A2 A'
A' → A1 A' | ε
A1 → , A | ε
A2 → id A3
A3 → . id | ε
T → T2 T'
T' → T1 T' | ε
T1 → , T | ε
T2 → id T3
T3 → id | ε */