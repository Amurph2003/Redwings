package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.estore.api.estoreapi.model.Jersey;
import com.estore.api.estoreapi.model.ShoppingCart;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartFileDAO implements ShoppingCartDAO {

    // Loggers!

    private static final Logger LOG = Logger.getLogger(ShoppingCartFileDAO.class.getName());
    // Map system like inventory, but uses usernames instead of id's
    Map<String, ShoppingCart> shoppingCarts;
    private ObjectMapper objectMapper;  
    private String filename;

    /**
     * Creates a Shopping Cart File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public ShoppingCartFileDAO(@Value("${shopping-carts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();
    }

    /**
     * Saves the {@linkplain Jersey jerseys} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Jersey jerseys} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        ShoppingCart[] cartArray = getShoppingCarts(null);

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),cartArray);
        return true;
    }
   
    /**
     * Loads {@linkplain Jersey jersey} from the JSON file into the map
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        shoppingCarts = new TreeMap<>();

        // Deserializes the JSON objects from the file into an array of shopping carts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        ShoppingCart[] cartArray = objectMapper.readValue(new File(filename),ShoppingCart[].class);
        
        // Add each shopping cart to the tree map
        for (ShoppingCart cart: cartArray) {
            shoppingCarts.put(cart.getName(), cart);
        }
        return true;
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
    private ShoppingCart[] getShoppingCarts(String username) { // if containsText == null, no filter
        ArrayList<ShoppingCart> cartArrayList = new ArrayList<>();

        for (ShoppingCart cart : shoppingCarts.values()) {
            cartArrayList.add(cart);
        }

        ShoppingCart[] cartArray = new ShoppingCart[cartArrayList.size()];
        cartArrayList.toArray(cartArray);
        return cartArray;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean removeJersey(String username, Jersey jersey) throws IOException {
        ShoppingCart currentCart = shoppingCarts.get(username);
        synchronized(currentCart) {
            // if jersey exists in cart, then decrement quantity or delete if only 1 exists
            if (currentCart != null && currentCart.getName().equals(username) && currentCart.getCart().contains(jersey)) {
                int index = currentCart.getCart().indexOf(jersey);
                Jersey inCart = currentCart.getCart().get(index);
                if (inCart.getQuantity() == 1) {
                    LOG.info("1 left");
                    currentCart.removeJersey(jersey);
                } else {
                    LOG.info("More then 1 left");
                    inCart.decrementQuantity();
                }
                return save();
            }
        
            return false; // doesn't exist
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey addJersey(String username, Jersey jersey) throws IOException {
        ShoppingCart currentCart = shoppingCarts.get(username);
        if(currentCart != null) {
            synchronized(currentCart) {
                if(!currentCart.addJersey(jersey)) {
                    LOG.info("Equals Cart!");
                    int quantityIncrease = 1;
                    int index = currentCart.getCart().indexOf(jersey);
                    Jersey inCart = currentCart.getCart().get(index);
                    inCart.addToQuantity(quantityIncrease);
                    jersey = inCart;
                }
                save(); // may throw an IOException
            }
        } else {
            return null;
        }
        return jersey;
    }

     /**
    ** {@inheritDoc}
     */
    @Override
    public Jersey[] getCart(String username) throws IOException {
        synchronized(shoppingCarts) {
            if (shoppingCarts.containsKey(username)) {
                ArrayList<Jersey> cartList = shoppingCarts.get(username).getCart();
                Jersey[] cartArray = new Jersey[cartList.size()];
                cartList.toArray(cartArray);
                return cartArray;
            } else {
                return null;
            }
        }
    }

    @Override
    public boolean createShoppingCart(String username) throws IOException {
        ShoppingCart newCart = new ShoppingCart(username);
        for(String key: shoppingCarts.keySet()) {
            if(key.equals(username)) {
                return false;
            }
        }
        shoppingCarts.put(username, newCart);
        save();
        return true;
    }
}