package antenna.direction;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        azimuthLabel.textProperty().bind(model.azimuthProperty().asString("%+7.2f"));
        elevationLabel.textProperty().bind(model.elevationProperty().asString("%5.2f"));
        polarizationLabel.textProperty().bind(model.polarizationProperty().asString("%+6.2f"));
        antennaTargetLabel.textProperty().bind(Bindings.concat("ANT#", model.targetAntennaProperty()));
        angleModeLabel.textProperty().bind(Bindings.createStringBinding(() ->
                model.angleModeProperty().isNull().get() ? "-" : model.angleModeProperty().get().name().substring(0, 1),
                model.angleModeProperty())
        );

        // set handlers to a scene when it is available.
        azimuthLabel.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                scene = newScene;
                initializeSceneHandler();
                initializeSceneStylesheet("CustomFloatingView.css");
            }
        });
    }

    // initialize handlers of scene
    private void initializeSceneHandler() {
        scene.setOnMousePressed(e -> {
            dragStartX = e.getSceneX();
            dragStartY = e.getSceneY();
        });
        scene.setOnMouseDragged(e -> {
            getStage().setX(e.getScreenX() - dragStartX);
            getStage().setY(e.getScreenY() - dragStartY);
        });
        scene.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
                model.setFloating(false);
            } else if (e.getButton() == MouseButton.SECONDARY) {
                copyClipboard();
            }
        });
    }

    // copy data to system clipboard as text.
    private void copyClipboard() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        var content = new ClipboardContent();
        content.putString(toText());
        clipboard.setContent(content);
    }

    // create text from view data
    private String toText() {
        return String.format("%s Az %s El %s Pol %s %s",
                antennaTargetLabel.getText(), azimuthLabel.getText(), elevationLabel.getText(),
                polarizationLabel.getText(), angleModeLabel.getText()
        );
    }

    // user additional stylesheet to the floating scene
    private void initializeSceneStylesheet(String cssFileName) {
        Path path = Paths.get(cssFileName);
        if (Files.exists(path)) {
            scene.getRoot().getStylesheets().add(path.toUri().toString());
        }
    }

    private Stage getStage() {
        return (Stage) scene.getWindow();
    }
}
