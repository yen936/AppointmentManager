package AppointmentManager;

import AppointmentManager.Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Main extends Application {

    public final static String language = Locale.getDefault().getDisplayLanguage();
    public final static TimeZone localZone = TimeZone.getDefault();
    public final static ZoneId newZoneId = ZoneId.systemDefault();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("View/Login.fxml"));
        primaryStage.setTitle("Appointment Manager");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
        DBConnect.startConnection();

//        LocalDateTime startldt = LocalDateTime.of(2020, 11, 23, 8, 15);
//        LocalDateTime endLdt = LocalDateTime.of(2020, 11, 23, 9, 45);
//
//        int index = 9;
//        String title = "l";
//        String location = "l";
//        String type = "l";
//        String description = "l";
//        int userID = 1;
//        int customerID = 3;
//        String contactName = "Anika Costa";
//
//        Appointment proposedAppt = new Appointment(index, title, description, location,
//                type, startldt, endLdt, userID, customerID, contactName);
//
//        System.out.println("Is there an overlap? : " + Util.hasOverlap(proposedAppt));

    }

    /**
     * Lambda justification:
     * Was
     *  Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
     *             public void run() {
     *                 DBConnect.closeConnection();
     *             }
     *         }, "Shutdown-thread"));
     *
     * By adding a lambda from the thread I was able to remove two lines of code. Making the code more readable.
     *
     * */

    public static void main(String[] args) {
        launch(args);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> DBConnect.closeConnection(), "Shutdown-thread"));
    }



}


