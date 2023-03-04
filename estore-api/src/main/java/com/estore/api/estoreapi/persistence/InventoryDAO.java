package com.estore.api.estoreapi.persistence;

import java.io.IOException;

import com.estore.api.estoreapi.model.Jersey;

/**
 * Defines the interface for Inventory containment persistence.
 * 
 * @author Team Red Wings
 */
public interface InventoryDAO {

    /**
     * Finds all {@linkplain Jersey jerseys} whose name contains the given text
     * 
     * @param containsText The text to match against
     * 
     * @return An array of {@link Jersey jerseys} whose names contain the given text, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey[] findJerseys(String containsText) throws IOException;

    /**
     * Updates and saves a {@linkplain Jersey jersey}
     * 
     * @param {@link Jersey jersey} object to be updated and saved
     * 
     * @return updated {@link Jersey jersey} if successful, null if
     * {@link Jersey jersey} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Jersey updateJersey(Jersey jersey) throws IOException;

     /**
     * Deletes a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@link Jersey jersey}
     * 
     * @return true if the {@link Jersey jersey} was deleted
     * <br>
     * false if jersey with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */


    boolean deleteJersey(int id) throws IOException;

    /**
     * Retrieves all {@linkplain Jersey jerseys}
     * 
     * @return An array of {@link Jerseys jerseys} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey[] getInventory() throws IOException;


    /**
     * Retrieves a {@linkplain Jersey jersey} with the given id
     * 
     * @param id The id of the {@link Jersey jersey} to get
     * 
     * @return a {@link Jersey jersey} object with the matching id
     * <br>
     * null if no {@link Jersey jersey} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey getJersey(int id) throws IOException;


    /**
     * Creates and saves a {@linkplain Jersey jersey}
     * 
     * @param jersey {@linkplain Jersey jersey} object to be created and saved
     * <br>
     * The id of the jersey object is ignored and a new unique id is assigned
     *
     * @return new {@link Jersey jersey} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Jersey createJersey(Jersey jersey) throws IOException;
}
