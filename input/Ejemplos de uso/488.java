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
