/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author TAKAHASHI,Toru
 */
public class AntennaDirectionApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AntennaDirectionView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("STC-110 Remote Viewer");
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        primaryStage.show();
    }

    static void setupLogging() {
        if (System.getProperty("java.util.logging.config.file") == null
                && System.getProperty("java.util.logging.config.class") == null) {
            try (InputStream resource = AntennaDirectionApp.class.getResourceAsStream("logging.properties")) {
                if (resource != null) {
                    LogManager.getLogManager().readConfiguration(resource);
                }
            } catch (IOException ex) {
                System.err.println("CONFIG error in reading logging.properties.");
            }
        }
    }

    public static void main(String[] args) {
        setupLogging();
        launch(args);
    }
}
