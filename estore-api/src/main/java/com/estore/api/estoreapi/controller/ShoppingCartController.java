package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.persistence.ShoppingCartDAO;

/**
 * Handles the REST API requests for products in the shopping cart
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author Team Red Wings
 */
@RestController
@RequestMapping("shopping-carts")
public class ShoppingCartController {
    private static final Logger LOG = Logger.getLogger(ShoppingCartController.class.getName());
    private ShoppingCartDAO shoppingCartDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param shoppingCartDao The {@link ShoppingCartDAO Shopping Cart Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public ShoppingCartController(ShoppingCartDAO shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    /**
     * Adds the {@linkplain Jersey jersey} to the user's shopping cart
     * 
     * @param username The username of the user updating their shopping cart
     * 
     * @param jersey The {@link Jersey jersey} to add
     * 
     * @return ResponseEntity with added {@link Jersey jersey} object and HTTP status of OK if updated
     * ResponseEntity with HTTP status of NOT_FOUND if the cart is not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/{username}")
    public ResponseEntity<Jersey> addJersey(@PathVariable String username, @RequestBody Jersey jersey){
        LOG.info("PUT /shopping-carts/ " + username);
        try{
            Jersey found = shoppingCartDao.addJersey(username, jersey);
            if(found != null)
                return new ResponseEntity<Jersey>(found, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Jersey Jersey} from the user's shopping cart
     * 
     * @param username The username of the user deleting from their cart
     * 
     * @param jersey The id of the {@link Jersey Jersey} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted
     * ResponseEntity with HTTP status of NOT_FOUND if not found
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<Jersey> deleteJersey(@PathVariable String username, @RequestBody Jersey jersey) {
        LOG.info("DELETE /shopping-carts/" + username); 

        try {
            boolean bool = shoppingCartDao.removeJersey(username, jersey);  // delete
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
     * Gets the shopping cart for the given user
     * 
     * @param username The username of the user retrieving their shopping cart
     * 
     * @return ResponseEntity HTTP status of OK if successfuly retrieved<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{username}")
    public ResponseEntity<Jersey[]> getCart(@PathVariable String username){
        LOG.info("GET /shopping-carts/" + username);
        try {
            Jersey[] cart = shoppingCartDao.getCart(username);
            if (cart != null)
                return new ResponseEntity<Jersey[]>(cart, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        } catch(IOException e) {
           LOG.log(Level.SEVERE,e.getLocalizedMessage());
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{username}")
    public ResponseEntity<Jersey[]> createShoppingCart(@PathVariable String username) {
        LOG.info("POST /shopping-carts/" + username);

        try {
            boolean created = shoppingCartDao.createShoppingCart(username);
            if(created == false) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}