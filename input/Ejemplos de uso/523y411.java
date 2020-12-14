
class TestFourthRule{
    void Metjh(){///error 5.2.3 por no estar en lowerCamelCase
        int a=0;
        /**/
        if(a>0)
            System.out.println("ey");//error 4.1.1 no se admiten if sin {}

        /**/
        if(a<0){//no saca error
            System.out.println("ifelse");
        }

        else//saca error
            System.out.println("else");

        /**/
        if(a<0)//saca error
            System.out.println("print");
        else//saca error
            System.out.println("value");

        /**/
        for(int i=0;i<10;i++){//no saca error
            System.out.println("for");
        }

        for(int i=0;i<10;i++)//Saca error 4.1.1 por no tener brackets
            System.out.println("for");

        List<Integer>as=new ArrayList<>();
        for (int i :as){//no saca error
            System.out.println("valor");
        }

        for(int i:as)//saca error 4.1.1
            System.out.println("foeach");

        do{//no saca error
            System.out.println("do");}
        while(a>0);
        do//saca error 4.1.1
            System.out.println("dowhile");
        while(a>0);

        while(a>0){//no saca error
            System.out.println("while");
        }
        while(a>0)//saca error 4.1.1
            System.out.println("whiea");
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
