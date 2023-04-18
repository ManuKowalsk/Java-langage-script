package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Java Program to create a button and add it to the stage

public class appIHM extends Application {

    // launch the application
    public void start(Stage s)
    {
        // set title for the stage
        try {
        Parent root = FXMLLoader.load(getClass().getResource("sb.fxml"));
        Scene scene = new Scene(root,600,400);
        s.setX(670);
        s.setY(20);
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        String css = this.getClass().getResource("application.css").toExternalForm();
        scene.getStylesheets().add(css);
        s.setScene(scene);
        s.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }
}