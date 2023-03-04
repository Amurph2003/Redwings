package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.InventoryDAO;

/**
 * Handles the REST API requests for the Product resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Red Wings
 */
@RestController
@RequestMapping("products")
public class InventoryController {
    private static final Logger LOG = Logger.getLogger(InventoryController.class.getName());
    private InventoryDAO inventoryDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param inventoryDao The {@link InventoryDAO Inventory Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public InventoryController(InventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Updates the {@linkplain Jersey jersey} with the provided {@linkplain Jersey jersey} object, if it exists
     * 
     * @param jersey The {@link Jersey jersey} to update
     * 
     * @return ResponseEntity with updated {@link Jersey jersey} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Jersey> updateJersey(@RequestBody Jersey jersey){
        LOG.info("PUT /products " + jersey);
        try{
            Jersey found = inventoryDao.updateJersey(jersey);
            if(found != null)
                if(found.equals(jersey))
                    return new ResponseEntity<Jersey>(found, HttpStatus.OK);
                else
                    return new ResponseEntity<Jersey>(found, HttpStatus.CONFLICT);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Jersey Jersey} with the given id
     * 
     * @param id The id of the {@link Jersey Jersey} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Jersey> deleteJersey(@PathVariable int id) {
        LOG.info("DELETE /products/" + id); 

        try {
            boolean bool = inventoryDao.deleteJersey(id);  // delete
            if (bool) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

           return new ResponseEntity<>(HttpStatus.NOT_FOUND); // jersey not found
        }
        catch (IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    /** 
     * Responds to the GET request for all {@linkplain Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jersey} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Jersey[]> getInventory() {
        LOG.info("GET /products");

        try {
            Jersey[] jerseys = inventoryDao.getInventory();
            
            if (jerseys != null) { // not empty
                return new ResponseEntity<Jersey[]>(jerseys, HttpStatus.OK);
            }

            else { // empty
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            }
        }

        catch(IOException e) {
                    LOG.log(Level.SEVERE,e.getLocalizedMessage());
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Responds to the GET request for all {@linkplain Jersey jerseys} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Jersey jerseys}
     * 
     * @return ResponseEntity with array of {@link Jersey jersey} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/")
    public ResponseEntity<Jersey[]> searchJerseys(@RequestParam String name) {
        LOG.info("GET /products/?name="+name);

        try {
            Jersey[] jerseys = inventoryDao.findJerseys(name);
            if (jerseys != null)
                return new ResponseEntity<Jersey[]>(jerseys, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch(IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getLocalizedMessage());
            return new ResponseEntity<Jersey[]>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the {@linkplain Jersey Jersey} with the given id
     * 
     * @param id The id of the {@link Jersey Jersey} to get
     * 
     * @return ResponseEntity HTTP status of OK if successfuly retrieved<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Jersey> getJersey(@PathVariable int id){
        LOG.info("GET /products/" + id);
        try {
            Jersey jersey = inventoryDao.getJersey(id);
            if (jersey != null)
                return new ResponseEntity<Jersey>(jersey, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        } catch(IOException e) {
           LOG.log(Level.SEVERE,e.getLocalizedMessage());
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    /**
     * Creates a {@linkplain Jersey jersey} with the provided jersey object
     * 
     * @param jersey - The {@link Jersey jersey} to create
     * 
     * @return ResponseEntity with created {@link Jersey jersey} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Jersey jersey} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Jersey> createJersey(@RequestBody Jersey jersey) {
        LOG.info("POST /products " + jersey);
        try {
            Jersey newJersey = inventoryDao.createJersey(jersey);
            if(newJersey == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else if(newJersey.getQuantity() != jersey.getQuantity()) {
                return new ResponseEntity<>(newJersey, HttpStatus.OK);
            }
            return new ResponseEntity<>(newJersey, HttpStatus.CREATED);
        } catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
