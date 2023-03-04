package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.Jersey;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;

/**
 * Implements the functionality for JSON file-based peristance for jerseys in the Inventory
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author Team Red Wings
 */
@Component
public class InventoryFileDAO implements InventoryDAO {
    private static final Logger LOG = Logger.getLogger(InventoryFileDAO.class.getName());
    private static int nextId;
    Map<Integer,Jersey> jerseys;   // Provides a local cache of the jersey objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Jersey
                                        // objects and JSON text format written
                                        // to the file
    private String filename;    // Filename to read from and write to

    /**
     * Creates an Inventory File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public InventoryFileDAO(@Value("${inventory.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Generates the next id for a new {@linkplain Jersey jersey}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Jersey jerseys} from the tree map
     * 
     * @return  The array of {@link Jersey jerseys}, may be empty
     */
    private Jersey[] getJerseysArray() {
        return getJerseysArray(null);
    }

    /**
     * Generates an array of {@linkplain Jersey jerseys} from the tree map for any
     * {@linkplain Jersey jerseys} that contains the text specified by containsText
     * <br>
     * If containsText is null, the array contains all of the {@linkplain Jersey jerseys}
     * in the tree map
     * 
     * @return  The array of {@link Jersey jerseys}, may be empty
     */
    private Jersey[] getJerseysArray(String containsText) { // if containsText == null, no filter
        ArrayList<Jersey> jerseyArrayList = new ArrayList<>();

        for (Jersey jersey : jerseys.values()) {
            String jerseyName = jersey.getName().toLowerCase();
            if (containsText != null)
                containsText = containsText.toLowerCase();
            if (containsText == null || jerseyName.contains(containsText)) {
                jerseyArrayList.add(jersey);
            }
        }

        Jersey[] jerseyArray = new Jersey[jerseyArrayList.size()];
        jerseyArrayList.toArray(jerseyArray);
        return jerseyArray;
    }

    /**
     * Saves the {@linkplain Jersey jerseys} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Jersey jerseys} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Jersey[] jerseyArray = getJerseysArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),jerseyArray);
        return true;
    }
   
    /**
     * Loads {@linkplain Jersey jersey} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        jerseys = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of jerseys
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Jersey[] jerseyArray = objectMapper.readValue(new File(filename),Jersey[].class);

        // Add each jersey to the tree map and keep track of the greatest id
        for (Jersey jersey : jerseyArray) {
            jerseys.put(jersey.getId(),jersey);
            if (jersey.getId() > nextId)
                nextId = jersey.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey[] findJerseys(String containsText) {
        synchronized(jerseys) {
            return getJerseysArray(containsText);
        }
    }
    
    /*
     * {@inheritDoc}
     */
    @Override
    public Jersey updateJersey(Jersey jersey) throws IOException{
        synchronized(jerseys){
            if (jerseys.containsKey(jersey.getId()) == false)
                return null;
            for(Jersey current : jerseys.values()) {
                if(!(current.getId() == jersey.getId())) {
                    if(current.conflicts(jersey)) {
                        return current;
                    }
                }
            }
            jerseys.put(jersey.getId(), jersey);
            save();
            return jersey;
        }
    }

     /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey getJersey(int id) throws IOException {
        synchronized(jerseys) {
            if (jerseys.containsKey(id)) {
                return jerseys.get(id);
            } else {
                return null;
            }
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteJersey(int id) throws IOException {
        synchronized(jerseys) {

            // if jersey exists, then delete
            if (jerseys!=null && jerseys.containsKey(id)) { 
                jerseys.remove(id);
                return save();
            }
        
            return false; // doesn't exist
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey[] getInventory() {
        synchronized(jerseys) {
            return getJerseysArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey createJersey(Jersey jersey) throws IOException {
        synchronized(jerseys) {
            // We create a new jersey object because the id field is immutable
            // and we need to assign the next unique id
            Jersey newJersey = new Jersey(nextId(),jersey.getName(), jersey.getNumber(), jersey.getPrice(), jersey.getSize(), jersey.getColor(), jersey.getQuantity(), jersey.getImageUrl());
            for (int key : jerseys.keySet()) {
                if (jerseys.get(key).conflicts(newJersey)) {
                    LOG.info("Conflicts!");
                    return null;
                } else if(jerseys.get(key).equals(newJersey)) {
                    LOG.info("Equals!");
                    int add = newJersey.getQuantity();
                    newJersey = jerseys.get(key);
                    newJersey.addToQuantity(add);
                    jerseys.put(key, newJersey);
                    save();
                    return newJersey;
                }
            }
            LOG.info("We good!");
            jerseys.put(newJersey.getId(),newJersey);
            save(); // may throw an IOException
            return newJersey;
        }
    }

}
