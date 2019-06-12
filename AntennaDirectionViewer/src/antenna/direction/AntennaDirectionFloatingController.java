package antenna.direction;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AntennaDirectionFloatingController implements Initializable {
    private static final Logger logger = Logger.getLogger(AntennaDirectionFloatingController.class.getName());
    @FXML
    Label azimuthLabel;
    @FXML
    Label elevationLabel;
    @FXML
    Label polarizationLabel;
    @FXML
    Label antennaTargetLabel;
    @FXML
    Label angleModeLabel;

    private AntennaDirectionViewModel model = AntennaDirectionViewModel.INSTANCE;
    private double dragStartX;
    private double dragStartY;
    private Scene scene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        azimuthLabel.textProperty().bind(model.azimuthProperty().asString());
        elevationLabel.textProperty().bind(model.elevationProperty().asString());
        polarizationLabel.textProperty().bind(model.polarizationProperty().asString());
        azimuthLabel.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                scene = newScene;
                scene.setOnMousePressed(e -> {
                    dragStartX = e.getSceneX();
                    dragStartY = e.getSceneY();
                });
                scene.setOnMouseDragged(e -> {
                   getStage().setX(e.getScreenX() - dragStartX);
                   getStage().setY(e.getScreenY() - dragStartY);
                });
                scene.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        model.setFloating(false);
                    }
                });
            }
        });
        antennaTargetLabel.textProperty().bind(Bindings.concat("ANT#", model.targetAntennaProperty()));
        angleModeLabel.textProperty().bind(Bindings.createStringBinding(() ->
                model.angleModeProperty().isNull().get() ? "-" : model.angleModeProperty().get().name().substring(0, 1))
        );
    }

    Stage getStage() {
        return (Stage) scene.getWindow();
    }
}
