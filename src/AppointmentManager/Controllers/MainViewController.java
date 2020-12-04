package AppointmentManager.Controllers;

import AppointmentManager.Model.Appointment;
import AppointmentManager.Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;


public class MainViewController implements Initializable {


    @FXML
    TableView<Appointment> apptTable = new TableView<>();

    @FXML
    TableColumn<Appointment, Integer> apptIDCol;

    @FXML
    TableColumn<Appointment, String> titleCol;

    @FXML
    TableColumn<Appointment, String> discriptionCol;

    @FXML
    TableColumn<Appointment, String> locationCol;

    @FXML
    TableColumn<Appointment, String> contactCol;

    @FXML
    TableColumn<Appointment, String> typeCol;

    @FXML
    TableColumn<Appointment, Timestamp> startCol;

    @FXML
    TableColumn<Appointment, Timestamp> endCol;

    @FXML
    TableColumn<Appointment, Integer> customerIDCol;

    @FXML
    TableView<Customer> customerTable;

    @FXML
    TableColumn<Customer, Integer> custIDCol;

    @FXML
    TableColumn<Customer, String> nameCol;

    @FXML
    TableColumn<Customer, String> addressCol;

    @FXML
    TableColumn<Customer, String> postalCodeCol;

    @FXML
    TableColumn<Customer, String> phoneCol;

    @FXML
    TableColumn<Customer, Integer> divisionIDCol;



    @FXML
    RadioButton weeklyRadio;

    @FXML
    RadioButton monthlyRadio;

    @FXML
    Button showReportsButton;

    @FXML
    Button editButton;



    /**
     * Updates the UI of based on Radio Buttons.
     * Builds the ToggleGroup for RadioButtons, and changes the apptTable based on selection.
     * @throws SQLException ,
     */
    @FXML
    public void updateRadioUI() throws SQLException {
        ToggleGroup Tgroup = new ToggleGroup();
        weeklyRadio.setToggleGroup(Tgroup);
        monthlyRadio.setToggleGroup(Tgroup);

        if (weeklyRadio.isSelected()) {
            updateTableWeeklyAppointments();
        } else if (monthlyRadio.isSelected()) {
            updateTableMonthlyAppointments();
        }

    }

    /**
     * Displays the Reports.fxml page.
     * @param event Action to display the view
     */
    @FXML
    public void setShowReportsButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Reports.fxml"));
            Parent parent = loader.load();
            Scene newScene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setTitle("Main Screen");
            window.setScene(newScene);
            window.show();

        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * Displays the modify screen for selected table--customer or Appointment based on selected item. Shows error otherwise.
     * @param event An ActionEvent to help scene transition
     */
    @FXML
    public void modifySelectionAction(ActionEvent event) {

        int caseInt = 0;

        if (apptTable.getSelectionModel().getSelectedItem() != null) {
            caseInt = 2;
        } else if (customerTable.getSelectionModel().getSelectedItem() != null) {
            caseInt = 1;
        }


        switch (caseInt) {
            case 1:
                ModifyCustomerViewController.displayedCustomer = customerTable.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/ModifyCustomer.fxml"));
                    Parent screen = loader.load();
                    Scene scene = new Scene(screen);
                    Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    winAddPart.setTitle("Modify Customer");
                    winAddPart.setScene(scene);
                    winAddPart.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.getCause();
                    System.out.println("Load error");
                    System.out.println(e.getLocalizedMessage());
                }
                break;

            case 2:
                ModifyAppointmentViewController.displayedAppointment = apptTable.getSelectionModel().getSelectedItem();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/ModifyAppointment.fxml"));
                    Parent screen = loader.load();
                    Scene scene = new Scene(screen);
                    Stage winAddPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    winAddPart.setTitle("Modify Appointment");
                    winAddPart.setScene(scene);
                    winAddPart.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Load error");
                    System.out.println(e.getLocalizedMessage());
                }
                break;

