/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import javafx.beans.property.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * View Model for Antenna Direction.
 *
 * @author TAKAHASHI,Toru
 */
public enum AntennaDirectionViewModel {
    INSTANCE;

    public enum AngleMode {
        ABSOLUTE, RELATIVE
    }

    private DoubleProperty elevationProperty = new SimpleDoubleProperty(Double.NaN);
    private DoubleProperty azimuthProperty = new SimpleDoubleProperty(Double.NaN);
    private DoubleProperty polarizationProperty = new SimpleDoubleProperty(Double.NaN);
    private ObjectProperty<AngleMode> angleModeProperty = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> updateTimeProperty = new SimpleObjectProperty<>();
    private IntegerProperty periodicProperty = new SimpleIntegerProperty(0);
    private BooleanProperty floatingProperty = new SimpleBooleanProperty(false);
    private IntegerProperty targetAntennaProperty = new SimpleIntegerProperty(1);

    private AntennaDirectionLogger directionLogger = new AntennaDirectionLogger();

    public DoubleProperty elevationProperty() {
        return elevationProperty;
    }

    public DoubleProperty azimuthProperty() {
        return azimuthProperty;
    }

    public DoubleProperty polarizationProperty() {
        return polarizationProperty;
    }

    public ObjectProperty<AngleMode> angleModeProperty() {
        return angleModeProperty;
    }

    public ObjectProperty<LocalTime> updateTimeProperty() {
        return updateTimeProperty;
    }

    public IntegerProperty periodicProperty() {
        return periodicProperty;
    }

    public BooleanProperty floatingProperty() {
        return floatingProperty;
    }

    public IntegerProperty targetAntennaProperty() {
        return targetAntennaProperty;
    }

    public void setElevation(double angle) {
        elevationProperty.set(angle);
    }
    
    public void setAzimuth(double angle) {
        azimuthProperty.set(angle);
    }
    
    public void setPolarization(double angle) {
        polarizationProperty.set(angle);
    }

    public void setAngleMode(AngleMode angleMode) {
        angleModeProperty.set(angleMode);
    }

    public void setUpdateTime(LocalTime time) {
        updateTimeProperty.set(time);
        directionLogger.add(
                LocalDateTime.of(LocalDate.now(), time),
                targetAntennaProperty.intValue(),
                azimuthProperty.doubleValue(),
                elevationProperty.doubleValue(),
                polarizationProperty.doubleValue(),
                angleModeProperty.getValue()
        );
    }

    public void setFloating(boolean isFloating) {
        floatingProperty.setValue(isFloating);
    }

    public void setTargetAntenna(int id) {
        if (id != 1 && id !=2) {
            throw new IllegalArgumentException("id should be 1 or 2, but " + id);
        }
        targetAntennaProperty.set(id);
    }

    public void saveDirectionLog(Path path) {
        directionLogger.save(path);
    }
}
