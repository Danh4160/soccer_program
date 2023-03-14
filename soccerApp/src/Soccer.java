import java.sql.*;
import java.util.Date;
import  java.util.Scanner;
import java.time.LocalDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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


        while(status) {

            // First main menu
            displayMenu();
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

                int count = 0;

                System.out.println("Matches");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT m.matchid,mp.country1,mp.country2,m.date,m.round FROM Match m inner join MatchPlayed mp on m.matchid = mp.matchid where m.date >= CURRENT_DATE AND m.date <=CURRENT_DATE + 3");
                while ( rs.next() ) {
                    String id = rs.getString("MatchID");
                    String country1 = rs.getString("Country1");
                    String country2 = rs.getString("Country2");
                    String date = rs.getString("Date");
                    String round = rs.getString("Round");
                    System.out.println(id + " " +  country1 +" "+ country2 + " " + round +" "+ date );
                }
                rs.close();
                System.out.println("\n");

                //Then, the system should request the user to input the match identier and for which country the insert
                //should be made. There should also be an option to go to cancel the request and go back to the previous
                //menu (e.g., by entering a [P]).
                System.out.println("You will be asked to enter the MatchIdentifier and Country for which you want to view and insert player, if you want to quit enter P\n");
                System.out.println("Enter MatchIdentifier or Exit(P)");
                Scanner mid = new Scanner(System.in);
                String midstring = mid.nextLine();
                if (midstring.equals("P") ||midstring.equals("p") ){
                    break;
                }
                System.out.println("Enter country or Exit(P)");
                Scanner ctry = new Scanner(System.in);
                String country = ctry.nextLine();
                if (country.equals("P") ||country.equals("p") ){
                    break;
                }

                while (count != 11) {
                    System.out.println("The following players from " + country + " are already entered for match " + midstring + "\n");
                    ResultSet rs2 = stmt.executeQuery("Select p.name,mi.shirtnumber,mi.PositionOnField, mi.OnField, mi.OffField,mi.YellowCard,mi.RedCard from player p inner join matchplayerinfo mi on p.country = mi.country and p.shirtnumber=mi.shirtnumber where mi.country='" + country + "' AND mi.matchid =" + midstring);
                    while (rs2.next()) {
                        String name = rs2.getString("Name");
                        String sn = rs2.getString("ShirtNumber");
                        String poField = rs2.getString("PositionOnField");
                        String onF = rs2.getString("OnField");
                        String offF = rs2.getString("OffField");
                        String YC = rs2.getString("YellowCard");
                        String RC = rs2.getString("RedCard");

                        System.out.println(name + " " + sn + " " + poField + " from minute " + onF + " to minute " + offF + " yellow: " + YC + " red: " + RC);
                    }
                    rs2.close();
                    System.out.println("\n");

                    System.out.println("Possible players from " + country + " not yet selected");
                    ResultSet rs3 = stmt.executeQuery("Select p.name,p.shirtnumber,p.GeneralPosition from player p where p.country='" + country + "' and  p.ShirtNumber NOT in (select mid2.ShirtNumber from matchplayerinfo mid2 where mid2.country='" + country + "' AND mid2.matchid=" + midstring + ")");

                    while (rs3.next()) {
                        String name = rs3.getString("Name");
                        String sn = rs3.getString("ShirtNumber");
                        String gPfield = rs3.getString("GeneralPosition");


                        System.out.println(name + " " + sn + " " + gPfield + " ");
                    }
                    rs3.close();
                    System.out.println("\n");
                    ResultSet rs4 = stmt.executeQuery("Select count(mp.shirtnumber) as numplayers from matchplayerinfo mp  where mp.country='" + country + "' AND mp.matchid=" + midstring);
                    while (rs4.next()) {
                        count = rs4.getInt("numplayers");

                    }
                    rs4.close();

                    if (count == 11) {
                        System.out.println("NO INSERTS ALLOWED:Already too many players in this game for this country, you will now return to the main menu");
                        break;
                    }

                    System.out.println("Enter the number of the player you want to insert or [P] to go to the previous menu");

                    Scanner num = new Scanner(System.in);
                    String numberOfPlayer = num.nextLine();


                    if (country.equals("P") || country.equals("p")) {
                        //probably need status used in ofr loop to break out
                        break;
                    }


                    System.out.println("Enter the position on the field the player you want to insert will take or exit with [P] to go to the previous menu");
                    Scanner positionIn = new Scanner(System.in);
                    String inputtedPosition = positionIn.nextLine();

                    stmt.executeUpdate("INSERT INTO MatchPlayerInfo(Matchid,Country,ShirtNumber,OnField,Offfield,PositionOnField,YellowCard,RedCard) VALUES (" + midstring + ",'" + country + "'," + numberOfPlayer + ",0,0,'" + inputtedPosition + "',0,0)");

                }

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
