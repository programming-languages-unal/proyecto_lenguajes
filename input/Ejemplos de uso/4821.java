
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
