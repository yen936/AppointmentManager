package AppointmentManager.Controllers;

import AppointmentManager.Model.Appointment;
import AppointmentManager.Model.Customer;
import AppointmentManager.Model.User;
import AppointmentManager.Model.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddAppointmentViewController implements Initializable {


    @FXML
    Label mainIDLabel;

    @FXML
    TextField IDField;

    @FXML
    TextField titleField;

    @FXML
    TextField descriptionField;

    @FXML
    TextField locationField;

    @FXML
    TextField typeField;

    @FXML
    DatePicker startDatePicker;

    @FXML
    ComboBox<String> startHourCBO;

    @FXML
    ComboBox<String> startMinCBO;

    @FXML
    DatePicker endDatePicker;

    @FXML
    ComboBox<String> endHourCBO;
    ObservableList<String> hours = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> endMinCBO;
    ObservableList<String> minutes = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> contactCombo;

    @FXML
    TextField userIDField;

    @FXML
    ComboBox<Integer> customerCombo;

    @FXML
    Button cancelButton;

    @FXML
    Button saveButton;


    /**
     * Saves modifications to appointment and displays MainScreen. The old appointment is replaced by new appointment at the index of old appointment.
     * Checks include, but are not limited to: start and end is valid,  all textFields are filled in.
     *
     * @param event An ActionEvent to help scene transition
     * @throws IOException failed to read the file
     */
    @FXML
    public void saveMod(ActionEvent event) throws IOException {

        LocalDate startDatePickerValue = startDatePicker.getValue();
        String startHourCBOValue = startHourCBO.getValue();
        String startMinCBOValue = startMinCBO.getValue();

        LocalDate endDatePickerValue = endDatePicker.getValue();
        String endHourCBOValue = endHourCBO.getValue();
        String endMinCBOValue = endMinCBO.getValue();

        // obtain the START LocalDateTime
        LocalDateTime startldt = LocalDateTime.of(startDatePickerValue.getYear(),
                startDatePickerValue.getMonthValue(),
                startDatePickerValue.getDayOfMonth(),
                Integer.parseInt(startHourCBOValue),
                Integer.parseInt(startMinCBOValue));

        // obtain the END LocalDateTime
        LocalDateTime endLdt = LocalDateTime.of(endDatePickerValue.getYear(),
                endDatePickerValue.getMonthValue(),
                endDatePickerValue.getDayOfMonth(),
                Integer.parseInt(endHourCBOValue),
                Integer.parseInt(endMinCBOValue));

        int caseInt = 0;
        Alert Alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR);

        if (titleField.getText() == null || locationField.getText() == null || descriptionField.getText() == null ||
                typeField.getText() == null || contactCombo.getValue() == null || customerCombo.getValue() == null || userIDField.getText() == null) {
            caseInt = 1;
        } else if (startDatePicker.getValue() == null || endDatePicker.getValue() == null || startHourCBO.getValue() == null
                || startMinCBO.getValue() == null || endHourCBO.getValue() == null || endMinCBO.getValue() == null) {
            caseInt = 2;
        } else if (startldt.isAfter(endLdt)){
            caseInt = 3;
        }
        else if (!Util.insideOfBusinessHours(startldt)) {
            caseInt = 4;
        }
        else if (!Util.insideOfBusinessHours(endLdt)) {
            caseInt = 5;
        }
        else {
            caseInt = 6;
        }

        switch (caseInt) {
            case 1:
                Alert.setTitle("Missing Fields");
                Alert.setHeaderText("Please fill in all of the fields");
                Alert.show();
                break;
            case 2:
                Alert.setTitle("Date and time not completely filled in!");
                Alert.setHeaderText("Please use the calendar date picker to select a date " +
                        "AND use the Hour and Minute dropdown to set hours and minutes before clicking this button");
                Alert.show();
                break;
            case 3:
                Alert.setTitle("Inaccurate Dates and Times");
                Alert.setHeaderText("Appointment ends must be after the start");
                Alert.show();
                break;
            case 4:
                Alert.setTitle("Start time is outside of business hours");
                Alert.setHeaderText("Start time is outside of business hours. " +
                        "\nBusiness hours are 8 am - 10pm EST.");
                Alert.show();
                break;
            case 5:
                Alert.setTitle("End time is outside of business hours");
                Alert.setHeaderText("End time is outside of business hours. " +
                        "\nBusiness hours are 8 am - 10pm EST.");
                Alert.show();
                break;
            case 6:
                try {
                    int index = Integer.parseInt(IDField.getText());
                    String title = titleField.getText();
                    String location = locationField.getText();
                    String type = typeField.getText();
                    String description = descriptionField.getText();
                    int userID = User.getCurrentUser().getID();
                    int customerID = customerCombo.getValue();
                    String contactName = contactCombo.getValue();

                    Appointment proposedAppt = new Appointment(index, title, description, location,
                            type, startldt, endLdt, userID, customerID, contactName);

                    System.out.println("Is there an overlap? : " + Util.hasOverlap(proposedAppt));

                    if (!Util.hasOverlap(proposedAppt)) {
                        boolean saveSuccess = Appointment.saveNewAppointmentToDB(proposedAppt);
                        System.out.println("Save was Successful? :" + saveSuccess); FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Main.fxml"));


                        Parent addPartScreen = loader.load();
                        Scene addPartScene = new Scene(addPartScreen);
                        Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        winAddPart.setTitle("Main");
                        winAddPart.setScene(addPartScene);
                        winAddPart.show();

                    }
                    else {
                        Alert.setTitle("Overlap Error");
                        Alert.setHeaderText("This appointment overlaps another appointment with this Customer.");
                        Alert.show();
                    }


                } catch (IOException | SQLException E) {
                    System.out.println(E.getLocalizedMessage());
                }
                catch (NullPointerException e) {
                    Alert.setTitle("Empty Field Error");
                    Alert.setHeaderText("Please fill in all of the fields before submitting");
                    Alert.show();
                }
                break;
        }


    }


    /**
     * Displays Main screen and does not save appointment changes. The modifications to the appointment instance are abandoned.
     * @throws IOException failed to read the file */
    @FXML
    void setCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel?");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to discard edits.");
        alert.showAndWait().ifPresent( response -> {
            if (response == ButtonType.OK){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Main.fxml"));
                Parent addPartScreen = null;
                try { addPartScreen = loader.load(); }
                catch (IOException e) { e.printStackTrace(); }

                assert addPartScreen != null;
                Scene addPartScene = new Scene(addPartScreen);
                Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                winAddPart.setTitle("Main");
                winAddPart.setScene(addPartScene);
                winAddPart.show();
            }
            else {
                alert.close();
            }
        });

    }


    /** Initializes Add Appointment screen. Sets titles, and items inside combo boxes */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainIDLabel.setText(String.valueOf(Util.getLastIDAppointment() + 1));
        IDField.setText(String.valueOf(Util.getLastIDAppointment() + 1));
        IDField.setDisable(true);
        userIDField.setText(String.valueOf((User.getCurrentUser().getID())));

        contactCombo.setItems(Util.getAllContactsDataBase());
        customerCombo.setItems(Util.getAllCustomersDataBase());


        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "15", "30", "45");
        startHourCBO.setItems(hours);
        endHourCBO.setItems(hours);
        startMinCBO.setItems(minutes);
        endMinCBO.setItems(minutes);


    }

}
