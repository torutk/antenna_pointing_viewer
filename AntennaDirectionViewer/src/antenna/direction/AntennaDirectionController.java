/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import antenna.direction.boundary.AntennaDirectionBoundary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

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

    AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    AntennaDirectionBoundary boundary = AntennaDirectionBoundary.INSTANCE;

    @FXML
    void handleManualUpdate(ActionEvent event) {
        logger.fine("Manual Update Operation Triggered.");
        boundary.getAzimuth();
        boundary.getElevation();
        boundary.getPolarizatoin();
        model.setUpdateTime(LocalTime.now());
        logger.fine("3 Axes are read.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elevationText.textProperty().bindBidirectional(
                model.elevationProperty, new NumberStringConverter()
        );
        azimuthText.textProperty().bindBidirectional(
                model.azimuthProperty, new NumberStringConverter()
        );
        polarizationText.textProperty().bind(model.polarizationProperty.asString());
        absoluteText.textProperty().bind(model.angleModeProperty.asString());
        updateTimeText.textProperty().bind(model.updateTimeProperty.asString());
    }
    
    
}
