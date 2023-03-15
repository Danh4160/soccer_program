import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.Date;
import java.time.LocalDate;
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
    static Scanner myInput;

    public static void main(String[] args) throws SQLException{


        // JDBC Connection
       // Register the driver.  You must register the driver before you can use it.
        try { DriverManager.registerDriver ( new com.ibm.db2.jcc.DB2Driver() ) ; }
        catch (Exception cnfe){ System.out.println("Class not found"); }

        // This is the url you must use for DB2.
        //Note: This url may not valid now ! Check for the correct year and semester and server name.
        String url = "jdbc:db2://winter2023-comp421.cs.mcgill.ca:50000/cs421";

        //REMEMBER to remove your user id and password before submitting your code!!
        String your_userid = "";
        String your_password = "";

        con = DriverManager.getConnection (url,your_userid,your_password) ;

        while(status) {
            // First main menu
            displayMenu();
            myInput = new Scanner(System.in);
            String optionCode = myInput.nextLine();
            handleOptionCode(optionCode);
        }
    }

    public static void displayMenu() {
        System.out.println("Soccer Main Menu");
        System.out.println("\t 1. List information of matches of a country");
        System.out.println("\t 2. Insert initial player information for a match");
        System.out.println("\t 3. List each match revenue");
        System.out.println("\t 4. Exit Application");
        System.out.println("Please Enter Your Option:");
    }


    public static void handleOptionCode(String optionCode) throws SQLException {
        switch(optionCode) {
            case "1":
                while (true) {
                    System.out.println("Enter a country name:");
                    String countryName = myInput.nextLine();
                    listCountryMatchInfo(countryName);
                    System.out.println("\nEnter [A] to find matches of another country, [P] to go to previous menu:");
                    if (myInput.nextLine().compareTo("P") == 0) break;
                }
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
                    ResultSet rs4 = stmt.executeQuery("Select count(mp.shirtnumber) as numplayers from matchplayerinfo mp  where mp.country='" + country + "' AND mp.matchid=" + midstring + ";");
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

                    if (numberOfPlayer.equals("P") || numberOfPlayer.equals("p")) {
                            //probably need status used in ofr loop to break out
                            break;
                    }

                    System.out.println("Enter the position on the field the player you want to insert will take or exit with [P] to go to the previous menu");
                    Scanner positionIn = new Scanner(System.in);
                    String inputtedPosition = positionIn.nextLine();
                    if (inputtedPosition.equals("P") || inputtedPosition.equals("p")) {
                        //probably need status used in ofr loop to break out
                        break;
                    }
                    stmt.executeUpdate("INSERT INTO MatchPlayerInfo(Matchid,Country,ShirtNumber,OnField,Offfield,PositionOnField,YellowCard,RedCard) VALUES (" + midstring + ",'" + country + "'," + numberOfPlayer + ",0,0,'" + inputtedPosition + "',0,0)");
                }
                break;

            case "3":
                while (true) {
//                    System.out.println("List of each match gross revenue");
                    listMatchRevenue();
                    System.out.println("\nEnter [A] to list again or Enter [P] to go to previous menu:");
                    if (myInput.nextLine().compareTo("P") == 0) break;
                }
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
    public static void listMatchRevenue() throws SQLException {
        System.out.println("Team 1 | Team 2 | Revenue ($CAD) | Seats Sold | Max Capacity  | Stadium | Location | Date | Round | Score");
        try {
            String sqlString =
		    "SELECT country1, country2, coalesce(revenue, 0) as revenue, stadium.name, location, coalesce(seatsSold, 0) as seatsSold, capacity, date, round, score " +
		    "FROM match " +
		    "INNER JOIN matchplayed " + 
                    "ON match.matchid = matchplayed.matchid " +
		    "LEFT JOIN (SELECT  matchid, sum(price) AS revenue, COUNT(ticketid) AS seatsSold FROM ticket GROUP BY matchid) AS table2 " +
                    "ON match.matchid = table2.matchid " +
		    "INNER JOIN stadium ON match.name = stadium.name " +
		    "ORDER BY revenue DESC";
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery(sqlString);
            while (res.next()) {
                String country1 = res.getString("COUNTRY1");
                String country2 = res.getString("COUNTRY2");
                int revenue = res.getInt("REVENUE");
                String stadiumName = res.getString("NAME");
                String stadiumLocation = res.getString("LOCATION");
                int seats = res.getInt("SEATSSOLD");
                int capacity = res.getInt("Capacity");
                Date date = res.getDate("DATE");
                String group = res.getString("ROUND");
                String score = res.getString("SCORE");
                System.out.println(country1 + "\t" + country2 + "\t" + revenue + "\t" + stadiumName +
                        "\t" + stadiumLocation + "\t" + seats + "\t" + capacity + "\t" + date +
                        group + "\t" + score );
            }
        } catch (SQLException e) {
            printError(e);
        }

    }
    public static void listCountryMatchInfo(String countryName) throws SQLException {
        System.out.println("Team 1 | Team 2 | Date of Match | Round | Score | Seats Sold");
        try {
            String sqlString = "WITH table1 AS (SELECT MatchPlayed.matchid, MatchPlayed.country1, MatchPlayed.country2, Date, Match.Round, MatchPlayed.Score " + 
		    "FROM Match " + 
		    "INNER JOIN MatchPlayed " +
		    "ON Match.matchid = MatchPlayed.matchid WHERE MatchPlayed.country1 = '" + countryName + "' " + 
		    "OR MatchPlayed.country2 = '" + countryName + "') " + 
                    "SELECT country1, country2, Date, Round, Score, coalesce(seatsSold, 0) as seatsSold " +
		    "FROM table1 " + 
		    "LEFT JOIN (" + "SELECT Ticket.matchid, COUNT(Ticket.ticketid) AS seatsSold " + "FROM Ticket " +
			"GROUP BY Ticket.matchid) AS table2 " +
			"ON table1.matchid = table2.matchid";
            Statement stmt = con.createStatement();
	    ResultSet res = stmt.executeQuery(sqlString);
            while (res.next()) {
                String country1 = res.getString("COUNTRY1");
                String country2 = res.getString("COUNTRY2");
                Date date = res.getDate("DATE");
                String group = res.getString("ROUND");
                String score = res.getString("SCORE");
                int seats = res.getInt("SEATSSOLD");
                System.out.println(country1 + "\t" + country2 + "\t" + date + "\t" +
                        group + "\t" + score + "\t" + seats);
            }

        } catch (SQLException e) {
            printError(e);
        }
    }

    public static void printError(SQLException e) {
        System.err.println("msg: " + e.getMessage() + "code: " + e.getErrorCode() + "state: " + e.getSQLState());
    }
}
