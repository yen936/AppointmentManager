package AppointmentManager.Model;

import AppointmentManager.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

public class Util {

    /**
     * Determines if string is numeric. Trys to parse a string as a double, thus determining if string is numeric.
     *
     * @param strNum string that may contain a numeric value
     * @return true is if input string is numeric
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static boolean validatePhoneNumber(String phoneNumber) {
        // validate phone numbers of format "1234567890"
        if (phoneNumber.matches("\\d{10}"))
            return true;

            // validating phone number with -, . or spaces
        else if (phoneNumber.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;

            // validating UK phone numbers
        else if (phoneNumber.matches("\\d{2}[-\\.\\s]\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
            return true;

            // validating phone number where area code is in braces ()
        else if (phoneNumber.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
            return true;

            // other phone format
        else if (phoneNumber.matches("\\(\\d{4}\\)-\\d{3}-\\d{3}"))
            return true;

            // return false if nothing matches the input
        else
            return false;

    }

    /**
     * Gets all associated divisions for a given country_ID.
     *
     * @param countryID foreign key used to index
     * @return all of the divisions in an ObservableList<String>
     */
    public static ObservableList<String> getDivisions(int countryID) {

        ObservableList<String> divisions = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT Division FROM first_level_divisions WHERE COUNTRY_ID = '" + countryID + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {
                divisions.add(resultSet.getString("Division"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return divisions;

    }


    /**
     * Gets country for a given division_name.
     *
     * @param division_name foreign key used to index
     * @return country in a String
     */
    public static String getCountry(String division_name) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT countries.Country FROM countries " +
                            "INNER JOIN first_level_divisions " +
                            "ON countries.COUNTRY_ID = first_level_divisions.COUNTRY_ID" +
                            " WHERE first_level_divisions.Division = '" + division_name + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("countries.Country");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return null;

    }

    /**
     * Gets Division_ID for division_name from database
     *
     * @param Division division name to be converted ot division ID
     * @return Int division id
     */
    public static int getDivisionID(String Division) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Division_ID FROM first_level_divisions WHERE Division = '" + Division + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("Division_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return 0;
    }

    /**
     * Gets division name for a given division_ID.
     *
     * @param divisionID foreign key used to index
     * @return division name in a String
     */
    public static String getDivisionName(int divisionID) {
        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT Division FROM first_level_divisions WHERE Division_ID = '" + divisionID + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("Division");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return null;
    }

