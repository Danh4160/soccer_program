import java.sql.*;
import  java.util.Scanner;

public class Soccer {

    public static void main(String[] args) throws SQLException{


        // JDBC Connection
       // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2023-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = null;
        String your_password = null;

        Connection con = DriverManager.getConnection (url,your_userid,your_password) ;
        Statement statement = con.createStatement ( ) ;

        displayMenu();
        while(true) {

            Scanner myInput = new Scanner(System.in);
            String optionCode = myInput.nextLine();
            System.out.println(optionCode);
        }
    }


    public static void displayMenu() {
        System.out.println("Soccer Main Menu");
        System.out.println("\t 1. List information of matches of a country");
        System.out.println("\t 2. Insert initial player information for a match");
        System.out.println("\t 3. For you to design");
        System.out.println("\t 4. Exit Application");
        System.out.println("Please Enter Your Option:\n");
    }
}
