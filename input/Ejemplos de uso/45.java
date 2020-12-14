
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