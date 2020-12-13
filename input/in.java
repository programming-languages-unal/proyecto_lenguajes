public class Spaghetti {

    void doNothingElse_2();

    void doNothingElse_3(){};

    // This is acceptable
    void doNothing() {
        // This is not acceptable: No concise empty blocks in a multi-block statement
        try {

        } catch (Exception e) {

        }
        catch (Exception e) {

        }
        catch (Exception e) {
            doNothing();
        }

        try {
            doNothing();
        }
        catch (Exception e) {
            doNothing();
        }
        finally {

        }

        if(true){

        } else {

        }

    }

    // This is equally acceptable
    void doNothingElse() {
    }

    public static void main(String[] args) {

    }
}