package AppointmentManager.Controllers;

import AppointmentManager.Model.Customer;
import AppointmentManager.Model.Util;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerViewController implements Initializable {

    @FXML
    TextField nameField;

    @FXML
    TextField addressField;

    @FXML
    TextField phoneField;

    @FXML
    TextField postalField;

    @FXML
    Label customerID;

    @FXML
    ComboBox<String> divisionCombo;

    @FXML
    ComboBox<String> countryCombo;


    /**
     * Setup function that populates country combo boxes with combo items.
     */
    private void setupCountryCombo() {
        ObservableList<String> countryComboItems = countryCombo.getItems();
        countryComboItems.add("U.S");
        countryComboItems.add("UK");
        countryComboItems.add("Canada");
    }

    /**
     * Setup function that populates division combo boxes with fetched divisions based on selected country.
     */
    private void setupDivisionCombo() {

        ObservableList<String> divisionComboItems = divisionCombo.getItems();
        divisionComboItems.clear();

        int caseInt = 0;

        if (countryCombo.getValue() == null) {
            caseInt = 1;
        } else if (countryCombo.getValue().equals("U.S")) {
            caseInt = 2;
        } else if (countryCombo.getValue().equals("UK")) {
            caseInt = 3;
        } else if (countryCombo.getValue().equals("Canada")) {
            caseInt = 4;
        }


        switch (caseInt) {
            case 1:
                displayNoCountryError();
                break;
            case 2:
                divisionComboItems.addAll(Util.getDivisions(1));
                break;
            case 3:
                divisionComboItems.addAll(Util.getDivisions(2));
                break;
            case 4:
                divisionComboItems.addAll(Util.getDivisions(3));
                break;
            default:
                System.out.println("Error");
                Alert unexpectedAlert = new Alert(Alert.AlertType.WARNING);
                unexpectedAlert.setTitle("Error");
                unexpectedAlert.setHeaderText("An Unexpected error occurred please retry from previous screen");
                unexpectedAlert.show();
                break;
        }

    }


    /**
     * Saves modifications to customer and displays MainScreen. The old customer is replaced by new customer at the index of old customer.
     * Checks include, but are not limited to: start and end is valid,  all textFields are filled in.
     * @param event An ActionEvent to help scene transition
     * @throws IOException failed to read the file
     */
    @FXML
    public void saveMod(ActionEvent event) throws IOException {

        int caseInt = 0;

        if ( Util.isNumeric(nameField.getText()) || Util.isNumeric(addressField.getText()) ) {
            caseInt = 1;
        } else if (!Util.validatePhoneNumber(phoneField.getText())) {
            caseInt = 2;
        } else if (countryCombo.getValue() == null) {
            caseInt = 3;
        } else if (divisionCombo.getValue() == null) {
            caseInt = 4;
        } else {
            caseInt = 5;
        }

        switch (caseInt) {
            case 1:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Type Error");
                alert.setHeaderText("Please format your inputs like the following:" +
                        "\nName: String" +
                        "\nPostal Code: Number" +
                        "\nAddress: String ");
                alert.show();
                break;
            case 2:
                Alert PhoneAlert = new Alert(Alert.AlertType.WARNING);
                PhoneAlert.setTitle("Phone Number Error");
                PhoneAlert.setHeaderText("Invalid phone number, please verify you are entering the correct number");
                PhoneAlert.show();
                break;
            case 3:
                displayNoCountryError();
                break;
            case 4:
                displayNoDivisionError();
                break;
            case 5:
                try {
                    int index = Integer.parseInt(customerID.getText());
                    String name = nameField.getText();
                    String address = addressField.getText();
                    String postalCode = postalField.getText();
                    String phoneNumber = phoneField.getText();
                    String divisionName = divisionCombo.getValue();


                    Customer updatedCustomer = new Customer(index, name, address, postalCode, phoneNumber, divisionName);
                    Customer.saveNewCustomerToDB(updatedCustomer);


                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Main.fxml"));
                    Parent addPartScreen = loader.load();
                    Scene addPartScene = new Scene(addPartScreen);
                    Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    winAddPart.setTitle("Main");
                    winAddPart.setScene(addPartScene);
                    winAddPart.show();
                } catch (IOException E) {
                    System.out.println(E.getLocalizedMessage());
                }

        }




    }

    /**
     * Displays an alert error if no selected is detected in the country combo box.
     */
    public static void displayNoCountryError() {
        System.out.println("Selection Error");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Selection Error");
        alert.setHeaderText("No Country Selected, please choose");
        alert.show();
    }

    /**
     * Displays an alert error if no selected is detected in the division combo box.
     */
    public static void displayNoDivisionError() {
        System.out.println("Selection Error");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Selection Error");
        alert.setHeaderText("No Division Selected, please choose");
        alert.show();
    }

    /**
     * Displays Main screen and does not save customer changes. The modifications to the customer instance are abandoned.
     * @throws IOException failed to read the file
     */
    @FXML
    void setCancelButton(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel?");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Press OK to discard edits.");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Main.fxml"));
            Parent addPartScreen = loader.load();
            Scene addPartScene = new Scene(addPartScreen);
            Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winAddPart.setTitle("Main");
            winAddPart.setScene(addPartScene);
            winAddPart.show();
        } else {
            alert.close();
        }

    }


    /** Initializes Add Customer screen. Sets titles, and items inside combo boxes */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerID.setText(String.valueOf(Util.getLastIDCustomers() + 1) );

        setupCountryCombo();
        countryCombo.setOnAction(e -> {
            setupDivisionCombo();
        });

    }
}
