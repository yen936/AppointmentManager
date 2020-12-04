package AppointmentManager.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;

public class DBConnect {

    public static Connection conn;

    private static final String userName = "U07T76";
    private static final String password = "53689123270";
    private static final String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ07T76";


    /**
     * Opens connection to database based on following info:
     * Server name: wgudb.ucertify.com
     * Port: 3306
     * Database name: WJ07T76
     * Username: U07T76
     * Password: 53689123270
     */
    public static void startConnection() {
        System.out.println("Attempting to Connect to DB");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connection established");
        }

        catch (ClassNotFoundException e) {
            System.out.println("Class not found check the jbdc driver");
            e.printStackTrace();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /** Gets the Connection Details
     *
     * @return Connection
     * @throws SQLException ,
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }


    /** Closes the DB connection
     *
     */
    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Database connection terminated");
        }

        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Database connection failed to terminate");
        }

    }


    /** Resets the database back with the default data, as listed in C195newDB.sql */
    public static void resetDatabase() {

        String s;
        StringBuilder sb = new StringBuilder();

        try {
            String sqlFilePath = Paths.get("").toAbsolutePath().toString() + "\\src\\AppointmentManager\\C195newDB.sql";
            FileReader fr = new FileReader(new File(sqlFilePath));


            BufferedReader br = new BufferedReader(fr);

            while((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            String[] inst = sb.toString().split(";");

            Connection c = DBConnect.getConnection();
            Statement st = c.createStatement();

            for (String value : inst) {
                if (!value.trim().equals("")) {
                    st.executeUpdate(value);
                    System.out.println(">>" + value);
                }
            }

        }
        catch(Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }

    }



}




