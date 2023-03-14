import java.sql.*;
import  java.util.Scanner;

public class Soccer {
    static String LIST = "1";
    static String INSERT = "2";
    static String YOU = "3";
    static String EXIT = "4";
    static Connection con;
    static boolean status = true;

    public static void main(String[] args) throws SQLException{


        // JDBC Connection
       // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2023-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "cs421g30";
        String your_password = "harshGroup30dan.";

        con = DriverManager.getConnection (url,your_userid,your_password) ;
//        Statement statement = con.createStatement ( ) ;

        displayMenu();
        while(status) {

            // First main menu
            Scanner myInput = new Scanner(System.in);
            String optionCode = myInput.nextLine();
            System.out.println(optionCode);
            handleOptionCode(optionCode);
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


    public static void handleOptionCode(String optionCode) throws SQLException {
        switch(optionCode) {
            case "1":
                break;

            case "2":
                break;

            case "3":
                break;

            case "4":
                System.out.println("Exiting App");
                // Close database
                con.close();
                status = false;
                break;
            default:
        }
    }
}
