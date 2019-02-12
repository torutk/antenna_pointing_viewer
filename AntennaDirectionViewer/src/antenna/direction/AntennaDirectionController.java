/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import antenna.direction.boundary.AntennaDirectionBoundary;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.util.converter.NumberStringConverter;

/**
 *
 * @author TAKAHASHI,Toru
 */
public class AntennaDirectionController implements Initializable {
    private static final Logger logger = Logger.getLogger(AntennaDirectionController.class.getName());
    @FXML Label elevationLabel;
    @FXML Label azimuthLabel;
    @FXML CheckBox absoluteCheckbox;
    @FXML Label polarizationLabel;

    AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    AntennaDirectionBoundary boundary = AntennaDirectionBoundary.INSTANCE;

    @FXML
    void handleElevation(ActionEvent event) {
        logger.fine("Now read elevation from STC-110.");
        boundary.getElevation();
        logger.fine("elevation is read.");
    }

    @FXML
    void handleAzimuth(ActionEvent event) {
        logger.fine("Now read azimuth from STC-110.");
        boundary.getAzimuth();
        logger.fine("read azimuth from STC-110.");
    }

    @FXML
    void handlePolarization(ActionEvent event) {
        logger.fine("Now read polarization from STC-110.");
        boundary.getPolarizatoin();
        logger.fine("read polarization from STC-110.");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        elevationLabel.textProperty().bindBidirectional(
                model.elevationProperty, new NumberStringConverter()
        );
        azimuthLabel.textProperty().bindBidirectional(
                model.azimuthProperty, new NumberStringConverter()
        );
        polarizationLabel.textProperty().bindBidirectional(
                model.polarizationProperty, new NumberStringConverter()
        );
        absoluteCheckbox.selectedProperty().bind(model.absolutePropeerty);
    }
    
    
}
