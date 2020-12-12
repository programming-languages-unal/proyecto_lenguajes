import java.io.*;
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
        try {
            /***
             *
             * 4.1.2
             * */
            File file = new File("input/in.java");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str;
            int counter = 1;
            while((str = br.readLine()) != null) {
                char[] ch = new char[str.length()];
                for (int i = 0; i < str.length(); i++) {
                    ch[i] = str.charAt(i);
                    if((int) str.charAt(i) == 123 && i != str.length() - 1){
                        System.out.println("{ debe ser seguido de un salto de linea, linea: "+ counter);
                        break;
                    }
                    if((int) str.charAt(i) == 123 && i == 0){
                        System.out.println("{ no debe tener un salto de linea antes, linea: "+ counter);
                        break;
                    }
                    if((int) str.charAt(i) == 123){
                        boolean isspace = true;
                        for(int j = i-1; j > 0; j--){
                            if(str.charAt(j) != 32){
                                isspace = false;
                                break;
                            }
                        }
                        if(isspace){
                            System.out.println("{ no debe tener un salto de linea antes, linea: "+ counter);
                            break;
                        }
                    }
                    if((int) str.charAt(i) == 125 && i != 0){
                        boolean isspace = false;
                        for(int j = i-1; j > 0; j--){
                            if(str.charAt(j) != 32){
                                isspace = true;
                                break;
                            }
                        }
                        if(isspace){
                            System.out.println("} debe tener un salto de linea antes, linea: "+ counter);
                            break;
                        }
                    }
                    if((int) str.charAt(i) == 125){
                        boolean isspace = false;
                        for(int j = i+1; j < str.length(); j++){
                            if(str.charAt(j) != 32 && str.charAt(j) != 101 && str.charAt(j) != 44 && str.charAt(j) != 99){
                                isspace = true;
                                break;
                            }
                            if(str.charAt(j) == 101){
                                if(str.charAt(j+1) == 108 && str.charAt(j+2) == 115 && str.charAt(j+3) == 101){
                                    break;
                                }
                            }
                            if(str.charAt(j) == 99){
                                if(str.charAt(j+1) == 97 && str.charAt(j+2) == 116 && str.charAt(j+3) == 99 && str.charAt(j+4) == 104){
                                    break;
                                }
                            }
                        }
                        if(isspace){
                            System.out.println("} debe tener un salto de linea despues si no termina un bloque: "+ counter);
                            break;
                        }
                    }
                }
                counter++;
            }

        } catch (IOException e) {
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

    /***
     *
     * 4.8.3.2
     * */
    @Override
    public Object visitVariableDeclaratorId(Java9Parser.VariableDeclaratorIdContext ctx){
        if(ctx.dims() != null){
            error("error: violacion de la regla [], linea: "+ctx.dims().LBRACK().get(0).getSymbol().getLine());
        }
        return super.visitVariableDeclaratorId(ctx);
    }
}
