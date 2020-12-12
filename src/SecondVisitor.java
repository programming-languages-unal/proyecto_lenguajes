import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SecondVisitor <T> extends Java9BaseVisitor {

    Boolean isclass = false;


    void error(String error) {
        //try {
        System.out.println(error);
        //throw new IllegalAccessException("Error!");
        //} catch (IllegalAccessException e) {
        //    e.printStackTrace();
        //}
    }

    @Override
    public  Object visitCompilationUnit(Java9Parser.CompilationUnitContext ctx){
        try{
            Scanner input = new Scanner(new File("input/in.java"));
            String answer = input.nextLine();
            System.out.println(answer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return super.visitCompilationUnit(ctx);
    }

    @Override
    public Object visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {

        /***
         *
         * 3.4.1
         * */
        if (ctx.identifier() != null) {
            if(isclass == false){
                isclass = true;
            }else{
                error("error: violacion de la regla 3.4.1 Cada clase de nivel superior reside en un archivo fuente propio, linea: "+ ctx.CLASS().getSymbol().getLine());
            }

        }

        /***
         *
         * 3.4.2
         * */
        if (ctx.classBody().classBodyDeclaration(0) != null) {
            int test = 0;
            List<String> list = new ArrayList<String>();
            for(int i=0; i<ctx.classBody().classBodyDeclaration().size(); i++){
                if(ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration() != null){
                    list.add(ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration().methodHeader().methodDeclarator().identifier().getText());
                }
            }
            List<String> sortedlsit = new ArrayList<String>(list);
            Collections.sort(sortedlsit);
            if(!sortedlsit.equals(list)){
                error("error: violacion de la regla 3.4.2 Los metodos deben estan en orden alfabetico, orden recomendado "+ sortedlsit);
            }
        }
        return super.visitNormalClassDeclaration(ctx);
    }
}
