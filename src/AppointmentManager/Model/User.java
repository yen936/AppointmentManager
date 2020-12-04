package AppointmentManager.Model;

import AppointmentManager.Main;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class User extends Noun {

    private static User currentUser;
    private int userID;
    private String username;
    private String password;

    /** Constructor for User
     *
     * @param userID int
     * @param username String
     * @param password String
     */
    User(int userID, String username, String password) {
        super(userID, username);
        this.password = password;

    }

    /** Getter for password
     *
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /** Setter for Password
     *
     * @param password String
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** Getter for currentUser
     *
     * @return User
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /** Setter for currentUser
     *
      * @param user User
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }


    /** Attempts to login a user by comparing the inputted user and password to the corresponding one in the DB.
     *
     * @param username String
     * @param password String
     * @return boolean
     */
    public static boolean login(String username, String password) {
        try {

            User dbUser = getUserFromDB(username);
            if (dbUser == null) {
                Alert alert  = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Username");
                alert.setContentText("Invalid Username");
                alert.showAndWait();

                logActivity("Invalid User", "Failure");

                return false;
            }

            else {
                if (dbUser.getPassword().equals(password) && dbUser.getName().equals(username)) {
                    logActivity(username, "Success");
                    setCurrentUser(dbUser);
                    return true;

                } else {
                    logActivity(username, "Failure");
                    return false;
                }
            }

        } catch (NullPointerException e) { return false; }
    }

    /** Logs all attempts to login_activity.txt
     *
     * @param username name of user
     * @param loginSucess String, 'True' or 'False'
     */
    private static void logActivity(String username, String loginSucess) {

        String activity;
        Instant instant = Instant.now();
        Timestamp timestamp = Timestamp.from(instant);

        activity = "\nLogin Attempt: " + timestamp.toString() + " " + username + " - " + loginSucess;

        try {
            // Login Attempt: Datetime username - success
            String logFilePath = Paths.get("").toAbsolutePath().toString() + "\\src\\AppointmentManager\\login_activity.txt";
            Files.write(Paths.get(logFilePath), activity.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /** Searches the DB for a user with name username
     *
     * @param username String
     * @return User
     */
    public static User getUserFromDB(String username) {

        int dbUserID;
        String dbUserName;
        String dbPassword;


        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT * FROM users WHERE User_Name='" + username + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {

                dbUserID = resultSet.getInt("User_ID");
                dbUserName = resultSet.getString("User_Name");
                dbPassword = resultSet.getString("Password");

                return new User(dbUserID, dbUserName, dbPassword);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }


        return null;

    }


}
