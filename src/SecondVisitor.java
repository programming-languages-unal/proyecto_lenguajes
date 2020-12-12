import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public Object visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {

        if (ctx.identifier() != null) {
            if(isclass == false){
                isclass = true;
            }else{
                error("error: violacion de la regla 3.4.1 Cada clase de nivel superior reside en un archivo fuente propio, linea: "+ ctx.CLASS().getSymbol().getLine());
            }

        }

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
        return null;
    }
}
