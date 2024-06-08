package com.brestats.exceptions;

/**
 * An exception that is thrown when the {@link com.brestats.model.dao.DBObject#constructor(String[]) constructor} method has not the correct arguments in the String[] argument.
 * @author IUT de Vannes - info 1B2 - Nathan ALEXANDRE - Louan CARRE - Merlin CAROMEL - Tasnim ISMAIL OMAR - Théau LEFRANC
 */
public class IncorectConstructorArguments extends Exception {
    /**
     * Initiate an instance of the exception, calling the constructor of {@link Exception}
     * @param m the message
     */
    public IncorectConstructorArguments(String m) {
        super(m);
    }
}
