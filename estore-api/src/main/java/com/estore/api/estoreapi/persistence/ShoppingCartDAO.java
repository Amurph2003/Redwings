package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Jersey;

/**
 * Defines the interface for Shopping Cart persistence.
 * 
 * @author Team Red Wings
 */
public interface ShoppingCartDAO {
    

     /**
     * Removes a {@linkplain Jersey jersey} with the given id from the shopping cart of a given user
     * 
     * @param username The username that corresponds to a shopping cart
     * 
     * @param jersey The {@link Jersey jersey} object being removed from the cart
     * 
     * @return true if the {@link Jersey jersey} was deleted
     * 
     * false if jersey with the given signature does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean removeJersey(String username, Jersey jersey) throws IOException;

    /**
     * Adds and saves a {@linkplain Jersey jersey} with the given body from the shopping cart of a given user
     * 
     * @param username The username that corresponds to a shopping cart
     * 
     * @param jersey The {@linkplain Jersey jersey} object to be added and saved
     *
     * @return new {@link Jersey jersey} if successful, null otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey addJersey(String username, Jersey jersey) throws IOException;

    /**
     * Retrieves the shopping cart for the given user
     * 
     * @param username The username of the user retrieving their shopping cart
     * 
     * @return an array of {@link Jersey jersey} objects in the user's cart
     * <br>
     * null if the user has no shopping cart
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey[] getCart(String username) throws IOException;

    /**
     * Adds a new shopping cart for the given user to the storage
     * 
     * @param username The username of the user having their cart added to storage
     * 
     * @return True if the user's shopping cart was successfuly added, false if the 
     * user already has a shopping cart.
     * 
     * @throws IOException if an issue with underlying storage
     */
    boolean createShoppingCart(String username) throws IOException;
}

