/*
 *  Copyright © 2019 TAKAHASHI,Toru
 */
package antenna.direction;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author TAKAHASHI,Toru
 */
public enum AntennaDirectionViewModel {
    INSTANCE;
    DoubleProperty elevationProperty = new SimpleDoubleProperty(Double.NaN); 
    DoubleProperty azimuthProperty = new SimpleDoubleProperty(Double.NaN);
    DoubleProperty polarizationProperty = new SimpleDoubleProperty(Double.NaN);
    BooleanProperty absolutePropeerty = new SimpleBooleanProperty(Boolean.FALSE);

    public void setElevation(double angle) {
        elevationProperty.set(angle);
    }
    
    public void setAzimuth(double angle) {
        azimuthProperty.set(angle);
    }
    
    public void setPolarization(double angle) {
        polarizationProperty.set(angle);
    }

    public void setAbsolute(boolean isAbsolute) {
        absolutePropeerty.set(isAbsolute);
    }
}
