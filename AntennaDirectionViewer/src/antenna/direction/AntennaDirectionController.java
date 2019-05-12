/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import antenna.direction.boundary.AntennaDirectionBoundary;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 *
 * @author TAKAHASHI,Toru
 */
public class AntennaDirectionController implements Initializable {
    private static final Logger logger = Logger.getLogger(AntennaDirectionController.class.getName());
    @FXML Text azimuthText;
    @FXML Text elevationText;
    @FXML Text polarizationText;
    @FXML Text absoluteText;
    @FXML Text updateTimeText;

    @FXML CheckBox periodicUpdateCheckbox;
    @FXML TextField periodicTextField;

    private AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    private AntennaDirectionBoundary boundary = AntennaDirectionBoundary.INSTANCE;
    private Stage stage;
    private Stage floatingStage;

    @FXML
    void handleManualUpdate(ActionEvent event) {
        logger.info("Manual Update Operation Triggered.");
        update();
    }

    @FXML
    void handleApply(ActionEvent event) {
        logger.config("Configuration Apply Triggered.");
        config();
    }

    @FXML
    void handleFloatingView(ActionEvent event) throws IOException {
        logger.info("Floating view Triggered.");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        floating();
    }

    void update() {
        boundary.getAzimuth();
        boundary.getElevation();
        boundary.getPolarization();
        model.setUpdateTime(LocalTime.now());
    }

    void config() {
        logger.info("model#periodicProperty is " + model.periodicProperty().get());
        if (periodicUpdateCheckbox.isSelected()) {
            boundary.startPeriodic(model.periodicProperty().get());
        } else {
            boundary.stopPeriodic();
        }
    }

    /**
     * change the view as transparent floating(always top) window.
     *
     * @throws IOException
     */
    void floating() throws IOException {
        stage.hide();
        floatingStage = new Stage();
        floatingStage.initOwner(stage);
        Parent root = FXMLLoader.load(getClass().getResource("AntennaDirectionFloatingView.fxml"));
        root.setStyle("-fx-background-color: transparent");
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        floatingStage.setScene(scene);
        floatingStage.initStyle(StageStyle.TRANSPARENT);
        floatingStage.setAlwaysOnTop(true);
        floatingStage.show();
        model.setFloating(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elevationText.textProperty().bind(model.elevationProperty().asString());
        azimuthText.textProperty().bind(model.azimuthProperty().asString());
        polarizationText.textProperty().bind(model.polarizationProperty().asString());
        absoluteText.textProperty().bind(model.angleModeProperty().asString());
        updateTimeText.textProperty().bindBidirectional(
                model.updateTimeProperty(), new LocalTimeStringConverter(FormatStyle.MEDIUM)
        );
        periodicTextField.disableProperty().bind(Bindings.not(periodicUpdateCheckbox.selectedProperty()));
        periodicTextField.textProperty().bindBidirectional(model.periodicProperty(), new NumberStringConverter());
        model.floatingProperty().addListener((arg, oldValue, newValue) -> {
            logger.info("floatingProperty changed from " + oldValue + " to " + newValue);
            if (!newValue) {
                floatingStage.close();
                stage.show();
            }
        });
    }
    
    
}
