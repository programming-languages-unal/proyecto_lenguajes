import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            // crear un analizador léxico que se alimenta a partir de la entrada (archivo  o consola)
            Java9Lexer lexer;
            if (args.length > 0)
                lexer = new Java9Lexer(CharStreams.fromFileName(args[0]));
            else
                lexer = new Java9Lexer(CharStreams.fromStream(System.in));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // Crear el objeto correspondiente al analizador sintáctico que se alimenta a partir del buffer de tokens
            Java9Parser parser = new Java9Parser(tokens);
            ParseTree tree = parser.compilationUnit(); // Iniciar el analisis sintáctico en la regla inicial: r

            StyleVisitor<Object> styleVisitor1 = new StyleVisitor<>();
            StyleVisitor2<Object> styleVisitor2 = new StyleVisitor2<>();
            StyleVisitor3<Object> styleVisitor3 = new StyleVisitor3<>();
            //try{}catch (Exception e){}finally {}

            //FileInputStream instream = null;
            PrintStream outstream = null;
            /*
            try  {
                //instream =  new  FileInputStream (INPUT);
                outstream =  new  PrintStream ( new FileOutputStream("output.txt"));
                //System.setIn (instream);
                System.setOut (outstream);
            }  catch  (Exception e) {
                System.err.println ( "Error Occurred." );
            }
            */
            try {
                styleVisitor1.visit(tree);
                styleVisitor2.visit(tree);
                styleVisitor3.visit(tree);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("Error (Test): " + e);
        }
    }
}