class O {

    final int LOWER = 3;
    String[] args;
    private final static int NUMBER_CONSTANT=5;//no debe tener problema

    void Empty(){}

    int Mars() {  //no debe tener problema

        final int LoleOne = 0;
        int loleTwo = 0;

        switch (LoleOne) {
            case 1:
            case 2:
            case 3:
            default:
        }//
        return LoleOne;
    };

    int Venus() {
        //String args2 [];
        int loleOne = 0;
        if(true){

        } else {

        };
        loleOne++;
        try {

        } catch (Exception e){

        }
        return 0;
    }
}

class TestThirdRule {
    private final static int NUMBER_CONSTANT=5;//no debe tener problema
    final static long df=8;//violacion de 5.2.4 las constantes deben tener solo mayusculas y undercore
    static int as=0;//no marca problema pues deber ser final y static para ser una constante
}