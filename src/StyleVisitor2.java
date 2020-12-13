import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class StyleVisitor2 <T> extends Java9BaseVisitor {

    Boolean isclass = false;
    private List<Java9Parser.FieldModifierContext> fieldModifierContexts;


    void error(String error) {
        //try {
        System.out.println(error);
        //throw new IllegalAccessException("Error!");
        //} catch (IllegalAccessException e) {
        //    e.printStackTrace();
        //}
    }

    boolean verifylowerCamelCase(String word){
        int first = (int) word.charAt(0);
        if (!(first >= 97 && first <= 122))
            return false;
        int n = word.length();
        for (int i = 1; i < n; i++) {
            int asciiChar = (int) word.charAt(i);
            if (!((asciiChar >= 65 && asciiChar <= 90) || (asciiChar >= 97 && asciiChar <= 122) ))
                return false;
        }
        return true;
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
                    if((int) str.charAt(i) == 123 && i != str.length() - 1 ){
                        if((int) str.charAt(i+1) != 125 && (int) str.charAt(i+1) != 47){
                            error("{ debe ser seguido de un salto de linea, linea: "+ counter);
                            break;
                        }
                    }
                    if((int) str.charAt(i) == 123 && i == 0){
                        error("{ no debe tener un salto de linea antes, linea: "+ counter);
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
                            error("{ no debe tener un salto de linea antes, linea: "+ counter);
                            break;
                        }
                    }
                    if((int) str.charAt(i) == 125 && i != 0){
                        boolean isspace = false;
                        if(str.charAt(i-1) != 123){
                            for(int j = i-1; j > 0; j--){
                                if(str.charAt(j) != 32){
                                    isspace = true;
                                    break;
                                }
                            }
                        }
                        if(isspace){
                            error("} debe tener un salto de linea o un { antes, linea: "+ counter);
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
                            error("} debe tener un salto de linea despues si no termina un bloque: "+ counter);
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
                if(ctx.classBody().classBodyDeclaration(i).classMemberDeclaration()!=null && ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration() != null){
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
            error("error: violacion de la regla 4.8.3.2 Los brackets forman parte del tipo, no de la variable, linea: "+ctx.dims().LBRACK().get(0).getSymbol().getLine());
        }
        return super.visitVariableDeclaratorId(ctx);
    }

    /***
     *
     * 4.8.4.3
     * */
    @Override
    public Object visitSwitchBlock(Java9Parser.SwitchBlockContext ctx){
        boolean isdefault = false;
        for(int i=0; i<ctx.switchLabel().size(); i++){
            if(ctx.switchLabel(i).DEFAULT() != null){
                isdefault = true;
            }
        }
        if(!isdefault){
            error("error: violacion de la regla 4.8.4.3 Cada switch debe tener una sentencia default: "+ctx.switchLabel(ctx.switchLabel().size()-1).CASE().getSymbol().getLine());
        }
        return super.visitSwitchBlock(ctx);
    }

    /***
     *
     * 5.2.5
     * */
    @Override
    public Object visitFieldDeclaration(Java9Parser.FieldDeclarationContext ctx){
        if(ctx.fieldModifier().isEmpty()){
            String identifier = ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().identifier().getText();
            if(!verifylowerCamelCase(identifier)){
                error("error: violacion de la regla 5.2.5 todas las declaraciones no constantes deben estar en  lowerCamelCase, linea: "+ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().getStart().getLine());
            }
        }else{
            for(int i=0; i<ctx.fieldModifier().size(); i++){
                if(ctx.fieldModifier(i).FINAL() == null){
                    String identifier = ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().identifier().getText();
                    if(!verifylowerCamelCase(identifier)){
                        error("no hay lower, linea: "+ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().getStart().getLine());
                    }
                }
            }
        }
        return super.visitFieldDeclaration(ctx);
    }

    @Override
    public Object visitLocalVariableDeclaration(Java9Parser.LocalVariableDeclarationContext ctx){
        if(ctx.variableModifier().isEmpty()){
            String identifier = ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().identifier().getText();
            if(!verifylowerCamelCase(identifier)){
                error("error: violacion de la regla 5.2.5 todas las declaraciones no constantes deben estar en  lowerCamelCase, linea: "+ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().getStart().getLine());
            }
        }else{
            for(int i=0; i<ctx.variableModifier().size(); i++){
                if(ctx.variableModifier(i).FINAL() == null){
                    String identifier = ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().identifier().getText();
                    if(!verifylowerCamelCase(identifier)){
                        error("no hay lower, linea: "+ctx.variableDeclaratorList().variableDeclarator(0).variableDeclaratorId().getStart().getLine());
                    }
                }
            }
        }
        return  super.visitLocalVariableDeclaration(ctx);
    }
}