package AppointmentManager.Model;

import java.sql.*;

import AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Appointment {

    //Instance variables
    private int appointmentID;
    private String title;
    private String discription;
    private String Location;
    private String Type;
    private LocalDateTime Start;
    private LocalDateTime End;
    private int userID;
    private int customerID;
    private String contactName;

    /** Constructor for Appointments
     * @param appointmentID int
     * @param title String
     * @param description String
     * @param Location String
     * @param Type String
     * @param Start LocalDateTime
     * @param End LocalDateTime
     * @param contactName String
     * @param customerID int
     * @param userID int
     */

    public Appointment(int appointmentID, String title, String description, String Location, String Type, LocalDateTime Start, LocalDateTime End, int userID, int customerID, String contactName) {

        this.appointmentID = appointmentID;
        this.title = title;
        this.discription = description;
        this.Location = Location;
        this.Type = Type;
        this.Start = Start;
        this.End = End;
        this.userID = userID;
        this.customerID = customerID;
        this.contactName = contactName;

    }

    /** Getter for ID
     * @return appointmentID */
    public int getID() {
        return appointmentID;
    }

    /** Setter for appointmentID
     * @param appointmentID int
     */
    public void setID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /** Getter for title
     * @return title String*/
    public String getTitle() {
        return title;
    }

    /** Setter for title
     * @param title string
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /** Getter for description
     * @return description String  */
    public String getDescription() {
        return discription;
    }

    /** Setter for description
     * @param description String
     */
    public void setDiscription(String description) {
        this.discription = description;
    }

    /** Getter for location
     * @return location String
     */
    public String getLocation() {
        return Location;
    }

    /** Setter for location
     * @param location string
     */
    public void setLocation(String location) {
        this.Location = location;
    }

    /** Getter for type
     * @return String type
     */
    public String getType() {
        return Type;
    }

    /** Setter for type
     * @param type String
     */
    public void setType(String type) {
        this.Type = type;
    }

    /** Getter for start
     * @return LocalDateTime
     */
    public LocalDateTime getStart() {
        return Start;
    }

    /** Setter for start
     * @param Start LocalDateTime
     */
    public void setStart(LocalDateTime Start) {
        this.Start = Start;
    }

    /** Getter for End
     * @return LocalDateTime
     */
    public LocalDateTime getEnd() {
        return End;
    }

    /** Setter for End
     * @param End LocalDateTime
     */
    public void setEnd(LocalDateTime End) {
        this.End = End;
    }

    /** Getter for userID
     * @return int
     */
    public int getUserID() {
        return userID;
    }

    /** Setter for userID
     *
     * @param userID int
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /** Getter for customerID
     *
     * @return int
     */
    public int getCustomerID() {
        return customerID;
    }

    /** Setter for customerID
     *
     * @param customerID int
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /** Getter for contact name
     * @return String
     */
    public String getContactName() {
        return contactName;
    }



    /** Setter for contact name
     *
     * @param contactName String
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }



    /** Gets an appointment from the database based on the Appointment ID
     * @param appointmentID index for appointments
     * @return Appointment at index
     * @throws SQLException ,
     */
    public static Appointment getAppointmentsDataBase(int appointmentID) throws SQLException {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM appointments, contacts " +
                            "WHERE appointments.Contact_ID = contacts.Contact_ID AND Appointment_ID='" + appointmentID + "'");

            // Result set get the result of the SQL query
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {

//                ResultSetMetaData rsmd = resultSet.getMetaData();
//
//                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//                    String name = rsmd.getColumnName(i);
//                    System.out.println(name);
//                }

                Timestamp dbStart = resultSet.getTimestamp("Start");
                Timestamp dbEnd = resultSet.getTimestamp("End");


                return new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        Util.TimestampToLocal(dbStart),
                        Util.TimestampToLocal(dbEnd),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Contact_Name"));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return null;

    }

    /** Checks if an appointment for logged-in user begins in the next 15 minutes. Displays alerts either way.
     *
     * @return Alert on Appointments in next 15 minutes
     */
    public static Alert get15MinAppointments() {

        ZonedDateTime zTime = ZonedDateTime.now(Main.newZoneId);
        ZonedDateTime now = zTime.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime plus15Min = now.plusMinutes(15);

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM appointments WHERE User_ID='" + User.getCurrentUser().getID() + "'"
                            + " AND Start BETWEEN '" + now + "'" + " AND '" + plus15Min + "'");

            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {

                Appointment a = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        Util.TimestampToLocal(resultSet.getTimestamp("Start")),
                        Util.TimestampToLocal(resultSet.getTimestamp("End")),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Customer_ID"),
                        Util.getContactName(resultSet.getString("Contact_ID")));

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appointment in 15 Minutes");
                alert.setHeaderText("Upcoming in 15 Minutes with: " + a.getContactName());
                alert.setContentText("Upcoming appointment " + a.getTitle() + " in the " + resultSet.getString("Location"));

                return alert;

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        //Invalid credentials
        Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
        if (Main.language.equals("français"))  {
            newAlert.setTitle("Aucun rendez-vous à venir");
            newAlert.setHeaderText("Aucun rendez-vous à venir");
            newAlert.setContentText("Aucun rendez-vous à venir");
        }
        else {
            newAlert.setTitle("No Upcoming Appointments");
            newAlert.setHeaderText("No Upcoming Appointments");
            newAlert.setContentText("No Upcoming Appointments");
        }


        return newAlert;


    }


    /** Gets all appointment for the next week by user
     *
     * @return ObservableList<Appointment> Weekly Appointments
     * @throws SQLException ,
     */
    public static ObservableList<Appointment> getWeeklyAppointments() throws SQLException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus1Week = now.plusWeeks(1);


        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM appointments, contacts WHERE Start BETWEEN '" + Util.localToUTC(now) + "'" + " AND '" + Util.localToUTC(plus1Week) + "'"
                            + " AND appointments.Contact_ID = contacts.Contact_ID");

            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {

                Appointment a = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        Util.TimestampToLocal(resultSet.getTimestamp("Start")),
                        Util.TimestampToLocal(resultSet.getTimestamp("End")),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Contact_Name"));

                appointments.add(a);

            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return appointments;

    }


    /** Gets all appointment for the next month by user
     *
     * @return ObservableList<Appointment> monthly Appointments
     * @throws SQLException ,
     */
    public static ObservableList<Appointment> getMonthlyAppointments() throws SQLException {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus1Month = now.plusMonths(1);


        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM appointments, contacts WHERE Start BETWEEN '" + Util.localToUTC(now) + "'" + " AND '" + Util.localToUTC(plus1Month) + "'"
                            + " AND appointments.Contact_ID = contacts.Contact_ID");

            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {

                Appointment a = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        Util.TimestampToLocal(resultSet.getTimestamp("Start")),
                        Util.TimestampToLocal(resultSet.getTimestamp("End")),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getString("Contact_Name"));

                appointments.add(a);

            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return appointments;

    }

    /**
     * Saves an appointment to the DB, validates that save was successful
     * @param appointment new Appointment
     * @return boolean
     */
    public static boolean saveNewAppointmentToDB(Appointment appointment) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("INSERT INTO appointments SET Title='" + appointment.getTitle() + "'"
                            + ", Description='" + appointment.getDescription() + "'"
                            + ", Location='" + appointment.getLocation() + "'"
                            + ", Type='" + appointment.getType() + "'"
                            + ", Start='" + Util.localToUTC(appointment.getStart()) + "'"
                            + ", End='" + Util.localToUTC(appointment.getEnd()) + "'"
                            + ", Customer_ID='" + appointment.getCustomerID() + "'"
                            + ", Contact_ID='" + Util.getContactID(appointment.getContactName()) + "'"
                            + ", User_ID='" + appointment.getUserID() + "'"
                            + ", Create_Date = NOW()"
                            + ", Created_By='" + User.getCurrentUser().getName() + "'"
                            + ", Last_Update = NOW()"
                            + ", Last_Updated_By='" + User.getCurrentUser().getName() + "'"
                            + ", Appointment_ID='" + (Util.getLastIDAppointment() + 1) + "'");


            int result = preparedSQL.executeUpdate();

            if (result == 1) {
                return true;
            }
        } catch (SQLException throwables) {
            System.out.println("Error: On Save");
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return false;

    }

    /**
     * Updates an appointment in the DB at an index, validates that save was successful
     * @param appointment updated Appointment
     * @param appointmentID appointment ID index for update
     * @return boolean
     */
    public static boolean updateAppointmentInDB(int appointmentID, Appointment appointment) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("UPDATE appointments SET Title='" + appointment.getTitle() + "'"
                            + ", Description='" + appointment.getDescription() + "'"
                            + ", Location='" + appointment.getLocation() + "'"
                            + ", Type='" + appointment.getType() + "'"
                            + ", Start='" + Util.localToUTC(appointment.getStart()) + "'"
                            + ", End='" + Util.localToUTC(appointment.getEnd()) + "'"
                            + ", Customer_ID='" + appointment.getCustomerID() + "'"
                            + ", Contact_ID='" + Util.getContactID(appointment.getContactName()) + "'"
                            + ", User_ID='" + appointment.getUserID() + "'"
                            + ", Last_Update = NOW()"
                            + ", Last_Updated_By='" + User.getCurrentUser().getName() + "'"
                            + " WHERE Appointment_ID='" + appointmentID + "'");

            int result = preparedSQL.executeUpdate();

            if (result == 1) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error: Failure outside of SQL");
            e.printStackTrace();
        }

        return false;

    }


    /**
     * Deletes an appointment from the DB at an index, validates that save was successful
     * @param appointmentID appointment ID index for deletion
     * @return boolean
     */
    public static boolean deleteAppointmentFromDB(int appointmentID) {

        try {

            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("DELETE FROM appointments WHERE Appointment_ID='" + appointmentID + "'");

            int result = preparedSQL.executeUpdate();

            if (result == 1) {
                return true;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return false;

    }


}
