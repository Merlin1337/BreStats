package com.brestats.model.data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MapCoordinates {
    private DoubleProperty latitude;
    private DoubleProperty longitude;

    public MapCoordinates() {
        this.latitude = new SimpleDoubleProperty(0);
        this.longitude = new SimpleDoubleProperty(0);
    }


    public DoubleProperty getLatitudeProperty() {
        return this.latitude;
    }

    public DoubleProperty getLongitudeProperty() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude.doubleValue();
    }

    public double getLongitude() {
        return this.longitude.doubleValue();
    }

    public void receiveCoordinates(double lat, double lgt) {
        this.latitude.set(lat);
        this.longitude.set(lgt);
    }   

    public String toString() {
        return latitude + " ; " + longitude;
    }
}