            default:
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Selection");
                alert.setHeaderText("Please select a customer or appointment to modify");
                alert.showAndWait();
        }

    }

    /** Displays the add appointment view.
     @param screenAddAppointment An ActionEvent to help scene transition
     @throws IOException failed to read the file */
    @FXML
    public void addAppointmentAction (ActionEvent screenAddAppointment) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/AddAppointment.fxml"));
            Parent addCustomerScreen = loader.load();
            Scene addCustomerScene = new Scene(addCustomerScreen);
            Stage winAddPart = (Stage)((Node)screenAddAppointment.getSource()).getScene().getWindow();
            winAddPart.setTitle("Add Appointment");
            winAddPart.setScene(addCustomerScene);
            winAddPart.show();
        }
        catch (IOException e) { e.printStackTrace(); }
    }


    /** Deletes an appointment from the db. First displaying an alert asking the user to confirm.
     * \nLambda Explanation: Lambda is inside of the alert.showAndWait function. It passes the response object in the lambda to be checked */
    @FXML
    public void deleteAppointmentAction() throws SQLException {

        if (apptTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection Detected");
            alert.setHeaderText("Please select an appointment to delete");
            alert.showAndWait();

        }
        else {
            Appointment selectedAppointment = apptTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Appointment?");
            alert.setHeaderText("Delete Appointment?"
                    + "\nType: " + selectedAppointment.getType()
                    + "\nID: " + selectedAppointment.getID());

            alert.showAndWait().ifPresent((response -> {
                        if (response == ButtonType.OK) {
                            boolean success = Appointment.deleteAppointmentFromDB(selectedAppointment.getID());
                            if (success) {
                                Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                                alertConfirm.setTitle("Success");
                                alertConfirm.setHeaderText("Successfully Deleted below Appointment: "
                                        + "\n\nType: " + selectedAppointment.getType()
                                        + "\nID: " + selectedAppointment.getID());
                                alertConfirm.showAndWait();
                            }
                            try {
                                updateTableWeeklyAppointments();
                                updateTableMonthlyAppointments();
                                monthlyRadio.setSelected(true);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                        }
                        else {
                            alert.close();
                        }
            }));

        }
    }


    /** Displays the add customer view.
     @param screenAddCustomer An ActionEvent to help scene transition
     @throws IOException failed to read the file */
    @FXML
    public void addCustomerAction (ActionEvent screenAddCustomer) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/AddCustomer.fxml"));
            Parent addCustomerScreen = loader.load();
            Scene addCustomerScene = new Scene(addCustomerScreen);
            Stage winAddPart = (Stage)((Node)screenAddCustomer.getSource()).getScene().getWindow();
            winAddPart.setTitle("Add Customer");
            winAddPart.setScene(addCustomerScene);
            winAddPart.show();
        }
        catch (IOException e) { assert true; }
    }

    /** Deletes a customer from the db. First displaying an alert asking the user to confirm.
     * \nLambda Explanation: Lambda is inside of the alert.showAndWait function. It passes the response object in the lambda to be checked
     * @throws SQLException , */
    @FXML
    public void deleteCustomerAction() throws SQLException {

        if (customerTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("Please select a customer to delete");
            alert.showAndWait();

        }
        else {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer?");
            alert.setHeaderText("Delete " + selectedCustomer.getName() + "? \nAll of this customer's appointments will be deleted as well.");
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK)  {
                    boolean success = false;

                    try { success = Customer.deleteCustomerFromDB(selectedCustomer.getID());}
                    catch (SQLException throwables) { throwables.printStackTrace(); }

                    if (success) {
                        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
                        alertConfirm.setTitle("Success");
                        alertConfirm.setHeaderText("Customer Deleted Successfully");
                        alertConfirm.showAndWait();
                    }
                    else { System.out.println(success); }

                    try { updateTableCustomers(); }
                    catch (SQLException throwables) { throwables.printStackTrace(); }


                }
                else {
                    alert.close();
                }


            }));

        }
    }



    /**
     * Populates the customerTable with allCustomers.
     * @throws SQLException ,
     */
    @FXML
    public void updateTableCustomers() throws SQLException {
        customerTable.refresh();
        customerTable.setItems(Customer.getAllCustomerDataBase());
        customerTable.refresh();
    }

    /**
     * Populates the apptTable with weekly Appointments.
     * @throws SQLException ,
     */
    @FXML
    public void updateTableWeeklyAppointments() throws SQLException {
        System.out.println("Weekly Appointments: " + Appointment.getWeeklyAppointments());
        apptTable.getItems().clear();
        apptTable.refresh();
        apptTable.setItems(Appointment.getWeeklyAppointments());
        apptTable.refresh();
    }

    /**
     * Populates the apptTable with Monthly Appointments.
     * @throws SQLException
     */
    @FXML
    public void updateTableMonthlyAppointments() throws SQLException {
        System.out.println("Monthly  Appointments: " + Appointment.getMonthlyAppointments());
        apptTable.getItems().clear();
        apptTable.refresh();
        apptTable.setItems(Appointment.getMonthlyAppointments());
        apptTable.refresh();
    }


    /** Initializes Main screen. Sets titles, and items inside combo boxes */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        monthlyRadio.setSelected(true);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        discriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        custIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divisionIDCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

        try {
            updateTableCustomers();
            updateRadioUI();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
