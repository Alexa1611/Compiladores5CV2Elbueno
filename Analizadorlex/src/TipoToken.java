public enum TipoToken {
    // Palabras clave
    Y,IF, ELSE, WHILE, FOR, DO, SWITCH, CASE, DEFAULT,
    BREAK, CONTINUE, RETURN, TRY, CATCH, THROW, FINALLY,
    PUBLIC, PRIVATE, PROTECTED, CLASS, INTREFACE, ENUM, EXTENDS,
    IMPLEMENTS, PACKAGE, IMPORT, STATIC, FINAL, ABSTRACT, SYNCHRONIZED,
    VOLATILE, TRANSIENT, NATIVE, SUPER, THIS, INSTANCEOF, NEW, TRUE, FALSE, NULL,

    // Tipos de token
    IDENTIFICADOR,
    CADENA,
    NUMERO,

    // Signos del lenguaje
    MAS,
    MENOS,
    ASTERISCO,
    DIVISION,
    MAYOR,
    MAYOR_IGUAL,
    PARENTESIS_IZQ,
    PARENTESIS_DER,
    MENOR, 
    MENOR_IGUAL,
    IGUAL, 
    IGUAL_IGUAL,
    LLAVE_IZQ,
    LLAVE_DER,
    COMA,
    PUNTO, 
    PUNTO_COMA,
    SLASH, 
    DIFERENTE,
    EXCLAMACION,


    // Final de cadena
    EOF
}