import java.io.*;
import java.util.*;

public class StyleVisitor2 <T> extends Java9BaseVisitor {

    private List<Java9Parser.FieldModifierContext> fieldModifierContexts;

    Map<String, Integer> LocalVariables = new HashMap<String, Integer>();;


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

    boolean isNotConstant(List<Java9Parser.FieldModifierContext>modifiers){
        boolean isFinal=false;
        for (int i=0;i<modifiers.size();i++){

            if(modifiers.get(i).getText().equals("final")){
                isFinal=true;
            }
        }
        return isFinal;
    }

    boolean isNotConstant2(List<Java9Parser.VariableModifierContext>modifiers){
        boolean isFinal=false;
        for (int i=0;i<modifiers.size();i++){

            if(modifiers.get(i).getText().equals("final")){
                isFinal=true;
            }
        }
        return isFinal;
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
                        for(int j = i+1; j < str.length()-1; j++){
                            if((int) str.charAt(j) == 47){
                                break;
                            }
                            if((int) str.charAt(j+1) != 125 && (int) str.charAt(j+1) != 47 && (int) str.charAt(j+1) != 32 && (int) str.charAt(j+1) != 59){
                                error("<linea:"+counter+"> Violación de la regla 4.1.2, { debe ser seguido de un salto de linea");
                                break;
                            }
                        }
                    }
                    if((int) str.charAt(i) == 123 && i == 0){
                        error("<linea:"+counter+"> Violación de la regla 4.1.2, { no debe tener un salto de linea antes");
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
                            error("<linea:"+counter+"> Violación de la regla 4.1.2, { no debe tener un salto de linea antes");
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
                            error("<linea:"+counter+"> Violación de la regla 4.1.2, } debe tener un salto de linea o un { antes");
                            break;
                        }
                    }
                    if((int) str.charAt(i) == 125){
                        if(i != str.length()-1){
                            if((int) str.charAt(i+1) == 59 || (int) str.charAt(i+1) == 47){
                                break;
                            }
                        }
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
                            error("<linea:"+counter+"> Violación  de la regla 4.1.2, } debe tener un salto de linea despues si no termina un bloque");
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
         * 3.4.2
         * */
        if (ctx.classBody().classBodyDeclaration(0) != null) {
            int line = 0;
            List<String> list = new ArrayList<String>();
            for(int i=0; i<ctx.classBody().classBodyDeclaration().size(); i++){
                if(ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration() != null) {
                    line = ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration().methodHeader().methodDeclarator().identifier().getStart().getLine();
                    list.add(ctx.classBody().classBodyDeclaration(i).classMemberDeclaration().methodDeclaration().methodHeader().methodDeclarator().identifier().getText());
                }
            }
            List<String> sortedlsit = new ArrayList<String>(list);
            Collections.sort(sortedlsit);
            if(!sortedlsit.equals(list)){
                error("<linea:"+line+"> Violación de la regla 3.4.2, Los metodos deben estan en orden alfabetico, orden recomendado: "+ sortedlsit);
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
            error("<linea:"+ctx.dims().LBRACK().get(0).getSymbol().getLine()+"> Violación de la regla 4.8.3.2, Los brackets forman parte del tipo, no de la variable");
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
            error("<linea:"+ctx.switchLabel(ctx.switchLabel().size()-1).CASE().getSymbol().getLine()+"> Violación de la regla 4.8.4.3, Cada switch debe tener una sentencia default");
        }
        return super.visitSwitchBlock(ctx);
    }

    /***
     *
     * 5.2.5
     * */
    @Override
    public Object visitFieldDeclaration(Java9Parser.FieldDeclarationContext ctx){
        List<Java9Parser.FieldModifierContext>modifiers=ctx.fieldModifier();
        List<Java9Parser.VariableDeclaratorContext> declarations=ctx.variableDeclaratorList().variableDeclarator();
        if(!isNotConstant(modifiers)){
            for(int i =0;i<declarations.size();i++){
                if(declarations.get(i).variableDeclaratorId()!=null
                        &&declarations.get(i).variableDeclaratorId().identifier()!=null){
                    String identifier=declarations.get(i).variableDeclaratorId().identifier().getText();
                    if(!verifylowerCamelCase(identifier)){
                        error("<linea:"+declarations.get(i).variableDeclaratorId().identifier().getStart().getLine()+"> Violación de la regla 5.2.5, los nombres de las no constantes son en lowerCamelCase");
                    }

                }
            }
        }
        return super.visitFieldDeclaration(ctx);
    }

    @Override
    public Object visitLocalVariableDeclaration(Java9Parser.LocalVariableDeclarationContext ctx){

        List<Java9Parser.VariableModifierContext>modifiers=ctx.variableModifier();
        List<Java9Parser.VariableDeclaratorContext> declarations=ctx.variableDeclaratorList().variableDeclarator();
        if(!isNotConstant2(modifiers)){
            for(int i =0;i<declarations.size();i++){
                if(declarations.get(i).variableDeclaratorId()!=null
                        &&declarations.get(i).variableDeclaratorId().identifier()!=null){
                    String identifier=declarations.get(i).variableDeclaratorId().identifier().getText();
                    if(!verifylowerCamelCase(identifier)){
                        error("<linea:"+declarations.get(i).variableDeclaratorId().identifier().getStart().getLine()+"> Violación de la regla 5.2.5, los nombres de las no constantes son en lowerCamelCase");
                    }

                }
            }
        }
        return  super.visitLocalVariableDeclaration(ctx);
    }

    /***
     *
     * 4.8.2.2
     * */

    @Override
    public Object visitMethodDeclaration(Java9Parser.MethodDeclarationContext ctx){
        LocalVariables = new HashMap<String, Integer>();
        return super.visitMethodDeclaration(ctx);
    }

    @Override
    public Object visitIdentifier(Java9Parser.IdentifierContext ctx){
        if(!LocalVariables.containsKey(ctx.getText())){
            LocalVariables.put(ctx.getText(),ctx.getStart().getLine());
        }else{
            int origin = LocalVariables.get(ctx.getText());
            if(ctx.getStart().getLine() - origin > 10){
                error("<linea:"+ctx.getStart().getLine()+"> Violación de la regla 4.8.2.2, la variable local "+ctx.getText()+" fue utilizada muy lejos de su primer uso");
            }
        }
        return super.visitIdentifier(ctx);
    }

}
