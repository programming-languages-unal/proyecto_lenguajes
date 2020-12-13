
/*
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




interface Po{
    void dd();
}
*/

interface MyInterface{
    void test();
    void x();
}

class ChildInterface implements MyInterface{


    public void test() {//saca error 6.1 por  no tener override
    }

    /*
    @Override//no saca error
    public void x() {

    }

    //saca error 6.1 pues no tiene override
    public void dd() {

    }
    */
}
