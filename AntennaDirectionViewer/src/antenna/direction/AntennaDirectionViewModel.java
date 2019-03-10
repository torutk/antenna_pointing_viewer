/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import javafx.beans.property.*;

import java.time.LocalTime;

/**
 *
 * @author TAKAHASHI,Toru
 */
public enum AntennaDirectionViewModel {
    INSTANCE;

    public enum AngleMode {
        ABSOLUTE, RELATIVE;
    }

    private DoubleProperty elevationProperty = new SimpleDoubleProperty(Double.NaN);
    private DoubleProperty azimuthProperty = new SimpleDoubleProperty(Double.NaN);
    private DoubleProperty polarizationProperty = new SimpleDoubleProperty(Double.NaN);
    private ObjectProperty<AngleMode> angleModeProperty = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> updateTimeProperty = new SimpleObjectProperty<>();
    private IntegerProperty periodicProperty = new SimpleIntegerProperty(0);

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
    }
}
