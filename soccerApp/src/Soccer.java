import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import  java.util.Scanner;

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
        String your_userid = "cs421g30";
        String your_password = "harshGroup30dan.";

        con = DriverManager.getConnection (url,your_userid,your_password) ;

        while(status) {
            // First main menu
            displayMenu();
            myInput = new Scanner(System.in);
            String optionCode = myInput.nextLine();
//            System.out.println(optionCode);
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
                break;

            case "3":
                while (true) {
//                    System.out.println("List of each match gross revenue");
                    listMatchRevenue();
                    System.out.println("\nEnter [P] to go to previous menu:");
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
                    """
                    select country1, country2, revenue, stadium.name, location, seatsSold, capacity, date, round, score from match
                    inner join matchplayed
                    on match.matchid = matchplayed.matchid
                    inner join (select  matchid, sum(price) as revenue, count(ticketid) as seatsSold from ticket
                                group by matchid) as table2
                    on match.matchid = table2.matchid
                    inner join stadium
                    on match.name = stadium.name
                    order by revenue DESC                         
                    """;
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
            String sqlString =
                    """
                    with table1 as (
                        select MatchPlayed.matchid, MatchPlayed.country1, MatchPlayed.country2, Date, Match.Round, MatchPlayed.Score from Match
                        inner join MatchPlayed
                        on Match.matchid = MatchPlayed.matchid
                        where MatchPlayed.country1 = ? or MatchPlayed.country2 = ?
                        )
                    select country1, country2, Date, Round, Score, seatsSold from table1
                    inner join (select Ticket.matchid, count(Ticket.ticketid) as seatsSold from Ticket
                    group by Ticket.matchid) as table2
                    on table1.matchid = table2.matchid
                    """;
            PreparedStatement stmt = con.prepareStatement(sqlString);
            stmt.setString(1, countryName);
            stmt.setString(2, countryName);
            ResultSet res = stmt.executeQuery();
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
