package com.badwallet.error;

public class FactureNotFoundException extends RuntimeException {

    public FactureNotFoundException(String message) {
        super(message);
    }
}
