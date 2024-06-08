package com.brestats.model.data;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Represents a position on a map, with latitude and longitude as {@link DoubleProperty} attributes.
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class MapCoordinates {
    private DoubleProperty latitude;
    private DoubleProperty longitude;

    /**
     * Construct a {@code MapCoordinates} object and initialize both attribute to 0
     */
    public MapCoordinates() {
        this.latitude = new SimpleDoubleProperty(0);
        this.longitude = new SimpleDoubleProperty(0);
    }

    /**
     * Getter for the latitude property. To get the value, see {@link #getLatitude()}
     * @return the latitude property
     */
    public DoubleProperty getLatitudeProperty() {
        return this.latitude;
    }

    /**
     * Getter for the longitude property. To get the value, see {@link #getLongitude()}
     * @return the longitude property
     */
    public DoubleProperty getLongitudeProperty() {
        return this.longitude;
    }

    /**
     * Getter for the latitude value. To get the property, see {@link #getLatitudeProperty()}
     * @return the latitude value
     */
    public double getLatitude() {
        return this.latitude.doubleValue();
    }

    /**
     * Getter for the longitude value. To get the property, see {@link #getLongitudeProperty()}
     * @return the longitude value
     */
    public double getLongitude() {
        return this.longitude.doubleValue();
    }

    /**
     * Method called from javascript script.js file, when user place a marker by clicking on the map
     * @param lat marker's latitude
     * @param lgt marker's longitude
     */
    public void receiveCoordinates(double lat, double lgt) {
        this.latitude.set(lat);
        this.longitude.set(lgt);
    }   

    /**
     * Return a String representation
     */
    public String toString() {
        return latitude + " ; " + longitude;
    }
}