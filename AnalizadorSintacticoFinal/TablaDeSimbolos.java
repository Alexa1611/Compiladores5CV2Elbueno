import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolos {

    private static TablaDeSimbolos instance;
    private final Map<String, Object> values = new HashMap<>();
    private boolean existenErrores;

    private TablaDeSimbolos() {
        // Constructor privado para evitar la instanciación desde fuera de la clase
    }

    public static TablaDeSimbolos getInstance() {
        if (instance == null) {
            instance = new TablaDeSimbolos();
        }
        return instance;
    }

    public boolean existeIdentificador(String identificador) {
        return values.containsKey(identificador);
    }

    public Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        reportarError("Variable no definida '" + identificador + "'.");
        return null;
    }

    public void asignar(String identificador, Object valor) {
        values.put(identificador, valor);

        System.out.println(valor);
    }

    public void reportarError(String mensaje) {
        System.err.println("[Error en la tabla de símbolos]: " + mensaje);
        existenErrores = true;
    }

    public boolean existenErrores() {
        return existenErrores;
    }
}
