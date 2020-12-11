import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.ws.Action;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebFault;
import java.util.ArrayList;
import java.util.List;

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
    public void method(){
        long as=10l,
        ad=10L;
    }

}
class TestThirdRule{
    private  final static int NUMBER_CONSTANT=5;//no debe tener problema
    final static long df=8;//violacion de 5.2.4 las constantes deben tener solo mayusculas y undercore
    static int as=0;//no marca problema pues deber ser final y static para ser una constante

}

class TestFourthRule{
    void metjh(){
        int a=0;
        /**/
        if(a>0)
            System.out.println("asasd");//error 4.1.1 no se admiten if sin {}

/**/
        if(a<0){//no saca error
            System.out.println("ifelse");
        }

        else//saca error
            System.out.println("else");

/**/
        if(a<0)//saca error
            System.out.println("filsd");
        else//saca error
            System.out.println("sd");

/**/
        for(int i=0;i<10;i++){//no saca error
            System.out.println("hsdl");
        }

        for(int i=0;i<10;i++)//Saca error 4.1.1 por no tener brackets
            System.out.println("sdsd");

        List<Integer>as=new ArrayList<>();
        for (int i :as){//no saca error
            System.out.println("sdsda");
        }

        for(int i:as)//saca error 4.1.1
            System.out.println("sdsd");

        do{//no saca error
            System.out.println("sda");}
        while(a>0);
        do//saca error 4.1.1
            System.out.println("sda");
        while(a>0);

        while(a>0){//no saca error
            System.out.println("asdafg");
        }
        while(a>0)//saca error 4.1.1
            System.out.println("sdsd");
        /*
        *
        *
        *
        6.2*
        */
        int i = 0;
        try {

            i=6;
        } catch (NumberFormatException ok) {
            i=10;
        }

        try {

            i=6;
        } catch (NumberFormatException ok) {//esto saca error 6.2

        }

        try {

            i=6;
        } catch (NumberFormatException expectedU) {//esto no saca saca error

        }

    }



}
/*
*
* 4.8.2.1 One variable per declaration
* */
class LastRule{
    int s;//no da error
    int k;//no da error
    int a,b;//error 4.8.2.1
    void me(){
        int c;//no da error
        int s;//no da error
        int a,b;//error 4.8.2.1
    }


}



class Parent {
    void greeting(){

    }
    void foo(){}
    void fooA(){}

}
class Child extends Parent{

    @Override//no saca error
    void greeting(){}
    @Deprecated//error 6.1 por falta de override
    void foo(){}

    void fooA(){}//error 6.1 por falta de override
}

interface MyInterface{
    void impl();
    void x();
}
interface Po{
    void dd();
}
class ChildInterface implements MyInterface,Po{


    public void impl() {//saca error 6.1 por  no tener override

    }

    @Override//no saca error
    public void x() {

    }

//saca error 6.1 pues no tiene override
    public void dd() {

    }
}


/*4.5*/
class A{
    void a(){
        Integer.parseInt("sdsd");
    }

}
class Pepe{
    A s=new A();
    void m(){
        s.a();//no saca error
        s.//saca error 4.5
                a();


        try{
            s.a();
        }catch(ArithmeticException |//saca error 4.5
                NullPointerException e){
            System.out.println("catch");
        }catch(IndexOutOfBoundsException//no saca error
                | SecurityException e){
            System.out.println("sd");

        }
    }


}
class P <T extends A &
        MyInterface>{

}
/*Sanchez*/