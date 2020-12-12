import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try{
            // crear un analizador léxico que se alimenta a partir de la entrada (archivo  o consola)
            Java9Lexer lexer;
            if (args.length>0)
                lexer = new Java9Lexer(CharStreams.fromFileName(args[0]));
            else
                lexer = new Java9Lexer(CharStreams.fromStream(System.in));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // Crear el objeto correspondiente al analizador sintáctico que se alimenta a partir del buffer de tokens
            Java9Parser parser = new Java9Parser(tokens);
            ParseTree tree = parser.compilationUnit(); // Iniciar el analisis sintáctico en la regla inicial: r

            StyleVisitor<Object> loader=new StyleVisitor<>();
            try{
                loader.visit(tree);
            }catch (Exception e){
                System.out.println("Error!");
            }
        } catch (Exception e){
            System.err.println("Error (Test): " + e);
        }
    }
}