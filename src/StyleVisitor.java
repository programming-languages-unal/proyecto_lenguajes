public class StyleVisitor<T> extends Java9BaseVisitor {

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
            e.printStackTrace();
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
        return null;
    }
}
