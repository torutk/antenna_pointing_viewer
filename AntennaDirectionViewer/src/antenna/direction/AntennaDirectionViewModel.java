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

    DoubleProperty elevationProperty = new SimpleDoubleProperty(Double.NaN); 
    DoubleProperty azimuthProperty = new SimpleDoubleProperty(Double.NaN);
    DoubleProperty polarizationProperty = new SimpleDoubleProperty(Double.NaN);
    ObjectProperty<AngleMode> angleModeProperty = new SimpleObjectProperty<>();
    ObjectProperty<LocalTime> updateTimeProperty = new SimpleObjectProperty<>();

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
