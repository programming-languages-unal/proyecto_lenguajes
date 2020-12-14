@WebFault @Deprecated @ServiceMode  //4.8.5 no se acepta que las anotaciones esten en una misma linea
class TestFirstRule{
    @Ignore    @Deprecated//4.8.5 no se acepta que las anotaciones esten en una misma linea
    public TestFirstRule(){
        String[] args;
        String args [];
    }

    @Action     @Deprecated//4.8.5 no se acepta que las anotaciones esten en una misma linea
    public void cantar(){

    }

    @Action  public void myActions(){}

    @Action
    @Deprecated  public void myAction(){}

    @WebFault @Deprecated @ServiceMode
    class Inner{

    }

}
