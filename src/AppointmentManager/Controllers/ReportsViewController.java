package AppointmentManager.Controllers;

import AppointmentManager.Model.Appointment;
import AppointmentManager.Model.Contact;
import AppointmentManager.Model.Reports;
import AppointmentManager.Model.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;


public class ReportsViewController implements Initializable {


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
    TableView<Contact> contactTable;

    @FXML
    TableColumn<Contact, String> contactNameCol;

    @FXML
    TableColumn<Contact, String> contactEmailCol;

    @FXML
    TableColumn<Contact, Integer> contactIdCol;


    @FXML
    Button backButton;

    @FXML
    Button doneButton;

    @FXML
    Button followUpButton;

    @FXML
    Button numberApptsButton;

    @FXML
    ComboBox<String> contactCombo;


    /**
     * Displays the Total Appointments by Type and Month Report.
     */
    @FXML
    public void showApptsReport() throws SQLException {
        Reports.customerAppointmentReport();
    }

    /**
     * Populates the apptTable with weekly Appointments pulled from the database.
     */
    @FXML
    public void updateTableScheduleReport() throws SQLException {
        apptTable.setVisible(true);
        doneButton.setVisible(true);
        apptTable.getItems().clear();
        apptTable.refresh();
        int contactID = Util.getContactID(contactCombo.getValue());
        System.out.println(Reports.contactScheduleReport(contactID));
        apptTable.setItems(Reports.contactScheduleReport(contactID));
        apptTable.refresh();
    }

    /**
     * Updates UI to reflect current conditions
     */
    @FXML
    public void setDoneButton() {
        apptTable.setVisible(false);
        doneButton.setVisible(false);
    }

    /**
     * Updates UI to reflect current conditions
     */
    @FXML
    public void setFollowUpButton() throws SQLException {

        if (contactTable.isVisible()) {
            contactTable.setVisible(false);
            followUpButton.setText("Show Report");
        } else {
            contactTable.getItems().clear();
            contactTable.refresh();
            contactTable.setItems(Reports.pastAppointmentReports());
            contactTable.setVisible(true);
            followUpButton.setText("Done");
        }

    }




    /**
     * Displays the Main.fxml page.
     * @param event Button click
     */
    @FXML
    public void setBackButton(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AppointmentManager/View/Main.fxml"));
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

    /** Initializes Add Appointment screen. Sets titles, and items inside combo boxes */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactCombo.setItems(Util.getAllContactsDataBase());

        apptTable.setVisible(false);
        doneButton.setVisible(false);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        discriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        contactTable.setVisible(false);

        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        contactNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
}
