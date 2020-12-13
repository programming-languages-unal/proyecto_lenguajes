import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class StyleVisitor3<T> extends Java9BaseVisitor {
    private int lastLineMarkerAnnotation;
    private TreeMap<String,List<String>> mapClassFunctions=new TreeMap<>();
    public StyleVisitor3(){
        this.lastLineMarkerAnnotation=-1;
    }
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
    boolean isConstant(List<Java9Parser.FieldModifierContext>modifiers){
        boolean isStatic=false;
        boolean isFinal=false;
        for (int i=0;i<modifiers.size();i++){
            if(modifiers.get(i).getText().equals("static")){
                isStatic=true;
            }
            if(modifiers.get(i).getText().equals("final")){
                isFinal=true;
            }
        }
        return isFinal && isStatic;
    }
    boolean isUpperCase(String identifier){
        for(int i =0;i<identifier.length();i++){
            int ascii=(int)identifier.charAt(i);
            if(!(ascii>=65 && ascii<=90 || ascii==95)){
                return false;
            }
        }
        return true;
    }

    @Override
    public T visitMethodDeclaration(Java9Parser.MethodDeclarationContext ctx) {
        List<Java9Parser.MethodModifierContext> modifier_contexts=ctx.methodModifier();
        int numAnnotations=0;

        /*4.8.5*/
        for (int i =0;i<modifier_contexts.size() ;i++){

            if(modifier_contexts.get(i).annotation()!=null && modifier_contexts.get(i).annotation().markerAnnotation()!=null){
                numAnnotations++;
            }

        }

        for (int i =0;i<modifier_contexts.size() ;i++){

            if(modifier_contexts.get(i).annotation()!=null && modifier_contexts.get(i).annotation().markerAnnotation()!=null){
                if(modifier_contexts.get(i).annotation().getStart().getLine()==this.lastLineMarkerAnnotation){
                    error("error:violacion de 4.8.5 Annotations, se tienen varias anotaciones en la linea "+modifier_contexts.get(i).annotation().getStart().getLine());
                    numAnnotations++;
                    break;
                }
                this.lastLineMarkerAnnotation=modifier_contexts.get(i).annotation().getStart().getLine();
            }
        }
        this.lastLineMarkerAnnotation=-1;
        if(numAnnotations>1){

            for (int i =0;i<modifier_contexts.size() ;i++){

                if(modifier_contexts.get(i).annotation()==null ){
                    if(modifier_contexts.get(i).getStart().getLine()==this.lastLineMarkerAnnotation){
                        error("error:violacion de 4.8.5 Annotations, Si hay mas de una anotacion la ultima no puede ir en la misma linea de la signatura linea: "+modifier_contexts.get(i).getStart().getLine());
                        //System.exit(-1);
                        break;
                    }

                }else{
                    this.lastLineMarkerAnnotation=modifier_contexts.get(i).annotation().getStart().getLine();
                }
            }
        }



        return (T) super.visitMethodDeclaration(ctx);
    }

    @Override
    public Object visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {
        List<Java9Parser.ClassModifierContext> modifier_contexts=ctx.classModifier();


        for (int i =0;i<modifier_contexts.size() ;i++){

            if(modifier_contexts.get(i).annotation()!=null && modifier_contexts.get(i).annotation().markerAnnotation()!=null){
                if(modifier_contexts.get(i).annotation().getStart().getLine()==this.lastLineMarkerAnnotation){
                    error("error:violacion de 4.8.5 Annotations, se tienen varias anotaciones en la linea "+modifier_contexts.get(i).annotation().getStart().getLine());
                    //System.exit(-1);
                    break;
                }
                this.lastLineMarkerAnnotation=modifier_contexts.get(i).annotation().getStart().getLine();
            }
        }


        /*6.1**/
        /*guardamos en mapClassFunctions como llave el nombre de la clase y como valor un
         * ArrayList con los nombres de sus metodos*/
        ArrayList<String>functions=new ArrayList<>();
        if(ctx.classBody()!=null
                &&ctx.classBody().classBodyDeclaration()!=null){
            List<Java9Parser.ClassBodyDeclarationContext> declarations=ctx.classBody().classBodyDeclaration();
            for (int i=0;i<declarations.size();i++){
                if(declarations.get(i).classMemberDeclaration()!=null
                        &&declarations.get(i).classMemberDeclaration().methodDeclaration()!=null){
                    Java9Parser.MethodDeclarationContext method=declarations.get(i).classMemberDeclaration().methodDeclaration();
                    if(method.methodHeader()!=null &&method.methodHeader().methodDeclarator()!=null){
                        functions.add(method.methodHeader().methodDeclarator().identifier().getText());
                    }
                }

            }
            this.mapClassFunctions.put(ctx.identifier().getText(),functions);
        }

        /*Si hereda de otra clase*/
        if(ctx.superclass()!=null
                &&ctx.superclass().classType()!=null
        ){

            String parentName=ctx.superclass().classType().identifier().getText();
            List<String>parentFunctions=this.mapClassFunctions.get(parentName);
            String childName=ctx.identifier().getText();
            List<String>childMethods=this.mapClassFunctions.get(childName);
            List<String>commonMethods=parentFunctions.stream().filter(childMethods:: contains).collect(Collectors.toList());

            List<Java9Parser.ClassBodyDeclarationContext> declarations=ctx.classBody().classBodyDeclaration();
            for (int i=0;i<declarations.size();i++){
                if(declarations.get(i).classMemberDeclaration()!=null
                        &&declarations.get(i).classMemberDeclaration().methodDeclaration()!=null){
                    Java9Parser.MethodDeclarationContext method=declarations.get(i).classMemberDeclaration().methodDeclaration();
                    if(method.methodHeader()!=null &&method.methodHeader().methodDeclarator()!=null){
                        String childMethodName=method.methodHeader().methodDeclarator().identifier().getText();
                        if(commonMethods.contains(childMethodName)){

                            if(method.methodModifier().size()==0){
                                error("error: violacion de la regla 6.1, el metodo "+childMethodName+" es heredado  y no cuenta con anotaciones (Debe contar con la anotacion Override), linea: "+method.getStart().getLine());

                            }
                            else{
                                boolean areOverride=false;
                                for(int j=0;j<method.methodModifier().size();j++){
                                    if(method.methodModifier().get(j).annotation()!=null
                                            &&method.methodModifier().get(j).annotation().markerAnnotation()!=null&&
                                            method.methodModifier().get(j).annotation().markerAnnotation().typeName()!=null
                                            &&method.methodModifier().get(j).annotation().markerAnnotation().typeName().identifier()!=null
                                            &&method.methodModifier().get(j).annotation().markerAnnotation().typeName().identifier().getText().equals("Override")){
                                        areOverride=true;


                                    }
                                }
                                if(!areOverride){
                                    error("<linea:"+method.getStart().getLine()+"> violacion la regla 6.1, el metodo "+childMethodName+" es heredado y no cuenta con la anotacion @Override");
                                }

                            }

                        }
                    }
                }

            }

        }


        /*Si implementa una interfaz*/

        if(ctx.superinterfaces()!=null&&ctx.superinterfaces().interfaceTypeList()!=null
        ){
            List<Java9Parser.InterfaceTypeContext> interfaceTypes=ctx.superinterfaces().interfaceTypeList().interfaceType();

            for(int a=0;a<interfaceTypes.size();a++){
                String parentInterface=interfaceTypes.get(a).classType().identifier().getText();


                List<String>parentFunctions=this.mapClassFunctions.get(parentInterface);
                String childName=ctx.identifier().getText();
                List<String>childMethods=this.mapClassFunctions.get(childName);
                List<String>commonMethods=parentFunctions.stream().filter(childMethods:: contains).collect(Collectors.toList());

                List<Java9Parser.ClassBodyDeclarationContext> declarations=ctx.classBody().classBodyDeclaration();
                for (int i=0;i<declarations.size();i++){
                    if(declarations.get(i).classMemberDeclaration()!=null
                            &&declarations.get(i).classMemberDeclaration().methodDeclaration()!=null){
                        Java9Parser.MethodDeclarationContext method=declarations.get(i).classMemberDeclaration().methodDeclaration();
                        if(method.methodHeader()!=null &&method.methodHeader().methodDeclarator()!=null){
                            String childMethodName=method.methodHeader().methodDeclarator().identifier().getText();
                            if(commonMethods.contains(childMethodName)){

                                if(method.methodModifier().size()==0){
                                    error("error: violacion de la regla 6.1, el metodo "+childMethodName+" es implementado  y no cuenta con anotaciones (Debe contar con la anotacion Override), linea: "+method.getStart().getLine());

                                }
                                else{
                                    boolean areOverride=false;
                                    for(int j=0;j<method.methodModifier().size();j++){
                                        if(method.methodModifier().get(j).annotation()!=null
                                                &&method.methodModifier().get(j).annotation().markerAnnotation()!=null&&
                                                method.methodModifier().get(j).annotation().markerAnnotation().typeName()!=null
                                                &&method.methodModifier().get(j).annotation().markerAnnotation().typeName().identifier()!=null
                                                &&method.methodModifier().get(j).annotation().markerAnnotation().typeName().identifier().getText().equals("Override")){
                                            areOverride=true;


                                        }
                                    }
                                    if(!areOverride){
                                        error("<linea:"+method.getStart().getLine()+"> violacion la regla 6.1, el metodo "+childMethodName+" es heredado y no cuenta con la anotacion @Override");
                                    }

                                }

                            }
                        }
                    }
                }
            }
        }
        return super.visitNormalClassDeclaration(ctx);
    }

    @Override
    public Object visitConstructorDeclaration(Java9Parser.ConstructorDeclarationContext ctx) {
        List<Java9Parser.ConstructorModifierContext> modifier_contexts=ctx.constructorModifier();


        for (int i =0;i<modifier_contexts.size() ;i++){

            if(modifier_contexts.get(i).annotation()!=null && modifier_contexts.get(i).annotation().markerAnnotation()!=null){
                if(modifier_contexts.get(i).annotation().getStart().getLine()==this.lastLineMarkerAnnotation){
                    error("error:violacion de 4.8.5 Annotations, se tienen varias anotaciones en la linea "+modifier_contexts.get(i).annotation().getStart().getLine());
                    //System.exit(-1);
                    break;
                }
                this.lastLineMarkerAnnotation=modifier_contexts.get(i).annotation().getStart().getLine();
            }
        }





        return super.visitConstructorDeclaration(ctx);
    }

    @Override
    public Object visitFieldDeclaration(Java9Parser.FieldDeclarationContext ctx) {


        if(ctx.variableDeclaratorList().variableDeclarator().size()>1){
            error("error: violacion de la regla 4.8.2.1, no se pueden declarar varias variables en un statement, linea: "+ctx.start.getLine());

        }




        /**/
        if(ctx.unannType()!=null && ctx.unannType().unannPrimitiveType()!=null
                && ctx.unannType().unannPrimitiveType().numericType()!=null
                &&ctx.unannType().unannPrimitiveType().numericType().integralType()!=null
                &&ctx.unannType().unannPrimitiveType().numericType().integralType().LONG()!=null){
            if(ctx.variableDeclaratorList()!=null
                    &&ctx.variableDeclaratorList().variableDeclarator()!=null
            ){
                List<Java9Parser.VariableDeclaratorContext>declarations=ctx.variableDeclaratorList().variableDeclarator();
                for(int i=0;i< declarations.size();i++){
                    String assignedValue=declarations.get(i).variableInitializer().getText();
                    int asciiFirstCharacter=(int)assignedValue.charAt(0);
                    if(assignedValue.charAt(assignedValue.length()-1)=='l' && (asciiFirstCharacter>=48 && asciiFirstCharacter<=57) ){
                        error("violacion de la regla 4.8.8, no se puede tener l como sufijo de un valor long en la linea: "+declarations.get(i).variableInitializer().getStart().getLine());
                    }

                }
            }
        }



        /***5.2.4***/
        List<Java9Parser.FieldModifierContext>modifiers=ctx.fieldModifier();
        List<Java9Parser.VariableDeclaratorContext> declarations=ctx.variableDeclaratorList().variableDeclarator();
        if(isConstant(modifiers)){
            for(int i =0;i<declarations.size();i++){
                if(declarations.get(i).variableDeclaratorId()!=null
                        &&declarations.get(i).variableDeclaratorId().identifier()!=null){
                    String identifier=declarations.get(i).variableDeclaratorId().identifier().getText();
                    if(!isUpperCase(identifier)){
                        error("error: violacion de la regla 5.2.4, los nombres de las constantes solo pueden usar mayusculas y underscore, linea "+declarations.get(i).variableDeclaratorId().identifier().getStart().getLine());
                    }

                }
            }
        }



        return super.visitFieldDeclaration(ctx);
    }

    @Override
    public Object visitLocalVariableDeclaration(Java9Parser.LocalVariableDeclarationContext ctx) {

        if(ctx.variableDeclaratorList().variableDeclarator().size()>1){
            error("error: violacion de la regla 4.8.2.1, no se pueden declarar varias variables en un statement, linea: "+ctx.start.getLine());

        }






        /***/

        if(ctx.unannType()!=null && ctx.unannType().unannPrimitiveType()!=null
                && ctx.unannType().unannPrimitiveType().numericType()!=null
                &&ctx.unannType().unannPrimitiveType().numericType().integralType()!=null
                &&ctx.unannType().unannPrimitiveType().numericType().integralType().LONG()!=null){
            if(ctx.variableDeclaratorList()!=null
                    &&ctx.variableDeclaratorList().variableDeclarator()!=null
            ){
                List<Java9Parser.VariableDeclaratorContext>declarations=ctx.variableDeclaratorList().variableDeclarator();
                for(int i=0;i< declarations.size();i++){
                    String assignedValue=declarations.get(i).variableInitializer().getText();
                    int asciiFirstCharacter=(int)assignedValue.charAt(0);
                    if(assignedValue.charAt(assignedValue.length()-1)=='l' && (asciiFirstCharacter>=48 && asciiFirstCharacter<=57) ){
                        error("violacion de la regla 4.8.8, no se puede tener l como sufijo de un valor long en la linea: "+declarations.get(i).variableInitializer().getStart().getLine());
                    }

                }
            }
        }


        return super.visitLocalVariableDeclaration(ctx);
    }
    /**
     * 5.2.3
     *
     * **/
    @Override
    public Object visitMethodDeclarator(Java9Parser.MethodDeclaratorContext ctx) {
        if(ctx.identifier()!=null){
            if(!verifylowerCamelCase(ctx.identifier().getText())){
                error("Violacion de la regla 5.2.3, los nombres de los metodos deben estar en lowerCamelCase sin underscore linea: "+ctx.identifier().getStart().getLine());
            }
        }

        return super.visitMethodDeclarator(ctx);
    }
    /***
     *
     * 5.2.4
     * */


    /**
     * 4.1.1
     *
     * */
    @Override
    public Object visitIfThenStatement(Java9Parser.IfThenStatementContext ctx) {
        if(ctx.statement()!=null
                && ctx.statement().statementWithoutTrailingSubstatement()!=null
                && ctx.statement().statementWithoutTrailingSubstatement().expressionStatement()!=null){
            error("error: violacion de la regla 4.1.1, no se permiten if sin abrir y cerrar los brackets {}, linea:"+ctx.RPAREN().getSymbol().getLine());
        }


        return super.visitIfThenStatement(ctx);
    }

    @Override
    public Object visitIfThenElseStatement(Java9Parser.IfThenElseStatementContext ctx) {

        /*caso en que el if si tiene los brackets*/
        if(ctx.statementNoShortIf()!=null

                &&ctx.statementNoShortIf().statementWithoutTrailingSubstatement()!=null
                &&ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block()!=null
                &&ctx.statementNoShortIf().statementWithoutTrailingSubstatement().block().LBRACE()==null){
            error("error: violacion de la regla 4.1.1, no se permiten if sin abrir y cerrar los brackets {}, linea:"+ctx.RPAREN().getSymbol().getLine());
        }
        /*caso en el que el if no tiene los brackets*/
        if(ctx.statementNoShortIf()!=null

                &&ctx.statementNoShortIf().statementWithoutTrailingSubstatement()!=null
                &&ctx.statementNoShortIf().statementWithoutTrailingSubstatement().expressionStatement()!=null
        ){

            error("error: violacion de la regla 4.1.1, no se permiten if sin abrir y cerrar los brackets {}, linea:"+ctx.RPAREN().getSymbol().getLine());
        }



        if(ctx.statement()!=null
                && ctx.statement().statementWithoutTrailingSubstatement()!=null
                && ctx.statement().statementWithoutTrailingSubstatement().expressionStatement()!=null){
            error("error: violacion de la regla 4.1.1, no se permiten else sin abrir y cerrar los brackets {}, linea:"+ctx.ELSE().getSymbol().getLine());

        }
        return super.visitIfThenElseStatement(ctx);
    }

    @Override
    public Object visitBasicForStatement(Java9Parser.BasicForStatementContext ctx) {
        if(ctx.statement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement().block()==null){
            error("error: violacion de la regla 4.1.1 no se permiten for sin brackets {}, linea: "+ctx.RPAREN().getSymbol().getLine());
        }

        return super.visitBasicForStatement(ctx);
    }

    @Override
    public Object visitEnhancedForStatement(Java9Parser.EnhancedForStatementContext ctx) {
        if(ctx.statement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement().block()==null){
            error("error: violacion de la regla 4.1.1 no se permiten for each sin brackets {}, linea: "+ctx.RPAREN().getSymbol().getLine());
        }



        return super.visitEnhancedForStatement(ctx);
    }

    @Override
    public Object visitDoStatement(Java9Parser.DoStatementContext ctx) {
        if(ctx.statement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement().block()==null){
            error("error: violacion de la regla 4.1.1 , no se permite un do sin brackets {}, linea: "+ctx.DO().getSymbol().getLine());
        }


        return super.visitDoStatement(ctx);
    }

    @Override
    public Object visitWhileStatement(Java9Parser.WhileStatementContext ctx) {
        if(ctx.statement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement()!=null
                &&ctx.statement().statementWithoutTrailingSubstatement().block()==null){
            error("error: violacion de la regla 4.1.1  no se puede tener while sin brackets {}, linea: "+ctx.WHILE().getSymbol().getLine());
        }

        return super.visitWhileStatement(ctx);
    }

    @Override
    public Object visitCatchClause(Java9Parser.CatchClauseContext ctx) {
        if(ctx.block()!=null
                &&ctx.block().blockStatements()==null &&!ctx.catchFormalParameter().variableDeclaratorId().identifier().getText().matches("expected[A-Za-z0-9]*")){
            error("error: violacion de la regla 6.2, no se pueden tener clausulas catch sin ningun statement, linea: "+ctx.CATCH().getSymbol().getLine());
        }



        return super.visitCatchClause(ctx);
    }

    @Override
    public Object visitNormalInterfaceDeclaration(Java9Parser.NormalInterfaceDeclarationContext ctx) {
        ArrayList<String>methods=new ArrayList<>();
        String interfaceName=ctx.identifier().getText();
        if(ctx.interfaceBody()!=null
                &&ctx.interfaceBody().interfaceMemberDeclaration()!=null){
            for (int i=0;i<ctx.interfaceBody().interfaceMemberDeclaration().size();i++){
                if(ctx.interfaceBody().interfaceMemberDeclaration().get(i).interfaceMethodDeclaration()!=null
                        &&ctx.interfaceBody().interfaceMemberDeclaration().get(i).interfaceMethodDeclaration().methodHeader()!=null
                        &&ctx.interfaceBody().interfaceMemberDeclaration().get(i).interfaceMethodDeclaration().methodHeader().methodDeclarator()!=null){
                    String nameMethod=ctx.interfaceBody().interfaceMemberDeclaration().get(i).interfaceMethodDeclaration().methodHeader().methodDeclarator().identifier().getText();
                    ;                   methods.add(nameMethod);
                }
            }
            this.mapClassFunctions.put(interfaceName,methods);

        }




        return super.visitNormalInterfaceDeclaration(ctx);
    }

    @Override
    public Object visitMethodInvocation(Java9Parser.MethodInvocationContext ctx) {
        List<ParseTree> children=ctx.children;
        //System.out.println(".............................");
    /*for(int i =0;i<children.size();i++){
        //System.out.println(children.get(i).getText());
        if(children.get(i).getText().equals(".")){

        }
    }*/
        if(ctx.DOT().size()!=0){
            if(ctx.DOT().get(0).getSymbol().getLine()!=ctx.identifier().getStart().getLine()){
                error("error: violacion de la regla 4.5,el salto de linea debe ir antes del punto, linea: "+ctx.DOT().get(0).getSymbol().getLine());

            }
        }


        return super.visitMethodInvocation(ctx);
    }

    @Override
    public Object visitCatchType(Java9Parser.CatchTypeContext ctx) {
        for(int i=0;i<ctx.BITOR().size();i++){

            if(ctx.BITOR().get(i).getSymbol().getLine()!=ctx.classType().get(i).getStart().getLine()){
                error("error: violacion de la regla 4.5, el salto de linea debe ir antes de | ,linea :"+ctx.BITOR().get(i).getSymbol().getLine());
            }

        }

        return super.visitCatchType(ctx);
    }

    @Override
    public Object visitAdditionalBound(Java9Parser.AdditionalBoundContext ctx) {
        if(ctx.BITAND()!=null&&ctx.interfaceType()!=null){
            if(ctx.BITAND().getSymbol().getLine()!=ctx.interfaceType().getStart().getLine()){
                error("error: violacion de la regla 4.5, el salto de linea ir antes de &, linea :"+ctx.BITAND().getSymbol().getLine());
            }
        }

        return super.visitAdditionalBound(ctx);
    }
}