package AppointmentManager.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;


public class Reports {

    /** Displays the customer appointment report. Counts the number appointments by month, and by type.
     *
     * @throws SQLException ,
     */
    public static void customerAppointmentReport() throws SQLException {

        //Month section
        int[] nubAppts = new int[13];
        int i;

        for (i = 1; i < nubAppts.length; i++) {

            PreparedStatement preparedSQL = DBConnect.getConnection()
                    .prepareCall("SELECT COUNT(Title) FROM appointments WHERE MONTH(Start) = '" + i + "'");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (resultSet.next()) {
                nubAppts[i] = resultSet.getInt("COUNT(Title)");
            }

        }


        //Type Section
        int noOfTypes = 0;
        PreparedStatement preparedSQL1 = DBConnect.getConnection().prepareCall("SELECT COUNT(DISTINCT Type) FROM appointments");
        ResultSet resultSet1 = preparedSQL1.executeQuery();

        if (resultSet1.next()) {
            noOfTypes = resultSet1.getInt("COUNT(DISTINCT Type)");
        }

        String[] types = new String[noOfTypes];
        int[] occurrences = new int[noOfTypes];


        PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT DISTINCT Type FROM appointments");
        ResultSet resultSet = preparedSQL.executeQuery();

        int k = 0;
        while (resultSet.next()) {
            String typeName = resultSet.getString("Type");
            types[k] = typeName;


            PreparedStatement preparedSQL2 = DBConnect.getConnection().prepareCall("SELECT COUNT(DISTINCT Type) FROM appointments WHERE Type ='" + typeName + "'");
            ResultSet resultSet2 = preparedSQL2.executeQuery();

            while (resultSet2.next()) {

                occurrences[k] = resultSet2.getInt("COUNT(DISTINCT Type)");
            }

            k++;

        }


        String monthOutput = "Appointments By Month:" +
                "\nJanuary: " + nubAppts[1]
                + "\nFebruary: " + nubAppts[2]
                + "\nMarch: " + nubAppts[3]
                + "\nApril: " + nubAppts[4]
                + "\nMay: " + nubAppts[5]
                + "\nJune: " + nubAppts[6]
                + "\nJuly: " + nubAppts[7]
                + "\nAugust: " + nubAppts[8]
                + "\nSeptember: " + nubAppts[9]
                + "\nOctober: " + nubAppts[10]
                + "\nNovember: " + nubAppts[11]
                + "\nDecember: " + nubAppts[12];


        String typeOutput = formatTypeOutput(types, occurrences);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Total number of appointments by type and month");
        alert.setHeaderText(null);
        alert.setContentText(monthOutput + "\n\n" + typeOutput);
        alert.showAndWait();


    }

    /**
     * Formats the type output into a string for easy display
     *
     * @param occurrences Int[], with the amount of times each type occurs
     * @param types       String[], with the DISTINCT types
     * @return String
     */
    private static String formatTypeOutput(String[] types, int[] occurrences) {
        int i;
        String returnedString = "Appointments by Type";

        for (i = 0; i < types.length; i++) {
            returnedString = returnedString + "\n" + types[i] + ": " + occurrences[i];
        }

        return returnedString;
    }


    /** Displays the schedule report for each contact in the organization. Uses contact ID to determine contact.
     *
     * @param contactID index for contact
     * @return ObservableList<Appointment>
     */
    public static ObservableList<Appointment> contactScheduleReport(int contactID) {

        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().prepareCall("SELECT * FROM appointments WHERE Contact_ID = '" + contactID + "'");
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
                        Util.getContactName(resultSet.getString("Contact_ID")));

                appointments.add(a);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }


        return appointments;

    }


    /** Displays the past appointment report. Gathers appointments that are over a week old,
     *  and displays the contacts that correspond with those appointments
     *
     * @return ObservableList<Contact>
     * @throws SQLException ,
     */
    public static ObservableList<Contact> pastAppointmentReports() throws SQLException {

        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        LocalDateTime weekAgo = LocalDateTime.now().minusWeeks(1);

        try {
            PreparedStatement preparedSQL = DBConnect.getConnection().
                    prepareCall("SELECT * FROM appointments, contacts WHERE Start > '" + Util.localToUTC(weekAgo)
                            + "' AND appointments.Contact_ID = contacts.Contact_ID");
            ResultSet resultSet = preparedSQL.executeQuery();

            if (!resultSet.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Follow up Needed");
                alert.setHeaderText("No Follow up Needed");
                alert.setContentText("Currently there are no contacts to follow-up with");
            }
            while (resultSet.next()) {

                Contact c = new Contact(resultSet.getInt("contacts.Contact_ID"),
                        resultSet.getString("contacts.Contact_Name"),
                        resultSet.getString("contacts.email"));
                contacts.add(c);
            }

            return contacts;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Failure outside of SQL");
        }

        System.out.println("Contacts:" + contacts);
        return contacts;
    }

}