    /**
     * Gets Customer_ID for Customer_Name from database
     *
     * @param Customer_Name customer name to be converted ot customer ID
     * @return String customer_Name
     */
    public static int getCustomerID(String Customer_Name) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Customer_ID FROM customers WHERE Customer_Name = '" + Customer_Name + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("Customer_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return 0;
    }

    /**
     * Gets customer name for a given customer_ID from the database.
     *
     * @param customerID foreign key used to index
     * @return customer name in a String
     */
    public static String getCustomerName(int customerID) {
        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT Customer_Name FROM customers WHERE Customer_ID = '" + customerID + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("Customer_Name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return null;
    }


    /**
     * Gets the highest customer_ID for listed customers.
     *
     * @return highest customer_id in the form of an int
     */
    public static int getLastIDCustomers() {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT MAX(Customer_ID) FROM customers");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("MAX(Customer_ID)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return 0;

    }


    /**
     * Gets all customer IDs from database
     *
     * @return ObservableList<Integer> with Customer_IDs
     */
    public static ObservableList<Integer> getAllCustomersDataBase() {
        ObservableList<Integer> customers = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Customer_ID FROM customers");
            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {
                customers.add(resultSet.getInt("Customer_ID"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return customers;

    }


    /**
     * Gets the highest Appointment_ID for listed appointments.
     *
     * @return highest Appointment_ID in the form of an int
     */
    public static int getLastIDAppointment() {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT MAX(Appointment_ID) FROM appointments");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("MAX(Appointment_ID)");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return 0;

    }


    /**
     * Gets all contact names from database
     *
     * @return ObservableList<String> with Contact_Names
     */
    public static ObservableList<String> getAllContactsDataBase() {
        ObservableList<String> contacts = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Contact_Name FROM contacts");
            ResultSet resultSet = preparedSQL.executeQuery();

            while (resultSet.next()) {
                contacts.add(resultSet.getString("Contact_Name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return contacts;

    }

    /**
     * Gets contact_ID for contact_Name from database
     *
     * @param contactName String
     * @return String contact_ID
     */
    public static int getContactID(String contactName) {

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Contact_ID FROM contacts WHERE Contact_Name = '" + contactName + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("Contact_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return 0;
    }


    /**
     * Gets contact_name for contact_ID from database
     *
     * @param contactID String
     * @return String contact_name
     */
    public static String getContactName(String contactID) {
        String contactName = "";
        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT Contact_Name FROM contacts WHERE Contact_ID = '" + contactID + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                contactName = resultSet.getString("Contact_Name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        return contactName;
    }


    /**
     * Converts Timestamp to local time in LocalDateTime format
     *
     * @param ts TimeStamp object
     * @return LocalDateTime
     */
    public static LocalDateTime TimestampToLocal(Timestamp ts) {
        LocalDateTime ldt = ts.toLocalDateTime();
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return zdt.toLocalDateTime();
    }

    /**
     * Converts LocalDateTime to UTC in TimeStamp format
     *
     * @param ldt LocalDateTime object
     * @return Timestamp
     */
    public static Timestamp localToUTC(LocalDateTime ldt) {
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        ZonedDateTime utczdt = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtIn = utczdt.toLocalDateTime();
        return Timestamp.valueOf(ldtIn);

    }


    /**
     * Checks if an appointment inside of business hours defined as 8:00 a.m. to 10:00 p.m. EST, including weekends
     * @param time time to check
     * @return boolean
     */
    public static boolean insideOfBusinessHours(LocalDateTime time) {
        ZonedDateTime zdt = time.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtEST = zdt.withZoneSameInstant(ZoneId.of("America/New_York"));
        LocalDateTime ldtEST = zdtEST.toLocalDateTime();

        LocalDateTime openingComparison = LocalDateTime.of(ldtEST.getYear(), ldtEST.getMonth(), ldtEST.getDayOfMonth(), 8, 0);
        LocalDateTime closingComparison = LocalDateTime.of(ldtEST.getYear(), ldtEST.getMonth(), ldtEST.getDayOfMonth(), 22, 0);

        return ldtEST.isAfter(openingComparison) && ldtEST.isBefore(closingComparison);
    }


    /**
     * Checks if the proposed appointment overlaps with an existing one for that customer
     *
     * @param proposedAppointment Appointment proposed to be saved
     * @return boolean
     */
    public static boolean hasOverlap(Appointment proposedAppointment) throws SQLException {

        LocalDateTime start = proposedAppointment.getStart();
        LocalDateTime end = proposedAppointment.getEnd();

//        PreparedStatement preparedSQL = DBConnect.getConnection().
//                prepareCall("SELECT * FROM appointments WHERE Customer_ID='" + proposedAppointment.getCustomerID()
//                        + "' AND '" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(start) + "' <= End AND '" + Util.localToUTC(end) + "' >= End"
//                        + " OR '" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' <= Start AND '" + Util.localToUTC(end) + "' <= End"
//                        + " OR '" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(end) + "' <= End"
//                        + " OR '" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' >= End"
//                        + " AND  Appointment_ID <>'" + proposedAppointment.getID() + "'");

//        PreparedStatement preparedSQL = DBConnect.getConnection().
//                prepareCall("SELECT * FROM appointments WHERE Customer_ID='" + proposedAppointment.getCustomerID()
//                        + "' AND '" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(start) + "' <= End AND '" + Util.localToUTC(end) + "' >= End"
//                        + " OR '" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' <= Start AND '" + Util.localToUTC(end) + "' <= End"
//                        + " OR '" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(end) + "' <= End"
//                        + " OR '" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' >= End"
//                        + " AND  Appointment_ID <>'" + proposedAppointment.getID() + "'");

        PreparedStatement preparedSQL = DBConnect.getConnection().
                prepareCall("SELECT * FROM appointments WHERE (('" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(start) + "' <= End AND '" + Util.localToUTC(end) + "' >= End)"
                        + " OR ('" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' >= Start AND '" + Util.localToUTC(end) + "' <= End)"
                        + " OR ('" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(end) + "' <= End)"
                        + " OR ('" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' >= End))");

//        PreparedStatement preparedSQL = DBConnect.getConnection().
//                prepareCall("SELECT * FROM appointments WHERE (('" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(start) + "' <= End AND '" + Util.localToUTC(end) + "' >= End)"
//                        + " OR ('" + Util.localToUTC(start) + "' <= Start AND '" + Util.localToUTC(end) + "' <= Start AND '" + Util.localToUTC(end) + "' <= End)"
//                        + " OR ('" + Util.localToUTC(start) + "' >= Start AND '" + Util.localToUTC(end) + "' <= End))"
//                        + " AND Appointment_ID<>'" + proposedAppointment.getID() + "'");


        System.out.println(preparedSQL);
        ResultSet resultSet = preparedSQL.executeQuery();
        boolean returnValue = false;


        while (resultSet.next()) {

            if (resultSet.getString("Customer_ID").equals(String.valueOf(proposedAppointment.getCustomerID())) &&
                    !resultSet.getString("Appointment_ID").equals(String.valueOf(proposedAppointment.getID()))) {

                System.out.println("Overlapping ApptID: " + resultSet.getString("Appointment_ID"));
                System.out.println("Overlapping Title: " + resultSet.getString("Title"));
                System.out.println("Overlapping Customer_ID: " + resultSet.getString("Customer_ID"));
                System.out.println("Overlapping Start: " + Util.TimestampToLocal(resultSet.getTimestamp("Start")));
                System.out.println("Overlapping End: " + Util.TimestampToLocal(resultSet.getTimestamp("End")));

                returnValue = true;
            }


        }


        return returnValue;

    }


}

