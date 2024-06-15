package com.brestats.model;

/**
 * Interface that defines the differents class representing a database's table
 * in the model
 * 
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin
 *         CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public interface Model {
    /**
     * Return the unique id of an item as {@link String String}, that can be used to
     * find the item in an Array
     * 
     * @return A unique id
     */
    public abstract String getId();
}
