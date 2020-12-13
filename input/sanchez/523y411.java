
class TestFourthRule{
    void Metjh(){///error 5.2.3 por no estar en lowerCamelCase
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
