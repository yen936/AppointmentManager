package AppointmentManager.Controllers;

import AppointmentManager.Main;
import AppointmentManager.Model.Appointment;
import AppointmentManager.Model.User;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


public class LoginViewController implements Initializable {

    @FXML
    Label loginTitle;

    @FXML
    Label statusText;

    @FXML
    Button loginButton;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    Label locationLabel;



    /** Attempts to login the user in. Checks if user exists, all fields are filled in, password matches user in DB. Logs all login attempts
     * @param event Button Click*/
    @FXML
    public void setLoginButton(ActionEvent event) {

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            emptyFieldsError();

        } else {

            setLoadingText();
            boolean loginResult = User.login(usernameField.getText(), passwordField.getText());

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event1 -> {
                statusText.setText("");
                if (loginResult) {
                    System.out.println(loginResult);

                    Alert apptAlert = Appointment.get15MinAppointments();
                    apptAlert.show();

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

                } else {
                    invalidLoginError();
                }

            });
            pause.play();

        }
    }

    /** Sets the location label to the user's local country. in French or English based on the detected OS language. */
    @FXML
    private void getLocation() {

        if (Main.language.equals("français")) {
            locationLabel.setText("Vous êtes dans: " + Locale.getDefault().getDisplayCountry());
        }
        else {
            locationLabel.setText("Log in Location: " + Locale.getDefault().getDisplayCountry());
        }

    }


    /** Displays a login error--missing fields-- in French or English based on the detected OS language.*/
    private void emptyFieldsError() {

        //Empty fields
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (Main.language.equals("français")) {
            alert.setTitle("Les champs sont manquants");
            alert.setHeaderText("Veuillez remplir le nom d'utilisateur et le mot de passe");
        }
        else {

            alert.setTitle("Fields are missing");
            alert.setHeaderText("Please fill in username and password");
        }
        alert.show();
    }


    /** Displays a login error--invalid credentials-- in French or English based on the detected OS language.*/
    private void invalidLoginError() {

        //Invalid credentials
        Alert newAlert = new Alert(Alert.AlertType.ERROR);
        if (Main.language.equals("français"))  {

            newAlert.setTitle("Erreur d'identification");
            newAlert.setHeaderText("Impossible de se connecter avec les identifiants fournis");
        }
        else {
            newAlert.setTitle("Login Error");
            newAlert.setHeaderText("Unable to login with the provided credentials");
        }
        newAlert.show();


    }

    /** Sets 'Loading' message to prompt text, in French or English based on the detected OS language. */
    private void setLoadingText() {
        if (Main.language.equals("français"))  {
            statusText.setText("Veuillez patienter...");
        }
        else {
            statusText.setText("Please wait...");
        }
    }

    /** Changes the UI elements to French if French is detected as ths OS language. */
    public void setUpViewByLanguage() {
        if (Main.language.equals("français")) {
            loginTitle.setText("Connexion");
            loginButton.setText("Connexion");
            usernameField.setPromptText("Nom d'utilisateur");
            passwordField.setPromptText("Mot de passe");
        }
        else { assert true; }
    }



    /** Initializes View and calls helper functions. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getLocation();
        setUpViewByLanguage();
    }
}
