import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.xml.ws.Action;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebFault;

class OuterClass {
    private static String msg = "GeeksForGeeks";

    // Static nested class
    public static class NestedStaticClass {

        // Only static members of Outer class
        // is directly accessible in nested
        // static class
        public void printMessage()
        {

            // Try making 'message' a non-static
            // variable, there will be compiler error
            System.out.println(
                    "Message from nested static class: "
                            + msg);
        }
    }

    // Non-static nested class -
    // also called Inner class
    public class InnerClass {

        // Both static and non-static members
        // of Outer class are accessible in
        // this Inner class
        public void display()
        {
            System.out.println(
                    "Message from non-static nested class: "
                            + msg);
        }
    }
}
/*Sanchez*/
@WebFault @Deprecated @ServiceMode  //4.8.5 no se acepta que las anotaciones esten en una misma linea
class TestFirstRule{
    @Ignore    @Deprecated//4.8.5 no se acepta que las anotaciones esten en una misma linea
    public TestFirstRule(){

    }

    @Action     @Deprecated//4.8.5 no se acepta que las anotaciones esten en una misma linea
    public void cantar(){

    }
    @Action
    @Deprecated  public void myAction(){}

    @WebFault @Deprecated @ServiceMode
    class Inner{

    }

}

class TestSecondRule{
    long l=10l;
    private long a=300000l,//4.8.8 no se puede poner l en declaracion de long
            b=6000l,
            c=l,//este caso es aceptado sin problema
            d=5986L;//este caso es aceptado sin problema

}
/*Sanchez*/