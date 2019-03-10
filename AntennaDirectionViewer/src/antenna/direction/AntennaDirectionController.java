/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import antenna.direction.boundary.AntennaDirectionBoundary;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.converter.LocalTimeStringConverter;
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

    @FXML CheckBox periodicUpdateCheckbox;
    @FXML TextField periodicTextField;

    AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    AntennaDirectionBoundary boundary = AntennaDirectionBoundary.INSTANCE;

    @FXML
    void handleManualUpdate(ActionEvent event) {
        logger.info("Manual Update Operation Triggered.");
        update();
    }

    @FXML
    void handleApply(ActionEvent event) {
        logger.config("Configuraion Apply Triggered.");
        config();
    }

    void update() {
        boundary.getAzimuth();
        boundary.getElevation();
        boundary.getPolarizatoin();
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
    }
    
    
}
