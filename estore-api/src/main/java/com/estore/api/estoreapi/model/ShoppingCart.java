package com.estore.api.estoreapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a customer's shopping cart.
 * 
 * @author Team Red Wings
 */
public class ShoppingCart {
    // fields
    static final String STRING_FORMAT = "Shopping Cart [name=%s, contents=%s]";

    @JsonProperty("username") private String username;
    @JsonProperty("cart") private ArrayList<Jersey> cart;

    /**
     * Create a new shopping cart
     * @param name is the name of the user to create a shopping cart for
     */
    public ShoppingCart(@JsonProperty("username") String name) {
        this.username = name;
        this.cart = new ArrayList<>();
    }

    // accessors as needed
    public String getName() {
        return this.username;
    }

    public ArrayList<Jersey> getCart() {
        return this.cart;
    }

    // add Jersey method
    public boolean addJersey(Jersey jersey) {
        if (cart.contains(jersey) == false) {
            cart.add(jersey);
            return true;
        }
        return false;
    }

    // remove Jersey method
    public Jersey removeJersey(Jersey jersey) {
        if (cart != null && cart.contains(jersey)) { 
            cart.remove(jersey);
            return jersey;
        }
        // if none match, return null
        return null;
    }

    @Override
    /**
     * ToString method
     */
    public String toString() {
        return String.format(STRING_FORMAT, username, cart);
    }
}