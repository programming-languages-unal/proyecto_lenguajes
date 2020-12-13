import java.util.List;

public class StyleVisitor<T> extends Java9BaseVisitor {

    @Override
    public Object visitOrdinaryCompilation(Java9Parser.OrdinaryCompilationContext ctx) {
        if (ctx.importDeclaration() != null) {
            List<Java9Parser.ImportDeclarationContext> listDeclarations = ctx.importDeclaration();
            boolean static_import = true;
            String error = "Los import tienen el siguiente orden:\n" +
                    "1.Todos los import estaticos en un solo bloque.\n" +
                    "2.Todos los import no estaticos en un solo bloque.\n";
            for (Java9Parser.ImportDeclarationContext ctxDeclaration : listDeclarations) {
                if (ctxDeclaration.singleStaticImportDeclaration() != null) {
                    if (!static_import) {
                        error("<linea:" + ctxDeclaration.singleStaticImportDeclaration().start.getLine() + "> violacion de la regla 3.3.3, " + error);
                    }
                } else if (ctxDeclaration.staticImportOnDemandDeclaration() != null) {
                    if (!static_import) {
                        error("<linea:" + ctxDeclaration.staticImportOnDemandDeclaration().start.getLine() + "> violacion de la regla 3.3.3, " + error);
                    }
                } else if (!static_import && ctxDeclaration.singleTypeImportDeclaration() != null || ctxDeclaration.typeImportOnDemandDeclaration() != null) {
                    static_import = false;
                }
            }
        }
        return super.visitOrdinaryCompilation(ctx);
    }

    boolean verifyvar(String identifierText) {
        int first = (int) identifierText.charAt(0);
        if (!(first >= 65 && first <= 90))
            return false;
        int n = identifierText.length();
        for (int i = 1; i < n; i++) {
            int asciiChar = (int) identifierText.charAt(i);
            if (!((asciiChar >= 65 && asciiChar <= 90) || (asciiChar >= 97 && asciiChar <= 122)))
                return false;
        }
        return true;
    }

    void error(String error) {
        try {
            System.out.println(error);
            throw new IllegalAccessException("Error!");
        } catch (IllegalAccessException e) {

        }
    }

    @Override
    public Object visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {
        if (ctx.identifier() != null) {
            String identifierText = ctx.identifier().getText();
            if (!verifyvar(identifierText)) {
                error("<linea:" + ctx.identifier().start.getLine() + "> violacion de la regla 5.2.2, las clases deben ser escritas en UpperCamelCase");
            }
        }
        return super.visitNormalClassDeclaration(ctx);
    }


    String visitLiteralPrint(int line, String normal, String unicode, String octal) {
        return "<linea:" + line + "> violacion de la regla 2.3.2, debe ingresar la secuencia de escape especial: " + normal + ", en vez la la secuencia de escape unicode: " + unicode + ", o la secuencia de escape octal: " + octal;
    }

    @Override
    public Object visitLiteral(Java9Parser.LiteralContext ctx) {
        if (ctx.CharacterLiteral() != null) {
            String chLiteral = ctx.CharacterLiteral().getText();

            //String octal = Integer.toOctalString('\u0009');
            //String unicode = "\\u" + Integer.toHexString('\011' | 0x10000).substring(1);
            //char oct_ch = '\011';
            //char octh = '\u0009';

            int line = ctx.CharacterLiteral().getSymbol().getLine();

            if (chLiteral.equals("'\\10'") || chLiteral.equals("'\\u0008'")) {// \b
                error(visitLiteralPrint(line, "'\\b'", "'\\u0008'", "'\\10'"));
            } else if (chLiteral.equals("'\\u0009'") || chLiteral.equals("'\\011'")) {// \t
                error(visitLiteralPrint(line, "'\\t'", "'\\u0009'", "'\\011'"));
            } else if (chLiteral.equals("'\\12'") || chLiteral.equals("'\\u000a'")) {// \n
                error(visitLiteralPrint(line, "'\\n'", "'\\u000a'", "'\\12'"));
            } else if (chLiteral.equals("'\\14'") || chLiteral.equals("'\\u000c'")) {// \f
                error(visitLiteralPrint(line, "'\\f'", "'\\u000c'", "'\\14'"));
            } else if (chLiteral.equals("'\\15'") || chLiteral.equals("'\\u000d'")) {// \r
                error(visitLiteralPrint(line, "'\\r'", "'\\u000d'", "'\\15'"));
            } else if (chLiteral.equals(("'\\42'")) || chLiteral.equals("'\\u0022'")) {// \"
                error(visitLiteralPrint(line, "'\\\"'", "'\\u0022'", "'\\42'"));
            } else if (chLiteral.equals("'\\47'") || chLiteral.equals("'\\u0027'")) {// \'
                error(visitLiteralPrint(line, "'\\''", "'\\u0027'", "'\\47'"));
            } else if (chLiteral.equals("'\\134'") || chLiteral.equals("'\\u005c'")) {
                error(visitLiteralPrint(line, "'\\\\'", "'\\u005c'", "'\\134'"));
            }
        }
        return super.visitLiteral(ctx);
    }

    @Override
    public Object visitLocalVariableDeclaration(Java9Parser.LocalVariableDeclarationContext ctx) {
        //System.out.println("visitLocalVariableDeclaration ");
        return super.visitLocalVariableDeclaration(ctx);
    }

    @Override
    public Object visitMethodDeclaration(Java9Parser.MethodDeclarationContext ctx) {
        if (ctx.methodBody() != null) {
            Java9Parser.MethodBodyContext ctxMethodBody = ctx.methodBody();

            if (ctxMethodBody.block() == null) {

                //error("<linea:"+method.getStart().getLine()+"> violacion de la regla 6.1, el metodo "+childMethodName+" es heredado y no cuenta con la anotacion @Override");
                error("<linea:" + ctxMethodBody.getStart().getLine() + "> violacion de la regla 4.1.3. La declaracion de los metodos tiene que tener un bloque, puede ser vacio en algunos casos o con contenido");
            }
        }
        return super.visitMethodDeclaration(ctx);
    }

    String emptyBlocks(int line) {
        return "<linea:" + line + "> violacion de la regla 4.1.3. No deben haber bloques vacios en un sentencia de bloques multiples";
    }

    @Override
    public Object visitTryStatement(Java9Parser.TryStatementContext ctx) {
        if (ctx.block() != null) {
            Java9Parser.BlockContext ctxBlock = ctx.block();
            if (ctxBlock.blockStatements() == null) {
                error(emptyBlocks(ctx.block().start.getLine()) + ", los bloques try no pueden estar vacios");
            }
        }
        /*
        if (ctx.catches() != null) {
            List<Java9Parser.CatchClauseContext> ctxCatchClauses = ctx.catches().catchClause();
            for (Java9Parser.CatchClauseContext ctxCl : ctxCatchClauses) {
                if (ctxCl.block() != null) {
                    Java9Parser.BlockContext ctxBlock = ctxCl.block();
                    if (ctxBlock.blockStatements() == null) {
                        error(emptyBlocks() + ", los bloques catch no pueden estar vacios");
                    }
                }
            }
        }
        */
        if (ctx.finally_() != null) {
            if (ctx.finally_().block() != null) {
                Java9Parser.BlockStatementsContext ctxBlockStatements = ctx.finally_().block().blockStatements();
                if (ctxBlockStatements == null) {
                    error(emptyBlocks(ctx.finally_().block().start.getLine()) + ", los bloques finally no pueden estar vacios");
                }
            }
        }

        return super.visitTryStatement(ctx);
    }
}
