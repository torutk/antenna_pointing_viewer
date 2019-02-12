module AntennaDirectionViewer {
    requires java.logging;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;
    opens antenna.direction to javafx.graphics, javafx.fxml;

}