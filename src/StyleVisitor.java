import java.util.List;

public class StyleVisitor<T> extends Java9BaseVisitor {

    @Override
    public Object visitOrdinaryCompilation(Java9Parser.OrdinaryCompilationContext ctx) {
        if (ctx.importDeclaration() != null) {
            List<Java9Parser.ImportDeclarationContext> listDeclarations = ctx.importDeclaration();
            boolean static_import = true;
            String error = "Los import tienen el siguiente orden:\n" +
                    "1.Todos los import estaticos en un solo bloque.\n" +
                    "2.Todo los import no estaticos en un solo bloque.\n";
            for (Java9Parser.ImportDeclarationContext ctxDeclaration : listDeclarations) {
                if (ctxDeclaration.singleStaticImportDeclaration() != null || ctxDeclaration.staticImportOnDemandDeclaration() != null) {
                    if (!static_import) {
                        error(error);
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
            if (!((asciiChar >= 65 && asciiChar <= 90) || (asciiChar >= 97 && asciiChar <= 122) || asciiChar == 95))
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
                error("Las clases deben ser escritas en UpperCamelCase");
            }

        }

        return super.visitNormalClassDeclaration(ctx);
    }


    public String visitLiteralPrint(String normal, String unicode, String octal) {
        return "Debe ingresar la secuencia de escape especial: " + normal + ", en vez la la secuencia de escape unicode: " + unicode + ", o la secuencia de escape octal: " + octal;
    }

    @Override
    public Object visitLiteral(Java9Parser.LiteralContext ctx) {
        if (ctx.CharacterLiteral() != null) {
            String chLiteral = ctx.CharacterLiteral().getText();

            char s = '∞';
            String octal = Integer.toOctalString('∞');
            String unicode = "\\u" + Integer.toHexString('∞' | 0x10000).substring(1);
            char oct_ch = '\134';

            if (chLiteral.equals("'\\10'") || chLiteral.equals("'\\u0008'")) {// \b
                error(visitLiteralPrint("'\\b'", "'\\u0008'", "'\\10'"));
            } else if (chLiteral.equals("'\\u0009'") || chLiteral.equals("'\\011'")) {// \t
                error(visitLiteralPrint("'\\t'", "'\\u0009'", "'\\011'"));
            } else if (chLiteral.equals("'\\12'") || chLiteral.equals("'\\u000a'")) {// \n
                error(visitLiteralPrint("'\\n'", "'\\u000a'", "'\\12'"));
            } else if (chLiteral.equals("'\\14'") || chLiteral.equals("'\\u000c'")) {// \f
                error(visitLiteralPrint("'\\f'", "'\\u000c'", "'\\14'"));
            } else if (chLiteral.equals("'\\15'") || chLiteral.equals("'\\u000d'")) {// \r
                error(visitLiteralPrint("'\\r'", "'\\u000d'", "'\\15'"));
            } else if (chLiteral.equals(("'\\42'")) || chLiteral.equals("'\\u0022'")) {// \"
                error(visitLiteralPrint("'\\\"'", "'\\u0022'", "'\\42'"));
            } else if (chLiteral.equals("'\\47'") || chLiteral.equals("'\\u0027'")) {// \'
                error(visitLiteralPrint("'\\''", "'\\u0027'", "'\\47'"));
            } else if (chLiteral.equals("'\\134'") || chLiteral.equals("'\\u005c'")) {
                error(visitLiteralPrint("'\\\\'", "'\\u005c'", "'\\134'"));
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
    public Object visitInterfaceMethodDeclaration(Java9Parser.InterfaceMethodDeclarationContext ctx) {
        return super.visitInterfaceMethodDeclaration(ctx);
    }
}
