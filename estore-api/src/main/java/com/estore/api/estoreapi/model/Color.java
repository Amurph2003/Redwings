package com.estore.api.estoreapi.model;

public enum Color {
    RED("Red"),
    WHITE("White"),
    BLACK("Black");

    private String colorString;

    private Color(String colorString) {
        this.colorString = colorString;
    }

    @Override
    public String toString() {
        return this.colorString;
    }
}
