package com.estore.api.estoreapi.model;

public enum Size {
    SMALL("S"),
    MEDIUM("M"),
    LARGE("L"),
    X_LARGE("XL"),
    XX_LARGE("XXL");

    private String sizeString;

    private Size(String sizeString) {
        this.sizeString = sizeString;
    }

    @Override
    public String toString() {
        return this.sizeString;
    }
}
