/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import antenna.direction.boundary.AntennaDirectionBoundary;
import javafx.beans.binding.Bindings;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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
    @FXML ToggleGroup antennaSelectionGroup;

    private PseudoClass errorClass = PseudoClass.getPseudoClass("error");
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

    @FXML
    void handleSaveRecord(ActionEvent event) {
        logger.info("Save Record Triggered.");
        saveRecord();
    }

    private void update() {
        logger.finer("Update antenna direction values.");
        boundary.getAzimuth();
        boundary.getElevation();
        boundary.getPolarization();
        model.setUpdateTime(LocalTime.now());
    }

    private void config() {
        int period = model.periodicProperty().get();
        logger.info("model#periodicProperty is " + period);
        if (! periodicUpdateCheckbox.isSelected()) {
            boundary.stopPeriodic();
            return;
        }
        if (period < 1) {
            periodicTextField.pseudoClassStateChanged(errorClass, true);
            periodicUpdateCheckbox.setSelected(false);
            return;
        }
        boundary.startPeriodic(period);
        periodicTextField.pseudoClassStateChanged(errorClass, false);
    }

    /**
     * change the view as transparent floating(always top) window.
     *
     * @throws IOException FXML file cannot be loaded
     */
    private void floating() throws IOException {
        stage.hide();
        floatingStage = new Stage();
        floatingStage.initOwner(stage);
        Parent root = FXMLLoader.load(getClass().getResource("AntennaDirectionFloatingView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        floatingStage.setScene(scene);
        floatingStage.initStyle(StageStyle.TRANSPARENT);
        floatingStage.setAlwaysOnTop(true);
        floatingStage.show();
        model.setFloating(true);
    }

    private void targetAntenna(int id) {
        logger.info("Antenna target is changed to " + id);
        model.setTargetAntenna(id);
        boundary.connectTo(id);
    }

    private void saveRecord() {
        var chooser = new FileChooser();
        var file = chooser.showSaveDialog(null);
        if (file == null) {
            return;
        }
        logger.info("Selected file to save directions - " + file);
        model.saveDirectionLog(file.toPath());
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
        antennaSelectionGroup.selectedToggleProperty().addListener((obs, ov, nv) -> {
            RadioButton selected = (RadioButton) nv;
            if (selected.getText().equals("ANT#1")) {
                targetAntenna(1);
            } else if (selected.getText().equals("ANT#2")) {
                targetAntenna(2);
            } else {
                throw new IllegalArgumentException("RadioButton should be 'ANT#1' or 'ANT#2', but " + selected.getText());
            }
        });

    }
    
    
}
