
package AnalizadorSintacticoFinal;


public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)


    // Palabras clave:

   Y, 
   CLASE, 
   ADEMAS,
   PARA, 
   FUNCION,
   SI, 
   NULO,
   IMPRIMIR,
   DEVOLVER,
   SUPER,
   ESTE,
   VERDADERO,
   FALSO, 
   MIENTRAS,
   VARIABLE,
   O, 
   
   NO,
   IGUAL,
   DIFERENTE,
   ASIGNAR,
   MENOR_QUE,
   MENOR_IGUAL, 
   MAYOR_QUE,
   MAYOR_IGUAL, 
   NUMERO, 
   CADENA,
   
   IDENTIFICADOR,
   
   
   SUMA,
   RESTA,
   MULTIPLICACION,
   DIVISION,
  
  
   PARENTESIS_IZQ,
   PARENTESIS_DER,
   CORCHETE_IZQ, 
   CORCHETE_DER, 
   COMA, 
   PUNTO,
   PUNTO_Y_COMA,
   LLAVE_DER,
   LLAVE_IZQ,
    EOF
}