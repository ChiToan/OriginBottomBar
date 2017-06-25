/**
 * @author HTI students, Spring 2013
 */
package com.example.s165097.originbottombar;

public class InvalidInputValueException extends Exception {

    public InvalidInputValueException() {
        super();
    }

    public InvalidInputValueException(String msg) {
        super(msg);
    }

    public InvalidInputValueException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidInputValueException(Throwable cause) {
        super(cause);
    }
}