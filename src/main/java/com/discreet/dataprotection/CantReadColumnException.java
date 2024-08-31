package com.discreet.dataprotection;

public class CantReadColumnException extends RuntimeException {
    public CantReadColumnException(String message) {
        super(message);
    }
}
