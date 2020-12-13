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
