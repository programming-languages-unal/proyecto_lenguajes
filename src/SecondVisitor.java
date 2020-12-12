public class SecondVisitor <T> extends Java9BaseVisitor {

    void error(String error) {
        try {
            System.out.println(error);
            throw new IllegalAccessException("Error!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object visitNormalClassDeclaration(Java9Parser.NormalClassDeclarationContext ctx) {
        if (ctx.identifier() != null) {
            String identifierText = ctx.identifier().getText();
            System.out.println(identifierText);

        }
        return null;
    }
}
