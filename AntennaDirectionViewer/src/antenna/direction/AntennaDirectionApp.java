/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

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
        var bundle = ResourceBundle.getBundle("antenna.direction.AntennaDirectionView");
        Parent root = FXMLLoader.load(getClass().getResource("AntennaDirectionView.fxml"), bundle);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("STC-110 Remote Viewer " + bundle.getString("antenna.direction.version"));
        primaryStage.setOnCloseRequest(event -> Platform.exit());
        primaryStage.show();
    }

    /**
     * Initialize log configuration.
     *
     * Logging configuration can be applied from file or class specified by system property.
     * If system property is not specified, then read file named 'logging.properties'
     * from the directory of this application class.
     */
    private static void setupLogging() {
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
     * Some system properties are used for this application.
     * <pre>{@code
     *     antenna.direction.boundary.serialport1 - specify serial port identifier for antenna #1
     *     antenna.direction.boundary.serialport2 - specify serial port identifier for antenna #2
     * }</pre>
     *
     * @param args command-line arguments
     *
     */
    public static void main(String[] args) {
        setupLogging();
        launch(args);
    }

}
