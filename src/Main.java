import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try{
            // crear un analizador léxico que se alimenta a partir de la entrada (archivo  o consola)
            Python3Lexer lexer;
            if (args.length>0)
                lexer = new Python3Lexer(CharStreams.fromFileName(args[0]));
            else
                lexer = new Python3Lexer(CharStreams.fromStream(System.in));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // Crear el objeto correspondiente al analizador sintáctico que se alimenta a partir del buffer de tokens
            Python3Parser parser = new Python3Parser(tokens);
            ParseTree tree = parser.file_input(); // Iniciar el analisis sintáctico en la regla inicial: r
        } catch (Exception e){
            System.err.println("Error (Test): " + e);
        }
    }
}