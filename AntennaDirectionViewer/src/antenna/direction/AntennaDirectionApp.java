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
 * JavaFX Application entry point.
 *
 * This application uses FXML and CSS files for JavaFX GUI, and logging properties file.
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
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();
    }

    /**
     * Initialize log configuration.
     *
     * Configuration can be applied from file or class specified by system property.
     * If system property is not specified, then read file named 'logging.properties'
     * from the directory of this application class.
     */
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

    /**
     * Entry point of this application program.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        setupLogging();
        launch(args);
    }
}
