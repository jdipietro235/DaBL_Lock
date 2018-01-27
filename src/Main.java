

public class Main implements CardReadListener{

    public static void main(String[] args) {
        System.out.println("start");

        MySQLAccess mySqlA = new MySQLAccess();

        try {
            mySqlA.readDataBase();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }


        /*
        try {
            SerialReader sr = new SerialReader();

            sr.initialize();
        }
        catch (Exception e){
            System.err.println(e.toString());
        }

        */

    }



    public void onRead(int value){
        System.out.println("placeholder");
    }

}
